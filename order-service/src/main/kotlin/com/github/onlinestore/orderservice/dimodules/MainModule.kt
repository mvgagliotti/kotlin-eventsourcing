package com.github.onlinestore.orderservice.dimodules

import akka.actor.typed.ActorSystem
import com.github.onlinestore.orderservice.App
import com.github.onlinestore.orderservice.actor.OrderActor
import com.github.onlinestore.orderservice.actor.guardianActor
import com.github.onlinestore.orderservice.akka.factory.CommandFirerFactory
import com.github.onlinestore.orderservice.domain.OrderCommand
import com.github.onlinestore.orderservice.domain.OrderEvent
import com.github.onlinestore.orderservice.eventsourcing.CommandFirer
import com.github.onlinestore.orderservice.restapi.controllers.OrderController
import com.github.onlinestore.orderservice.restapi.routes.OrderRouter
import com.typesafe.config.ConfigFactory
import org.koin.dsl.module

fun startMainModule(values: Map<String, String>) = module {

    single<ActorSystem<Void>> {
        //TODO: move boilerplate code to a factory
        val config =
            ConfigFactory
                .parseString(
                    """
                    akka.remote.artery.canonical.port = ${values["akka-node-port"]}    
                    """.trimIndent()
                )
                .withFallback(ConfigFactory.defaultApplication())

        val mySystem = ActorSystem.create<Void>(
            guardianActor,
            "my-system",
            config
        )
        return@single mySystem
    }

    single<CommandFirer<OrderCommand, OrderEvent>> {
        CommandFirerFactory().create("OrderEntity", get()) { id -> OrderActor(id) }
    }

    single { OrderController(get()) }
    single { OrderRouter(get()) }
    single { App(get()) }
}
