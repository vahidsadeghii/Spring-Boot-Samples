package com.example.outboxsample.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.outboxsample.entity.OutboxEvent;
import com.example.outboxsample.repository.OutboxEventRepository;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
@Transactional
class OutboxServiceTest {

  @Autowired
  private OutboxService outboxService;

  @Autowired
  private OutboxEventRepository outboxEventRepository;

  @Test
  void testStoreEvent() {
    // Given
    String aggregateType = "User";
    String aggregateId = "user-123";
    String eventType = "UserCreated";
    String eventData = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

    // When
    OutboxEvent event = outboxService.storeEvent(aggregateType, aggregateId, eventType, eventData);

    // Then
    assertThat(event).isNotNull();
    assertThat(event.getId()).isNotNull();
    assertThat(event.getAggregateType()).isEqualTo(aggregateType);
    assertThat(event.getAggregateId()).isEqualTo(aggregateId);
    assertThat(event.getEventType()).isEqualTo(eventType);
    assertThat(event.getEventData()).isEqualTo(eventData);
    assertThat(event.getProcessed()).isFalse();
    assertThat(event.getEventTimestamp()).isNotNull();
    assertThat(event.getCreatedAt()).isNotNull();
  }

  @Test
  void testGetUnprocessedEventCount() {
    // Given
    outboxService.storeEvent("User", "user-1", "UserCreated", "{}");
    outboxService.storeEvent("User", "user-2", "UserCreated", "{}");
    outboxService.storeEvent("Order", "order-1", "OrderCreated", "{}");

    // When
    long count = outboxService.getUnprocessedEventCount();

    // Then
    assertThat(count).isEqualTo(3);
  }
}