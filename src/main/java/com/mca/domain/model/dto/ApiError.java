package com.mca.domain.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private String message;
    private String description;
    private int code;
    @Builder.Default
    private LocalDateTime moment = LocalDateTime.now();
}