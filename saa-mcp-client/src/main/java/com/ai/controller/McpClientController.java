package com.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class McpClientController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/mcp/chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "深圳") String msg) {
        return chatClient.prompt(msg).stream().content();
    }
}
