-- Initial migration for Outbox Pattern
-- Creates the outbox event table for storing events before publishing to Kafka

CREATE TABLE outbox_event (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    aggregate_type NVARCHAR(255) NOT NULL,
    aggregate_id NVARCHAR(255) NOT NULL,
    event_type NVARCHAR(255) NOT NULL,
    event_data NVARCHAR(MAX) NOT NULL,
    event_timestamp DATETIME2 NOT NULL DEFAULT GETDATE(),
    processed BIT NOT NULL DEFAULT 0,
    processed_at DATETIME2 NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- Index for efficient querying of unprocessed events
CREATE INDEX IX_outbox_event_processed_timestamp 
ON outbox_event (processed, event_timestamp);

-- Index for aggregate lookup
CREATE INDEX IX_outbox_event_aggregate 
ON outbox_event (aggregate_type, aggregate_id);

-- Index for event type queries
CREATE INDEX IX_outbox_event_type 
ON outbox_event (event_type);