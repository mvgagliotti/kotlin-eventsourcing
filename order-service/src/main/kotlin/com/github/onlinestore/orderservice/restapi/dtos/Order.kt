package com.github.onlinestore.orderservice.restapi.dtos

data class OrderDTO(
    val items: List<ItemDTO>
)

data class ItemDTO(
    val id: String,
    val description: String,
    val amount: Int
)