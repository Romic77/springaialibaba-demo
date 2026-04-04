package com.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
public class PromptController {
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private OllamaChatModel chatModel;


    @GetMapping("/prompt/chat")
    public Flux<String> chat(@RequestParam("query") String query) {
        //垂直ai能力边界
        return chatClient.prompt().system("你是一个法律助手，其他问题回复无可奉告。").user(query).stream().content();
    }

    @GetMapping("/prompt/chat2")
    public Flux<String> chat2(@RequestParam("query") String query) {
        SystemMessage systemMessage = new SystemMessage("讲个笑话，100字以内");

        UserMessage userMessage = new UserMessage(query);

        Prompt prompt = new Prompt(userMessage, systemMessage);

        return chatModel.stream(prompt)
                .mapNotNull(resp -> resp.getResult().getOutput().getText());
    }

    @GetMapping("/prompt/chat3")
    public String chat3(@RequestParam("query") String query) {
        return Objects.requireNonNull(chatClient.prompt().user(query).call().chatResponse()).getResult().getOutput().getText();
    }

    @GetMapping("/prompt/chat4")
    public String chat4(@RequestParam("query") String query) {


        return Objects.requireNonNull(chatClient.prompt().system("如果用户问天气，必须调用weather工具").user(query + "未来3天天气如何").call().chatResponse()).getResult().getOutput().getText();
    }
}
