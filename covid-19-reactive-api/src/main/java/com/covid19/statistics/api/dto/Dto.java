package com.covid19.statistics.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dto {
    private UUID id;
    private String value;
}
