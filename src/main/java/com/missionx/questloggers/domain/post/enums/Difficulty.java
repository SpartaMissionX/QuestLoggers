package com.missionx.questloggers.domain.post.enums;

import com.missionx.questloggers.domain.post.exception.PostException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Difficulty {
    EASY(1), NORMAL(2), HARD(3);

    private final Integer code;

    Difficulty(Integer code) {
        this.code = code;
    }

    public static Difficulty fromCode(Integer code) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getCode() == code) {
                return difficulty;
            }
        }
        throw new PostException(HttpStatus.BAD_REQUEST, "입력한 난이도를 찾을 수 없습니다. 1~3 사이 값으로 입력해주세요");
    }
}
