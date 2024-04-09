package com.mca.domain.model.dto;

import lombok.Builder;

@Builder
public record ErrorDto(
        String code,
        String description
) {}
