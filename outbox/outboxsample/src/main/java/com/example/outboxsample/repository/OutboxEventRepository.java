package com.example.outboxsample.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.outboxsample.entity.OutboxEvent;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    /**
     * Find all unprocessed events ordered by event timestamp
     */
    @Query("SELECT e FROM OutboxEvent e WHERE e.processed = false ORDER BY e.eventTimestamp ASC")
    List<OutboxEvent> findUnprocessedEvents();

    /**
     * Find unprocessed events with limit for batch processing
     */
    @Query("SELECT e FROM OutboxEvent e WHERE e.processed = false ORDER BY e.eventTimestamp ASC")
    List<OutboxEvent> findUnprocessedEventsWithLimit(org.springframework.data.domain.Pageable pageable);

    /**
     * Find events by aggregate type and id
     */
    List<OutboxEvent> findByAggregateTypeAndAggregateIdOrderByEventTimestampAsc(
            String aggregateType, String aggregateId);

    /**
     * Find events by event type
     */
    List<OutboxEvent> findByEventTypeOrderByEventTimestampAsc(String eventType);

    /**
     * Find processed events older than specified date for cleanup
     */
    @Query("SELECT e FROM OutboxEvent e WHERE e.processed = true AND e.processedAt < :beforeDate")
    List<OutboxEvent> findProcessedEventsBefore(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * Count unprocessed events
     */
    @Query("SELECT COUNT(e) FROM OutboxEvent e WHERE e.processed = false")
    long countUnprocessedEvents();
}