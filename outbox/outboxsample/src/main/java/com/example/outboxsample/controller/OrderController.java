package com.example.outboxsample.controller;

import com.example.outboxsample.dto.OrderRequest;
import com.example.outboxsample.entity.Order;
import com.example.outboxsample.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public Order addOrder(@RequestBody OrderRequest request) {
    Order order = new Order();
    order.setProduct(request.product());
    order.setQuantity(request.quantity());
    return orderService.addOrder(order);
  }
}
