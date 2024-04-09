package com.mca.domain.mapper;

import com.mca.domain.model.dto.GameDto;
import com.mca.domain.model.entity.Promotion;
import com.mca.domain.model.entity.Stock;
import com.mca.domain.model.entity.Videogame;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GameMapper {

    @Mapping(target = "price", source = "entity.promotions", qualifiedByName = "price")
    @Mapping(target = "availability", source = "entity.stock", qualifiedByName = "stock")
    GameDto toDomain(Videogame entity);

    @Named("price")
    static Double price(List<Promotion> promotions) {
        return promotions.isEmpty() ? null : promotions.getFirst().getPrice().doubleValue();
    }

    @Named("stock")
    static boolean stock(Stock stock) {
        return stock != null && stock.isAvailability();
    }
}
