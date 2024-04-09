package com.mca.domain.model.dto;

public record GameDto(

        String id,
        String title,
        Double price,
        boolean availability

) { }