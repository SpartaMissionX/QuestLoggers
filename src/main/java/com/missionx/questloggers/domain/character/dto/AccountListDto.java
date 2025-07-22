package com.missionx.questloggers.domain.character.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListDto {

    @JsonProperty("account_list")
    private List<AccountInfo> accountList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountInfo { // 내부 클래스로 정의하여 구조를 명확히 함
        @JsonProperty("account_id")
        private String accountId;

        @JsonProperty("character_list")
        private List<CharacterDto> characterList;
    }

}
