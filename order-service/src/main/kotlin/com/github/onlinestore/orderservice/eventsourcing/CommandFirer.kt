package com.github.onlinestore.orderservice.eventsourcing

import java.util.concurrent.CompletionStage

interface CommandFirer<Command, Event> {

    fun fire(command: Command, entityId: String): CompletionStage<Event>

}
