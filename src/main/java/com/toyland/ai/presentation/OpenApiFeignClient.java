package com.toyland.ai.presentation;

import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.global.config.ai.OpenApiFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "openApiClient", url = "https://api.openai.com/v1/chat/completions", configuration = OpenApiFeignConfig.class)
public interface OpenApiFeignClient  {

    @PostMapping
    AiResponseDto getAnswer(@RequestBody AiRequestDto request);

}
