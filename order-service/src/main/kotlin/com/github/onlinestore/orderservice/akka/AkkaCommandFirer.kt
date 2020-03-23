package com.github.onlinestore.orderservice.akka

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.AskPattern
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.japi.function.Function
import com.github.onlinestore.orderservice.akka.adapter.CommandWithReplyTo
import com.github.onlinestore.orderservice.akka.factory.ShardRegionActorRef
import com.github.onlinestore.orderservice.eventsourcing.CommandFirer
import java.time.Duration
import java.util.concurrent.CompletionStage

class AkkaCommandFirer<Command, Event>(
    val sr: ShardRegionActorRef<Command, Event>,
    val system: ActorSystem<Void>
) : CommandFirer<Command, Event> {

    override fun fire(command: Command, entityId: String): CompletionStage<Event> {
        return AskPattern
            .ask<ShardingEnvelope<CommandWithReplyTo<Command, Event>>, Event>(
                sr,
                Function { replyTo ->
                    ShardingEnvelope(entityId, CommandWithReplyTo(command, replyTo))
                },
                Duration.ofSeconds(4),
                system.scheduler()
            )
    }
}

fun <T, R> ActorRef<T>.ask(
    actorSystem: ActorSystem<Void>,
    msg: T,
    func: (T, ActorRef<R>) -> T
) = AskPattern.ask<T, R>(
    this,
    Function { replyTo ->
        func.invoke(msg, replyTo)
    },
    Duration.ofSeconds(4),
    actorSystem.scheduler()
)