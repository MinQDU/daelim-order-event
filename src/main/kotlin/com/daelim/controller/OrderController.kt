package com.daelim.controller

import com.daelim.model.domain.Order
import com.daelim.service.OrderService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController (
    private val orderService: OrderService
){
    @PostMapping
    fun createOrder(
        @RequestParam product: String,
        @RequestParam quantity: Int
    ): ResponseEntity<Order> {
        val order = orderService.createOrder(product, quantity)
        return ResponseEntity.ok(order)
    }

    @GetMapping
    fun getOrdersByUserId(
        @RequestParam userId: Long
    ): ResponseEntity<List<Order>> {
        val orders = orderService.getOrdersByUserId(userId)
        return ResponseEntity.ok(orders)
    }
}