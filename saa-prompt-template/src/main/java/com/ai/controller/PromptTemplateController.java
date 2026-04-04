package com.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
public class PromptTemplateController {
    @Autowired
    private ChatClient chatclient;

    /**
     * http://192.168.5.240:8001/prompt/template/chat?name=%E5%BC%A0%E4%B8%89&output_format=html&count=100
     *
     */
    @GetMapping("/prompt/template/chat")
    public Flux<String> chat(@RequestParam("name") String name, @RequestParam("output_format") String output_format, @RequestParam("count") int count) {
        PromptTemplate promptTemplate = new PromptTemplate(
                "讲一个关于{name}的故事,并且以{output_format}的格式输出,字数在{count}"
        );
        Prompt prompt = promptTemplate.create(Map.of(
                "name", name,
                "output_format", output_format,
                "count", count
        ));

        return chatclient.prompt(prompt).stream().content();
    }
}
