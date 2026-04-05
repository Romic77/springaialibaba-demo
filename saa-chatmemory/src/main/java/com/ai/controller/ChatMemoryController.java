package com.ai.controller;

import com.alibaba.cloud.ai.memory.redis.RedissonRedisChatMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RequiredArgsConstructor
@RestController
public class ChatMemoryController {
    private final ChatClient chatClient;

    private final MessageWindowChatMemory messageWindowChatMemory;

    @GetMapping("/chatmemory/chat")
    public String call(@RequestParam(value = "query", defaultValue = "你好，我的外号是影子，请记住呀") String query,
                       @RequestParam(value = "conversation_id", defaultValue = "yingzi") String conversationId) {
        return chatClient.prompt(query)
                .advisors(
                        a -> a.param(CONVERSATION_ID, conversationId)
                )
                .call().content();
    }

    @GetMapping("/message/{conversation_id}")
    public List<Message> messages(@PathVariable(value = "conversation_id") String conversationId) {
        return messageWindowChatMemory.get(conversationId);
    }


}
