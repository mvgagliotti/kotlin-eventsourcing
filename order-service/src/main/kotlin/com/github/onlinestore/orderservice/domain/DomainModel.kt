package com.github.onlinestore.orderservice.domain

import com.github.onlinestore.orderservice.serializable.CborSerializable
import java.math.BigDecimal

val EMPTY_ORDER: Order = Order("", "", listOf())

data class Order(
    val id: String,
    val userId: String,
    val items: List<Item>) : CborSerializable {

    fun total(): BigDecimal {
        return BigDecimal.ZERO //TODO: implement
    }
}

data class Item(
    val id: String,
    val description: String,
    val value: BigDecimal,
    val amount: Int)

data class Payment(
    val orderId: String,
    val value: BigDecimal)

data class Refund(
    val orderId: String,
    val value: BigDecimal)
