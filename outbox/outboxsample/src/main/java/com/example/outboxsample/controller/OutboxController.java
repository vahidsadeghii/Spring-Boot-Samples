package com.example.outboxsample.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outboxsample.entity.OutboxEvent;
import com.example.outboxsample.service.OutboxService;

@RestController
@RequestMapping("/api/v1")
public class OutboxController {

  @Autowired private OutboxService outboxService;

  /**
   * Create a sample event to demonstrate outbox pattern
   */
  @PostMapping("/events")
  public ResponseEntity<OutboxEvent> createEvent(@RequestBody CreateEventRequest request) {
    OutboxEvent event =
        outboxService.storeEvent(
            request.getAggregateType(),
            request.getAggregateId(),
            request.getEventType(),
            request.getEventData());

    return ResponseEntity.ok(event);
  }

  /**
   * Get the count of unprocessed events
   */
  @GetMapping("/events/unprocessed/count")
  public ResponseEntity<Map<String, Long>> getUnprocessedEventCount() {
    long count = outboxService.getUnprocessedEventCount();
    return ResponseEntity.ok(Map.of("unprocessedCount", count));
  }

  /**
   * Health check endpoint
   */
  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health() {
    return ResponseEntity.ok(Map.of("status", "UP", "service", "outbox-sample"));
  }

  // DTO for event creation
  public static class CreateEventRequest {
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    private String eventData;

    // Constructors
    public CreateEventRequest() {}

    public CreateEventRequest(
        String aggregateType, String aggregateId, String eventType, String eventData) {
      this.aggregateType = aggregateType;
      this.aggregateId = aggregateId;
      this.eventType = eventType;
      this.eventData = eventData;
    }

    // Getters and setters
    public String getAggregateType() {
      return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
      this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
      return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
      this.aggregateId = aggregateId;
    }

    public String getEventType() {
      return eventType;
    }

    public void setEventType(String eventType) {
      this.eventType = eventType;
    }

    public String getEventData() {
      return eventData;
    }

    public void setEventData(String eventData) {
      this.eventData = eventData;
    }
  }
}