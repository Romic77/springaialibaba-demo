
package com.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ollama")
public class HelloworldController {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private final ChatClient chatClient;

    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(defaultValue = "你好") String query) {

        return chatClient
                .prompt()
                .user(query)
                .call()
                .content(); // ✅ 直接拿字符串
    }


    @GetMapping(value = "/stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你") String query) {

        return chatClient
                .prompt()
                .user(query)
                .stream()
                .content() // ✅ 已经是纯文本 token
                .filter(content -> content != null && !content.isEmpty())
                .bufferTimeout(10, Duration.ofMillis(100)) // 👈 控制输出粒度
                .map(list -> String.join("", list))
                .map(content -> "data:" + content + "\n\n"); // SSE
    }

}