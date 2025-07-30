package com.missionx.questloggers.global.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CharacterStatDetailDto {
    @JsonProperty("stat_name")
    private String statName;
    @JsonProperty("stat_value")
    private String statValue;
}
