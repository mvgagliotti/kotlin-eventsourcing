package com.github.onlinestore.orderservice.akka.factory

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.javadsl.ClusterSharding
import akka.cluster.sharding.typed.javadsl.Entity
import akka.cluster.sharding.typed.javadsl.EntityTypeKey
import akka.persistence.typed.PersistenceId
import com.github.onlinestore.orderservice.akka.AkkaCommandFirer
import com.github.onlinestore.orderservice.akka.adapter.CommandWithReplyTo
import com.github.onlinestore.orderservice.akka.adapter.EventSourcingActorAdapter

typealias ESActorAdapterFunction<Command, Event, State> =
    (PersistenceId) -> EventSourcingActorAdapter<Command, Event, State>

typealias ShardRegionActorRef<Command, Event> = ActorRef<ShardingEnvelope<CommandWithReplyTo<Command, Event>>>

class CommandFirerFactory {

    fun <Command, Event, State> create(
        entityName: String,
        actorSystem: ActorSystem<Void>,
        esActorAdapterFunction: ESActorAdapterFunction<Command, Event, State>): AkkaCommandFirer<Command, Event> {

        val entityTypeKey: EntityTypeKey<CommandWithReplyTo<Command, Event>> =
            EntityTypeKey
                .create(CommandWithReplyTo::class.java as Class<CommandWithReplyTo<Command, Event>>, entityName)

        val sharding = ClusterSharding.get(actorSystem)

        val shardRegion: ActorRef<ShardingEnvelope<CommandWithReplyTo<Command, Event>>> =
            sharding.init(
                Entity.of(entityTypeKey) { entityCtx ->
                    val id = PersistenceId(entityTypeKey.name() + "|" + entityCtx.entityId)
                    esActorAdapterFunction.invoke(id)
                })
        return AkkaCommandFirer(shardRegion, actorSystem)
    }

}
