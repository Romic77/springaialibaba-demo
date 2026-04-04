
package com.ai.controller;

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
@RestController
@RequestMapping("/ollama")
public class HelloworldController {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    @Autowired
    private OllamaChatModel ollamaChatModel;

    /**
     * ChatClient 简单调用
     */
    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {

        return ollamaChatModel.call(new Prompt(query))
                .getResult()
                .getOutput().toString();
    }

    /**
     * ChatClient 流式调用
     */
    @GetMapping(value = "/stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你") String query) {

        Prompt prompt = new Prompt(query);

        return ollamaChatModel
                .stream(prompt)
                .mapNotNull(resp -> resp.getResult().getOutput().getText())
                .filter(content -> content != null && !content.isEmpty())
                .bufferTimeout(10, Duration.ofMillis(100)) // 👈 聚合
                .map(list -> String.join("", list))
                .map(content -> content);
    }

}