package com.daelim.service

import com.daelim.model.domain.Order
import com.daelim.model.dto.OrderCreatedEvent
import com.daelim.repository.OrderRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service


@Service
class OrderService(
    private val userService: UserService,
    private val orderRepository: OrderRepository,
    private val kafkaTemplate: KafkaTemplate<String, OrderCreatedEvent>
) {
    fun createOrder(product: String, quantity: Int): Order {
        val userId = userService.getUserId()
        val order = Order(userId = userId, product = product, quantity = quantity)
        val savedOrder = orderRepository.save(order)
        kafkaTemplate.send("order-created", OrderCreatedEvent(savedOrder.id, userId, product, quantity))
        return savedOrder
    }
    fun getOrdersByUserId(userId: Long): List<Order> {
        return orderRepository.findByUserId(userId)
    }
}