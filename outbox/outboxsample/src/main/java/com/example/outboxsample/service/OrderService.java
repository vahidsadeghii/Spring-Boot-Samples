package com.example.outboxsample.service;

import com.example.outboxsample.entity.Order;
import com.example.outboxsample.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final OutboxService outboxService;

  public OrderService(OrderRepository orderRepository, OutboxService outboxService) {
    this.orderRepository = orderRepository;
    this.outboxService = outboxService;
  }

  @Transactional
  public Order addOrder(Order order) {
    orderRepository.save(order);

    outboxService.storeEvent(
        "Order",
        null,
        "OrderCreated",
        String.format(
            "{\"product\":\"%s\",\"quantity\":%d}", order.getProduct(), order.getQuantity()));
    return orderRepository.save(order);
  }
}
