package com.missionx.questloggers.global.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CharacterStatDto {
    @JsonProperty("date")
    private String date;
    @JsonProperty("character_class")
    private String characterClass;
    @JsonProperty("final_stat")
    private List<CharacterStatDetailDto> finalStat;
}
