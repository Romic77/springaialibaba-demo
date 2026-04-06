package com.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel, ToolCallbackProvider toolCallbackProvider) {
        return ChatClient.builder(ollamaChatModel).defaultToolCallbacks(toolCallbackProvider).build();
    }
}
