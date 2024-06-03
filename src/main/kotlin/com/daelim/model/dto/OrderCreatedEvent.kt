package com.daelim.model.dto

data class OrderCreatedEvent(
    val orderId: Long,
    val userId: Long,
    val product: String,
    val quantity: Int
)