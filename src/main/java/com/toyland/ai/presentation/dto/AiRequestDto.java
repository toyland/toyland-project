package com.toyland.ai.presentation.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AiRequestDto {
    private final String model;
    private final List<Message> messages;

    public AiRequestDto(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user",prompt));
    }



}
