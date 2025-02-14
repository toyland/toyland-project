package com.toyland.ai.presentation.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AiRequestDto {
    private String model;
    private List<Message> messages;

    public AiRequestDto(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user",prompt));
    }



}
