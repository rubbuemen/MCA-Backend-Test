package com.mca.infrastructure.event;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mca.domain.model.entity.Stock;
import com.mca.domain.model.entity.Videogame;
import com.mca.domain.repository.StockRepository;
import com.mca.domain.repository.VideogameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class KafkaMessageListenerTest {

    @Autowired
    private KafkaMessageListener kafkaMessageListener;

    @Autowired
    private Gson gson;

    @MockBean
    private VideogameRepository videogameRepository;

    @MockBean
    private StockRepository stockRepository;

    @Test
    void testlListenCreate() {
        var event = "{\"stockId\": 999, \"availability\": true, \"timeUpdate\": \"2022-04-08T10:00:00.000Z\"}";
        when(videogameRepository.findById(999L)).thenReturn(Optional.of(new Videogame()));
        when(stockRepository.existsById(999L)).thenReturn(false);
        when(stockRepository.saveAndFlush(any(Stock.class))).thenReturn(new Stock());

        kafkaMessageListener.listen(event, null);

        verify(videogameRepository, times(2)).findById(999L);
        verify(stockRepository).existsById(999L);
        verify(stockRepository).saveAndFlush(any(Stock.class));
    }

    @Test
    void testlListenUpdate() {
        var event = "{\"stockId\": 999, \"availability\": true, \"timeUpdate\": \"2022-04-08T10:00:00.000Z\"}";
        var stock = new Stock();
        stock.setId(999L);
        stock.setAvailability(false);
        stock.setLastUpdated(LocalDate.of(2020, 1, 1).atStartOfDay());
        when(videogameRepository.findById(999L)).thenReturn(Optional.of(new Videogame()));
        when(stockRepository.existsById(999L)).thenReturn(true);
        when(stockRepository.findByIdAndLastUpdatedBefore(anyLong(), any(LocalDateTime.class))).thenReturn(Optional.of(stock));
        when(stockRepository.saveAndFlush(any(Stock.class))).thenReturn(new Stock());

        kafkaMessageListener.listen(event, null);

        verify(videogameRepository).findById(999L);
        verify(stockRepository).existsById(999L);
        verify(stockRepository).findByIdAndLastUpdatedBefore(anyLong(), any(LocalDateTime.class));
        verify(stockRepository).saveAndFlush(any(Stock.class));
    }

    private VideoGameEvent createVideoGameEvent(String message) throws JsonParseException {
        return gson.fromJson(message, VideoGameEvent.class);
    }

}