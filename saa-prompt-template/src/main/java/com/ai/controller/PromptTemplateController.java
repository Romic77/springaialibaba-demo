package com.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class PromptTemplateController {
    @Autowired
    private ChatClient chatclient;

    @Value("classpath:/prompttemplate/template.txt")
    private Resource userTemplate;

    /**
     * http://192.168.5.240:8001/prompt/template/chat?name=%E5%BC%A0%E4%B8%89&output_format=html&count=100
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

    @GetMapping("/prompt/template/chat2")
    public Flux<String> chat2(@RequestParam("name") String name, @RequestParam("output_format") String output_format, @RequestParam("count") int count) {
        PromptTemplate promptTemplate = new PromptTemplate(
               userTemplate
        );
        Prompt prompt = promptTemplate.create(Map.of(
                "name", name,
                "output_format", output_format,
                "count", count
        ));

        return chatclient.prompt(prompt).stream().content();
    }

    /**
     * 多角色设定
     * @param systemTopic
     * @param userTopic
     * @return
     */
    @GetMapping("/prompt/template/chat3")
    public Flux<String> chat3(@RequestParam("systemTopic") String systemTopic, @RequestParam("userTopic") String userTopic) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
                "你是一个 {systemTopic} 的助手,只回答{systemTopic}相关问题"
        );
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("systemTopic", systemTopic));

        PromptTemplate promptTemplate = new PromptTemplate(
                "解释一下{userTopic}"
        );

        Message userMessage = promptTemplate.createMessage(Map.of(
                "userTopic", userTopic
        ));

        Prompt prompt1 = new Prompt(List.of(systemMessage, userMessage));
        return chatclient.prompt(prompt1).stream().content();
    }
}
