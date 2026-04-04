package com.ai.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTool {

    @Tool(description = "用于查询城市未来天气信息，必须使用该工具获取真实天气")
    public String weather(String city) {
        //理论上直接http请求api获取天气信息返回给用户即可
        /*
        String apiKey = "你的key";

        String url = "https://restapi.amap.com/v3/weather/weatherInfo"
                + "?key=" + apiKey
                + "&city=" + city
                + "&extensions=all";

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();*/
        System.out.println("🔥 Tool被调用了，city=" + city);
        return city + "未来3天天气：晴，25~30度";
    }
}