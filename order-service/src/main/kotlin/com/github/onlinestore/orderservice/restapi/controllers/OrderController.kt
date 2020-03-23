package com.github.onlinestore.orderservice.restapi.controllers

import com.github.onlinestore.orderservice.restapi.dtos.OrderDTO
import io.javalin.http.Context
import org.slf4j.LoggerFactory

class OrderController {

    val logger = LoggerFactory.getLogger(OrderController::class.java)

    fun post(ctx: Context) {

        val dto  = ctx.bodyAsClass(OrderDTO::class.java)
        logger.info(dto.toString())

    }

}