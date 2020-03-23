package com.github.onlinestore.orderservice

import com.github.onlinestore.orderservice.dimodules.mainModule
import com.github.onlinestore.orderservice.restapi.routes.OrderRouter
import io.javalin.Javalin
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin

class App(
    private val orderRouter: OrderRouter
) : KoinComponent {

    fun start(httpPort: Int): Javalin {

        val app = Javalin
            .create { config ->
                config.enableDevLogging()
            }
            .routes {
                orderRouter.addRoutes()
            }
            .start(httpPort)

        return app
    }
}

fun main() {

    //Disabling Jetty logs
    System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog")
    System.setProperty("org.eclipse.jetty.LEVEL", "OFF")

    startKoin {
        modules(mainModule)
    }
        .koin
        .get<App>()
        .start(7001)
}
