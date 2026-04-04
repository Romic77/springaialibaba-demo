package com.ai.controller;

import com.ai.model.enitty.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class StructuredOutputController {
    private final ChatClient chatClient;


    @GetMapping("/structured/output/chat")
    public Student chat(@RequestParam("sname") String sname,
                        @RequestParam("email") String email) {
        return chatClient.prompt().user(promptUserSpec -> promptUserSpec.text("学号1001,我叫{sname},大学专业计算机科学与技术，邮箱{email}")
                .param("sname", sname)
                .param("email", email)).call().entity(Student.class);
    }
}
