package com.github.onlinestore.orderservice.dimodules

import com.github.onlinestore.orderservice.App
import com.github.onlinestore.orderservice.restapi.controllers.OrderController
import com.github.onlinestore.orderservice.restapi.routes.OrderRouter
import org.koin.dsl.module

val mainModule = module {
    single { OrderController() }
    single { OrderRouter(get()) }
    single { App(get()) }
}