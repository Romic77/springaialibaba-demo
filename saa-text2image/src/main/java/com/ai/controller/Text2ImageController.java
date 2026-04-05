package com.ai.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Text2ImageController {
    public static final String IMAGE_MODEL = "wanx2.1-t2i-turbo";
    @Resource
    private ImageModel imageModel;

    @GetMapping("/text2image")
    public String text2image(@RequestParam("prompt") String prompt) {
        return imageModel.call(new ImagePrompt(prompt)).getResult().getOutput().getUrl();
    }
}
