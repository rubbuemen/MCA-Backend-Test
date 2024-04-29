package com.mca.infrastructure.config;

import com.google.gson.Gson;
import com.mca.infrastructure.event.KafkaMessageProducer;
import com.mca.infrastructure.event.VideoGameEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.EnableKafka;
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
@Profile("!test")
public class KafkaConfig implements CommandLineRunner {

    @Value("classpath:events.csv")
    Resource resourceFile;

    private final KafkaMessageProducer<String> kafkaMessageProducer;

    private final Gson gson;

    public KafkaConfig(KafkaMessageProducer<String> kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
        this.gson = new Gson();
    }

    @Override
    public void run(String... args) throws Exception {
        List<VideoGameEvent> stocks;
        try {
            stocks = Files.readAllLines(ResourceUtils.getFile(resourceFile.getURL()).toPath()).stream()
                    .map(line -> convertStock(Arrays.asList(line.trim().split(",")))).toList();
            stocks.forEach(stock -> kafkaMessageProducer.sendMessage(String.valueOf(gson.toJson(stock))));
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
