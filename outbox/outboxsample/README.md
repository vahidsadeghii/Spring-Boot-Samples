# Outbox Sample with Flyway

This Spring Boot application demonstrates the Outbox pattern with Flyway database migrations and Kafka integration.

## Features

- ✅ **Flyway Database Migration** - Automatic database schema management
- ✅ **Outbox Pattern** - Reliable event publishing with transactional guarantees
- ✅ **Kafka Integration** - Event publishing to Apache Kafka
- ✅ **SQL Server Support** - Microsoft SQL Server database
- ✅ **Scheduled Processing** - Automatic outbox event processing
- ✅ **RESTful API** - Endpoints for creating events and monitoring

## Prerequisites

- Java 21
- Microsoft SQL Server (or SQL Server in Docker)
- Apache Kafka (or Kafka in Docker)

## Database Setup

### Option 1: Docker SQL Server
```bash
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YourStrong!Passw0rd" \
  -p 1433:1433 --name sqlserver \
  -d mcr.microsoft.com/mssql/server:2022-latest

# Create database
docker exec -it sqlserver /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U SA -P "YourStrong!Passw0rd" \
  -Q "CREATE DATABASE outboxsample"
```

### Option 2: Local SQL Server
Create a database named `outboxsample` in your local SQL Server instance.

## Kafka Setup

### Docker Kafka
```bash
# Start Kafka with Docker Compose
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181
  
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
      - 9092:9092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

## Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=outboxsample;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourStrong!Passw0rd

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
```

## Running the Application

1. **Build the application:**
   ```bash
   ./gradlew build
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

3. **Flyway will automatically:**
   - Create the `flyway_schema_history` table
   - Execute migration scripts in `src/main/resources/db/migration/`
   - Create the `outbox_event` table

## Flyway Commands

### Manual Flyway Operations
```bash
# Check migration status
./gradlew flywayInfo

# Migrate to latest version
./gradlew flywayMigrate

# Validate migrations
./gradlew flywayValidate

# Clean database (⚠️ Use with caution!)
./gradlew flywayClean
```

## API Usage

### Create an Event
```bash
curl -X POST http://localhost:8080/api/v1/events \
  -H "Content-Type: application/json" \
  -d '{
    "aggregateType": "User",
    "aggregateId": "user-123",
    "eventType": "UserCreated",
    "eventData": "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}"
  }'
```

### Check Unprocessed Events
```bash
curl http://localhost:8080/api/v1/events/unprocessed/count
```

### Health Check
```bash
curl http://localhost:8080/api/v1/health
```

## How It Works

1. **Event Storage**: When you create an event via the API, it's stored in the `outbox_event` table within the same transaction as your business logic.

2. **Event Processing**: A scheduled job (`OutboxService.processOutboxEvents()`) polls for unprocessed events every 5 seconds.

3. **Kafka Publishing**: Unprocessed events are published to Kafka topics formatted as `{aggregateType}.{eventType}` (e.g., `user.usercreated`).

4. **Event Marking**: Successfully published events are marked as processed with a timestamp.

5. **Cleanup**: Processed events older than 7 days are automatically cleaned up daily at 2 AM.

## Database Schema

The Flyway migration creates the following table:

```sql
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
```

## Adding New Migrations

To add new database changes:

1. Create a new migration file in `src/main/resources/db/migration/`
2. Follow the naming convention: `V{version}__{description}.sql`
3. Example: `V2__Add_user_table.sql`

```sql
-- V2__Add_user_table.sql
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
```

## Configuration Properties

Key configuration properties:

```properties
# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# JPA
spring.jpa.hibernate.ddl-auto=validate

# Outbox Processing
outbox.polling-interval=5000
outbox.batch-size=10
outbox.processing.stop-on-first-error=true
```

## Monitoring

- Monitor Flyway migrations: Check the `flyway_schema_history` table
- Monitor outbox events: Query the `outbox_event` table
- Check application logs for processing status
- Use the `/api/v1/events/unprocessed/count` endpoint

## Troubleshooting

### Common Issues

1. **Migration fails**: Check database connection and permissions
2. **Events not processing**: Verify Kafka is running and accessible
3. **Database connection issues**: Check connection string and credentials

### Useful Queries

```sql
-- Check migration history
SELECT * FROM flyway_schema_history ORDER BY installed_on DESC;

-- Check unprocessed events
SELECT COUNT(*) FROM outbox_event WHERE processed = 0;

-- View recent events
SELECT TOP 10 * FROM outbox_event ORDER BY created_at DESC;
```