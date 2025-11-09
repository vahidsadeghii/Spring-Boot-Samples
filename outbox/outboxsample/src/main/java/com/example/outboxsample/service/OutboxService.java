package com.example.outboxsample.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outboxsample.entity.OutboxEvent;
import com.example.outboxsample.repository.OutboxEventRepository;

@Service
@Transactional
public class OutboxService {

  private static final Logger logger = LoggerFactory.getLogger(OutboxService.class);

  @Autowired private OutboxEventRepository outboxEventRepository;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Value("${outbox.batch-size:10}")
  private int batchSize;

  @Value("${outbox.processing.stop-on-first-error:true}")
  private boolean stopOnFirstError;

  /**
   * Store an event in the outbox
   */
  public OutboxEvent storeEvent(
      String aggregateType, String aggregateId, String eventType, String eventData) {
    OutboxEvent event = new OutboxEvent(aggregateType, aggregateId, eventType, eventData);
    OutboxEvent savedEvent = outboxEventRepository.save(event);
    logger.info(
        "Stored outbox event: id={}, aggregateType={}, eventType={}",
        savedEvent.getId(),
        aggregateType,
        eventType);
    return savedEvent;
  }

  /**
   * Process unprocessed events and publish to Kafka
   */
  @Scheduled(fixedDelayString = "${outbox.polling-interval:5000}")
  public void processOutboxEvents() {
    logger.debug("Processing outbox events...");

    List<OutboxEvent> unprocessedEvents =
        outboxEventRepository.findUnprocessedEventsWithLimit(Pageable.ofSize(batchSize));

    if (unprocessedEvents.isEmpty()) {
      logger.debug("No unprocessed events found");
      return;
    }

    logger.info("Processing {} unprocessed events", unprocessedEvents.size());

    for (OutboxEvent event : unprocessedEvents) {
      try {
        publishEvent(event);
        markEventAsProcessed(event);
        logger.info(
            "Successfully processed event: id={}, eventType={}", event.getId(), event.getEventType());

      } catch (Exception e) {
        logger.error(
            "Failed to process event: id={}, eventType={}, error={}",
            event.getId(),
            event.getEventType(),
            e.getMessage(),
            e);

        if (stopOnFirstError) {
          logger.error("Stopping processing due to error and stopOnFirstError=true");
          break;
        }
      }
    }
  }

  /**
   * Publish event to Kafka
   */
  private void publishEvent(OutboxEvent event) {
    String topicName = generateTopicName(event.getAggregateType(), event.getEventType());
    String key = event.getAggregateId();

    kafkaTemplate.send(topicName, key, event.getEventData());
    logger.debug("Published event to Kafka: topic={}, key={}", topicName, key);
  }

  /**
   * Mark event as processed
   */
  private void markEventAsProcessed(OutboxEvent event) {
    event.markAsProcessed();
    outboxEventRepository.save(event);
  }

  /**
   * Generate Kafka topic name based on aggregate type and event type
   */
  private String generateTopicName(String aggregateType, String eventType) {
    return String.format("%s.%s", aggregateType.toLowerCase(), eventType.toLowerCase());
  }

  /**
   * Get count of unprocessed events
   */
  @Transactional(readOnly = true)
  public long getUnprocessedEventCount() {
    return outboxEventRepository.countUnprocessedEvents();
  }

  /**
   * Cleanup processed events older than specified days
   */
  @Scheduled(cron = "${outbox.cleanup.cron:0 0 2 * * ?}") // Daily at 2 AM
  public void cleanupProcessedEvents() {
    int cleanupDays = 7; // Keep processed events for 7 days
    LocalDateTime cutoffDate = LocalDateTime.now().minusDays(cleanupDays);

    List<OutboxEvent> oldProcessedEvents =
        outboxEventRepository.findProcessedEventsBefore(cutoffDate);

    if (!oldProcessedEvents.isEmpty()) {
      outboxEventRepository.deleteAll(oldProcessedEvents);
      logger.info("Cleaned up {} processed events older than {} days", oldProcessedEvents.size(), cleanupDays);
    }
  }
}