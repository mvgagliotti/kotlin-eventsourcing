package com.github.onlinestore.orderservice.domain

import com.github.onlinestore.orderservice.eventsourcing.DoNotPersistEvent
import com.github.onlinestore.orderservice.eventsourcing.ESAggregate
import com.github.onlinestore.orderservice.akka.serializable.CborSerializable

/////////////////
///Aggregate protocol
////////////////

//commands
sealed class OrderCommand(open val orderId: String) : CborSerializable

data class CreateOrderCommand(
    override val orderId: String,
    val userId: String,
    val items: List<Item>
) : OrderCommand(orderId)

data class AddItemsCommand(
    override val orderId: String,
    val items: List<Item>
) : OrderCommand(orderId)


data class Get(override val orderId: String) : OrderCommand(orderId)

//events
sealed class OrderEvent(open val orderId: String) : CborSerializable

data class OrderCreatedEvent(
    override val orderId: String,
    val userId: String,
    val items: List<Item>
) : OrderEvent(orderId)

data class ItemsAddedEvent(
    override val orderId: String,
    val items: List<Item>
) : OrderEvent(orderId)

@DoNotPersistEvent
data class GetEvent(
    override val orderId: String,
    val order: Order?
) : OrderEvent(orderId)

//state
data class OrderState(val order: Order = EMPTY_ORDER)

//////////////////////////
///Aggregate business logic
/////////////////////////

object OrderAggregate : ESAggregate<OrderCommand, OrderEvent, OrderState> {


    /**
     * Returns the initial state
     *
     */
    override fun emptyState(): OrderState = OrderState()

    /**
     * Handles commands for this aggregate
     *
     * @param state   the current state
     * @param command the command to be processed
     * @return the event to be persisted
     */
    override fun handleCommand(command: OrderCommand, state: OrderState): OrderEvent = when (command) {
        is CreateOrderCommand -> {
            check(command.items.isNotEmpty()) { "Cannot create an order without items" }
            OrderCreatedEvent(command.orderId, command.userId, command.items)
        }
        is AddItemsCommand ->
            ItemsAddedEvent(command.orderId, command.items)
        is Get -> {
            check(state.order != EMPTY_ORDER) { "Order ${command.orderId} has not been created" }
            GetEvent(command.orderId, state.order)
        }
    }

    /**
     * Handles the events for this aggregate
     *
     * @param state the aggregate curret state
     * @param event the event to be applied
     * @return the new state
     */
    override fun handleEvent(state: OrderState, event: OrderEvent): OrderState = when (event) {
        is OrderCreatedEvent ->
            OrderState(
                Order(
                    id = event.orderId,
                    userId = event.userId,
                    items = event.items
                )
            )

        is ItemsAddedEvent -> TODO()
        is GetEvent -> state
    }

}