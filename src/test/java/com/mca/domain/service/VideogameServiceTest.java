package com.mca.domain.service;

import com.mca.domain.mapper.GameMapper;
import com.mca.domain.model.dto.GameDto;
import com.mca.domain.model.entity.Promotion;
import com.mca.domain.model.entity.Stock;
import com.mca.domain.model.entity.Videogame;
import com.mca.domain.repository.VideogameRepository;
import com.mca.infrastructure.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class VideogameServiceTest {

    @Autowired
    private VideogameService videogameService;

    @Autowired
    private GameMapper gameMapper;

    @MockBean
    private VideogameRepository videogameRepository;

    private List<Videogame> games;

    @BeforeEach
    void setUp() {
        var game1 = new Videogame();
        var game2 = new Videogame();
        var promotion1 = new Promotion();
        var promotion2 = new Promotion();
        var stock1 = new Stock();
        var stock2 = new Stock();
        promotion1.setId(1L);
        promotion1.setPrice(BigDecimal.ONE);
        promotion1.setValidFrom(LocalDateTime.now());
        promotion2.setId(2L);
        promotion2.setPrice(BigDecimal.TEN);
        promotion2.setValidFrom(LocalDateTime.now());
        stock1.setId(1L);
        stock1.setAvailability(true);
        stock2.setId(2L);
        stock2.setAvailability(false);
        game1.setId(1L);
        game1.setTitle("test1");
        game1.setPromotions(List.of(promotion1));
        game1.setStock(stock1);
        game2.setId(2L);
        game2.setTitle("test2");
        game2.setPromotions(List.of(promotion2));
        game2.setStock(stock2);
        this.games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
    }

    @Test
    void testSuccessGetVideogamesWithStockAndPromotions() {
        when(videogameRepository.findByIdsWithStockAndPromotionsUntilDate(anyList(), any(LocalDateTime.class))).thenReturn(games);
        var res = videogameService.getVideogamesWithStockAndPromotions(List.of("1", "2"));
        var game1 = new GameDto("1", "test1", 1.0, true);
        var game2 = new GameDto("2", "test2", 10.0, false);
        var expected = List.of(game1, game2);
        assertEquals(expected, res, "Expected videogames must be equals than res");
        verify(videogameRepository).findByIdsWithStockAndPromotionsUntilDate(anyList(), any(LocalDateTime.class));
    }

    @Test
    void testExceptionGetVideogamesWithStockAndPromotions() {
        when(videogameRepository.findByIdsWithStockAndPromotionsUntilDate(anyList(), any(LocalDateTime.class))).thenThrow(ServiceException.class);
        assertThrows(ServiceException.class, () -> videogameService.getVideogamesWithStockAndPromotions(List.of("1", "2")), "ServiceException must be thrown");
    }


}