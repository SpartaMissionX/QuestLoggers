package com.missionx.questloggers.global.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDto {

    @JsonProperty("ocid")
    private String ocid;

    @JsonProperty("character_name")
    private String characterName;

    @JsonProperty("world_name")
    private String worldName;

    @JsonProperty("character_class")
    private String characterClass;

    @JsonProperty("character_level")
    private int characterLevel;
}
