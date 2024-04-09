package com.mca.infrastructure.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.Mockito.*;

@SpringBootTest
class KafkaMessageProducerTest {

    @Autowired
    private KafkaMessageProducer<String> kafkaMessageProducer;

    @Value("${events.resource}")
    private String resourceRoute;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testSendMessage() throws IOException {
        var lines = Files.readAllLines(ResourceUtils.getFile(resourceRoute).toPath()).stream().toList();
        kafkaMessageProducer.sendMessage("test");
        verify(kafkaTemplate, times(lines.size() + 1)).send(anyString(), anyString());
    }

}