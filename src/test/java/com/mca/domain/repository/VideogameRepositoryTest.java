package com.mca.domain.repository;

import com.mca.domain.model.entity.Videogame;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class VideogameRepositoryTest {

    @Autowired
    private VideogameRepository videogameRepository;

    @Test
    void testFindByIdsWithStockAndPromotionsUntilDate() {
        var dateLimit = LocalDateTime.parse("2023-11-24T12:40:01.773Z", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        var res = videogameRepository.findByIdsWithStockAndPromotionsUntilDate(List.of("2", "3", "4"), dateLimit);
        var expectedTitles = List.of("Nebulas Descent: Awakening", "Whispers of the Void: Echoes", "Titans of the Ancients: Reawakened");
        var gameId2 = res.stream().filter(v -> v.getId() == 2).findFirst();
        var gameId3 = res.stream().filter(v -> v.getId() == 3).findFirst();
        var gameId4 = res.stream().filter(v -> v.getId() == 4).findFirst();
        var resTitles = res.stream().map(Videogame::getTitle).toList();
        assertEquals(3, res.size(), "Expected 3 records");
        assertTrue(gameId2.isPresent() && gameId3.isPresent() && gameId4.isPresent(), "Records with ID 2, 3, 4 exist");
        assertThat("Expected same titles in any order", resTitles, containsInAnyOrder(expectedTitles.toArray()));
    }

}