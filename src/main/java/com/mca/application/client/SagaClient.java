package com.mca.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${api.games.saga.name}", url = "${api.games.saga.url}")
public interface SagaClient {

    @GetMapping("/game-saga/{gameId}/related-sagas")
    ResponseEntity<List<String>> getGamesBySagaId(@PathVariable String gameId);

}
