package com.github.onlinestore.orderservice.restapi.routes

import com.github.onlinestore.orderservice.restapi.controllers.OrderController
import io.javalin.apibuilder.ApiBuilder

class OrderRouter(
    private val orderController: OrderController
) {
    fun addRoutes() {
        ApiBuilder.path("order") {
            ApiBuilder.post(orderController::post)
        }
    }
}