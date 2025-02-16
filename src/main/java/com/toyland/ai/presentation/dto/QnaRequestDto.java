package com.toyland.ai.presentation.dto;

import lombok.Getter;

import java.util.UUID;


@Getter
public class QnaRequestDto {

    String question;
    String answer;
    UUID storeId;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
