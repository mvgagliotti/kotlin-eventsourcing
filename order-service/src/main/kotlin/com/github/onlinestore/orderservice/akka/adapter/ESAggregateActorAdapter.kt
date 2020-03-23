package com.github.onlinestore.orderservice.akka.adapter

import akka.actor.typed.ActorRef
import akka.persistence.typed.ExpectingReply
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.javadsl.CommandHandler
import akka.persistence.typed.javadsl.EventHandler
import akka.persistence.typed.javadsl.EventSourcedBehavior
import com.github.onlinestore.orderservice.eventsourcing.DoNotPersistEvent
import com.github.onlinestore.orderservice.eventsourcing.ESAggregate
import org.slf4j.LoggerFactory
import sun.rmi.runtime.Log
import kotlin.reflect.full.findAnnotation

data class CommandWithReplyTo<Command, Event>(
    val command: Command,
    val replyTo: ActorRef<Event>
) : ExpectingReply<Event> {
    override fun replyTo() = replyTo
}

abstract class EventSourcingActorAdapter<Command, Event, State>(persistenceId: PersistenceId?) :
    EventSourcedBehavior<CommandWithReplyTo<Command, Event>, Event, State>(persistenceId) {


    val logger = LoggerFactory.getLogger(EventSourcingActorAdapter::class.java)

    abstract val adaptee: ESAggregate<Command, Event, State>

    override fun emptyState(): State = adaptee.emptyState()

    override fun commandHandler(): CommandHandler<CommandWithReplyTo<Command, Event>, Event, State> {

        return newCommandHandlerBuilder()
            .forAnyState()
            .onCommand(
                CommandWithReplyTo::class.java as Class<CommandWithReplyTo<Command, Event>>
            ) { state, cmdWithReplyTo ->
                adaptee
                    .handleCommand(cmdWithReplyTo.command, state)
                    .let { event ->

                        if ((event as Any)::class.findAnnotation<DoNotPersistEvent>() != null) {
                            logger.info("Skipping persisting for $event")
                            Effect().reply(cmdWithReplyTo, event)
                        } else {
                            Effect()
                                .persist(event)
                                .thenRun { cmdWithReplyTo.replyTo.tell(event) }
                        }

                    }
            }.build()
    }

    override fun eventHandler(): EventHandler<State, Event> = EventHandler { state, event ->
        adaptee.handleEvent(state, event)
    }
}