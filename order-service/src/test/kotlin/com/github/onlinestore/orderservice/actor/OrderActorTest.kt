package com.github.onlinestore.orderservice.actor

import akka.actor.testkit.typed.javadsl.BehaviorTestKit
import akka.actor.testkit.typed.javadsl.TestKitJunitResource
import akka.persistence.typed.PersistenceId
import com.github.onlinestore.orderservice.akka.adapter.CommandWithReplyTo
import com.github.onlinestore.orderservice.domain.CreateOrderCommand
import com.github.onlinestore.orderservice.domain.OrderCreatedEvent
import com.github.onlinestore.orderservice.domain.OrderEvent
import org.junit.Test
import java.util.*

/**
 * Tests for OrderActor event sourced actor.
 */
class OrderActorTest {

    companion object {
        val testKit = TestKitJunitResource(
            """
            akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
            akka.persistence.snapshot-store.plugin = "akka.persistence.snapshot-store.local"
            akka.persistence.snapshot-store.local.dir = "target/snapshot-${UUID.randomUUID()}" 
            """.trimIndent()
        )
    }

    @Test
    fun testIt() {
        val orderActorRef = testKit.spawn(OrderActor(PersistenceId("Order|123")))
        val testProbe = testKit.createTestProbe<OrderEvent>()
        orderActorRef.tell(CommandWithReplyTo(CreateOrderCommand("", "", listOf()), testProbe.ref))
        //testProbe.expectMessage(OrderCreatedEvent("", "", listOf()))
        testProbe.expectNoMessage()
    }

}