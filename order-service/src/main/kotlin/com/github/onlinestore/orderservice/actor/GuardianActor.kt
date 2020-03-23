package com.github.onlinestore.orderservice.actor

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors

val guardianActor: Behavior<Void> = Behaviors.setup { ctx ->
    Behaviors.empty<Void>()
}