package com.mca.domain.service.impl;

import com.mca.infrastructure.exception.ServiceException;
import com.mca.domain.mapper.GameMapper;
import com.mca.domain.model.dto.GameDto;
import com.mca.domain.repository.VideogameRepository;
import com.mca.domain.service.VideogameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class VideogameServiceImpl implements VideogameService {

    private final VideogameRepository videogameRepository;

    private final GameMapper gameMapper;

    @Value(value = "${date}")
    private String date;

    public VideogameServiceImpl(VideogameRepository videogameRepository, GameMapper gameMapper) {
        this.videogameRepository = videogameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    @Cacheable("videogames")
    public List<GameDto> getVideogamesWithStockAndPromotions(List<String> ids) {
        try {
            var dateLimit = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
            var games = videogameRepository.findByIdsWithStockAndPromotionsUntilDate(ids, dateLimit);
            games.sort(Comparator.comparingLong(i -> ids.indexOf(i.getId().toString())));
            return games.stream().map(gameMapper::toDomain).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}