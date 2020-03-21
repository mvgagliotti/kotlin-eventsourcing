package com.github.onlinestore.orderservice.eventsourcing

/**
 * A contract for an event sourced aggregate
 *
 */
interface ESAggregate<Command, Event, State> {

    fun emptyState(): State

    fun handleCommand(command: Command, state: State): Event

    fun handleEvent(currentState: State, event: Event): State
}
