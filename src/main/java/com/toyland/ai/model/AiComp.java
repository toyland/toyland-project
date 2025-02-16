package com.toyland.ai.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiComp {

    private final String model;

    public AiComp( @Value("${openai.model}")String openaiModel) {
        this.model = openaiModel;
    }

    public String getModel() {
        return model;
    }

}
