package com.github.onlinestore.orderservice.actor

import akka.persistence.typed.PersistenceId
import com.github.onlinestore.orderservice.akka.adapter.EventSourcingActorAdapter
import com.github.onlinestore.orderservice.domain.OrderAggregate
import com.github.onlinestore.orderservice.domain.OrderCommand
import com.github.onlinestore.orderservice.domain.OrderEvent
import com.github.onlinestore.orderservice.domain.OrderState
import com.github.onlinestore.orderservice.eventsourcing.ESAggregate

class OrderActor(
    persistenceId: PersistenceId
) : EventSourcingActorAdapter<OrderCommand, OrderEvent, OrderState>(persistenceId) {

    override val adaptee: ESAggregate<OrderCommand, OrderEvent, OrderState>
        get() = OrderAggregate
}