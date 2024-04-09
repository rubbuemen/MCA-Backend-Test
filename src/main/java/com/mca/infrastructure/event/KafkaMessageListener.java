package com.mca.infrastructure.event;

import com.google.gson.Gson;
import com.mca.domain.model.entity.Stock;
import com.mca.domain.repository.StockRepository;
import com.mca.domain.repository.VideogameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@Slf4j
@Component
public class KafkaMessageListener  {

    private final Gson gson;
    private final LocalDateTime dateLimit;

    private final VideogameRepository videogameRepository;
    private final StockRepository stockRepository;

    private final CacheManager cacheManager;

    public KafkaMessageListener(@Value("${date}") String date, VideogameRepository videogameRepository, StockRepository stockRepository, CacheManager cacheManager) {
        this.dateLimit = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        this.gson = new Gson();
        this.videogameRepository = videogameRepository;
        this.stockRepository = stockRepository;
        this.cacheManager = cacheManager;
    }

    @KafkaListener(topics = "${topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void listen(String message, Acknowledgment acknowledgment) {
        try {
            log.info("Listening message: {}", message);
            var videogameEvent = gson.fromJson(message, VideoGameEvent.class);
            var id = videogameEvent.getStockId();
            var timeUpdate = videogameEvent.getTimeUpdate().toLocalDateTime();

            // Stock ID maps same than videogame ID
            if (!timeUpdate.isAfter(dateLimit) && videogameRepository.findById(id).isPresent()) {
                if (!stockRepository.existsById(id)) {
                    var stock = new Stock();
                    stock.setId(id); // Necessary to maps correctly
                    stock.setVideogame(videogameRepository.findById(id).get());
                    saveStock(stock, videogameEvent);
                } else {
                    var updateableStock = stockRepository.findByIdAndLastUpdatedBefore(id, timeUpdate);
                    updateableStock.ifPresent(s -> saveStock(s, videogameEvent));
                }
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Exception occurred during listening message {}: {}", message, e.getMessage());
        }
    }

    private void saveStock(Stock stock, VideoGameEvent videogameEvent) {
        stock.setLastUpdated(videogameEvent.getTimeUpdate().toLocalDateTime());
        stock.setAvailability(videogameEvent.isAvailability());
        stockRepository.saveAndFlush(stock);
        log.info("Stock updated: {}", videogameEvent);
        Objects.requireNonNull(cacheManager.getCache("videogames")).clear();
    }

}
