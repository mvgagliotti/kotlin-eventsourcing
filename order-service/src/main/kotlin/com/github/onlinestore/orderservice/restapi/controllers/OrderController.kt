package com.github.onlinestore.orderservice.restapi.controllers

import com.github.onlinestore.orderservice.domain.*
import com.github.onlinestore.orderservice.eventsourcing.CommandFirer
import com.github.onlinestore.orderservice.restapi.dtos.OrderCreatedResponseDTO
import com.github.onlinestore.orderservice.restapi.dtos.OrderDTO
import io.javalin.http.Context
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.*

class OrderController(
    private val commandFirer: CommandFirer<OrderCommand, OrderEvent>
) {

    private val logger = LoggerFactory.getLogger(OrderController::class.java)

    fun get(ctx: Context) {
        val id = ctx.pathParam("order-id")
        val command = Get(id)

        val future = commandFirer
            .fire(command, id)
            .toCompletableFuture()
            .thenApply { evt ->
                val order = (evt as GetEvent).order
                return@thenApply order
            }

        ctx.json(future)
    }

    fun post(ctx: Context) {
        logger.info("$commandFirer is ready!")

        //step1: translate to dto
        val dto = ctx.bodyAsClass(OrderDTO::class.java)
        logger.info(dto.toString())

        //step2: convert from dto to domain
        val id = UUID.randomUUID().toString()
        val items = dto.items.map {
            Item(
                id = it.id,
                description = it.description,
                amount = it.amount,
                value = BigDecimal.valueOf(10.0) //TODO: define the item value
            )
        }

        //step3: create the command and fire it
        val command: OrderCommand = CreateOrderCommand(
            id, "user-2", items //TODO: check where to get this user id from
        )

        val future =
            commandFirer
                .fire(command, id)
                .toCompletableFuture()
                .thenApply { orderCreatedEvent -> OrderCreatedResponseDTO(orderCreatedEvent.orderId) }

        //step4: set the async future to the result
        ctx.json(future)
    }
}
