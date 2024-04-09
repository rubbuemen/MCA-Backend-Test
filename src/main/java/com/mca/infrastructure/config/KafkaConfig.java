package com.mca.infrastructure.config;

import com.google.gson.Gson;
import com.mca.infrastructure.event.KafkaMessageProducer;
import com.mca.infrastructure.event.VideoGameEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@EnableKafka
@Configuration
@EnableScheduling
public class KafkaConfig {

    @Value("${events.resource}")
    private String resourceRoute;

    private final KafkaMessageProducer<String> kafkaMessageProducer;

    private final Gson gson;

    public KafkaConfig(KafkaMessageProducer<String> kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
        this.gson = new Gson();
    }

    @Scheduled(fixedRateString = "${kafka.producer.fixed.rate}")
    public void run() {
        List<VideoGameEvent> stocks;
        try {
            stocks = Files.readAllLines(ResourceUtils.getFile(resourceRoute).toPath()).stream()
                    .map(line -> convertStock(Arrays.asList(line.trim().split(",")))).toList();
            stocks.forEach(stock -> kafkaMessageProducer.sendMessage(gson.toJson(stock)));
        } catch (IOException e) {
            log.error("Exception occurred while reading stock events: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception occurred during topic send to Kafka: {}", e.getMessage());
        }

    }

    private VideoGameEvent convertStock(List<String> stock) {
        return VideoGameEvent.builder()
                .stockId(Long.parseLong(stock.get(0)))
                .availability(Boolean.parseBoolean(stock.get(1)))
                .timeUpdate(Timestamp.valueOf(LocalDateTime.parse(stock.get(2),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")))).build();
    }
}
