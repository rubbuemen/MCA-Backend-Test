package com.mca.domain.service;

import com.mca.domain.model.dto.GameDto;

import java.util.List;

public interface VideogameService {

    List<GameDto> getVideogamesWithStockAndPromotions(List<String> ids);
}
