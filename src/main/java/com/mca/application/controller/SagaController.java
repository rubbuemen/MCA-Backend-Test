package com.mca.application.controller;

import com.mca.application.client.SagaClient;
import com.mca.domain.model.dto.GameDto;
import com.mca.domain.service.VideogameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class SagaController {

    private final VideogameService videogameService;
    private final SagaClient sagaClient;

    @GetMapping("/game/{gameId}/saga")
    public ResponseEntity<List<GameDto>> getSagaGamesByGameId(@PathVariable String gameId) {
        if (!gameId.matches("^\\d+$")){
            throw new IllegalArgumentException("The path variable is not a valid number ID.");
        }
        log.info("Requesting related video games for ID: {}", gameId);
        var resGamesId = sagaClient.getGamesBySagaId(gameId);
        var games = videogameService.getVideogamesWithStockAndPromotions(resGamesId.getBody());
        return ResponseEntity.ok(games);
    }

}