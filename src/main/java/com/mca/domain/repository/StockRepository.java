package com.mca.domain.repository;

import com.mca.domain.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByIdAndLastUpdatedBefore(Long id, LocalDateTime date);

}
