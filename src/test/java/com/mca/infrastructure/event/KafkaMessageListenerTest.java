package com.mca.infrastructure.event;

import com.google.gson.Gson;
import com.mca.domain.model.entity.Stock;
import com.mca.domain.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class KafkaMessageListenerTest {

    @Autowired
    private KafkaMessageListener kafkaMessageListener;

    @Autowired
    private Gson gson;

    @MockBean
    private StockRepository stockRepository;

    @Test
    void testlListenStockNoExists() {
        var event = "{\"stockId\": 999, \"availability\": true, \"timeUpdate\": \"2022-04-08T10:00:00.000Z\"}";
        when(stockRepository.findByIdAndLastUpdatedBefore(anyLong(), any(LocalDateTime.class))).thenReturn(Optional.empty());
        Acknowledgment acknowledgment = mock(Acknowledgment.class);

        kafkaMessageListener.listen(event, acknowledgment);

        verify(stockRepository).findByIdAndLastUpdatedBefore(anyLong(), any(LocalDateTime.class));
        verify(stockRepository, never()).saveAndFlush(any(Stock.class));
    }

    @Test
    void testlListenUpdate() {
        var event = "{\"stockId\": 999, \"availability\": true, \"timeUpdate\": \"2022-04-08T10:00:00.000Z\"}";
        Acknowledgment acknowledgment = mock(Acknowledgment.class);

        var stock = new Stock();
        stock.setId(999L);
        stock.setAvailability(false);
        stock.setLastUpdated(LocalDate.of(2022, 1, 1).atStartOfDay());
        when(stockRepository.findByIdAndLastUpdatedBefore(anyLong(), any(LocalDateTime.class))).thenReturn(Optional.of(stock));

        stock.setAvailability(true);
        stock.setLastUpdated(LocalDateTime.of(2022, 4, 8, 10, 0));

        when(stockRepository.saveAndFlush(any(Stock.class))).thenReturn(stock);

        kafkaMessageListener.listen(event, acknowledgment);

        verify(stockRepository).findByIdAndLastUpdatedBefore(anyLong(), any(LocalDateTime.class));
        verify(stockRepository).saveAndFlush(any(Stock.class));
    }
}