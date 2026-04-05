package com.ai.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeAudioSpeechApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.tts.Speech;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Text2VoiceController {


    private static final String TEST_TEXT = "那我来给大家推荐一款T恤，这款呢真的是超级好看";

    @Resource
    private TextToSpeechModel textToSpeechModel;


    @GetMapping("/sambert/stream")
    public Flux<byte[]> sambertStream() {
        DashScopeAudioSpeechOptions options = DashScopeAudioSpeechOptions.builder()
                .model(DashScopeModel.AudioModel.SAMBERT_ZHICHU_V1.getValue())
                .responseFormat(DashScopeAudioSpeechApi.ResponseFormat.MP3)
                .sampleRate(16000)
                .volume(50)
                .build();

        TextToSpeechPrompt prompt = new TextToSpeechPrompt(TEST_TEXT, options);

        // 直接返回Flux流，Spring会自动处理流式传输
        return textToSpeechModel.stream(prompt)
                .map(response -> {
                    Speech speech = response.getResult();
                    return speech.getOutput();
                })
                .doOnNext(chunk -> log.debug("Received audio chunk, size: {} bytes", chunk.length))
                .doOnComplete(() -> log.info("Sambert stream completed"))
                .doOnError(error -> log.error("Error in sambert stream: {}", error.getMessage()));
    }

}
