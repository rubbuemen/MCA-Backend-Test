package com.mca.domain.repository;

import com.mca.domain.model.entity.Videogame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VideogameRepository extends JpaRepository<Videogame, Long> {

    @Query("SELECT v FROM Videogame v LEFT JOIN FETCH v.stock s LEFT JOIN FETCH v.promotions p WHERE v.id IN (:ids) AND (p.validFrom = (SELECT MAX(p.validFrom) FROM Promotion p WHERE p.videogame.id = v.id AND p.validFrom <= :dateLimit) OR v.promotions IS EMPTY)")
    List<Videogame> findByIdsWithStockAndPromotionsUntilDate(@Param("ids") List<String> ids, @Param("dateLimit") LocalDateTime dateLimit);

}
