package com.ai.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherService {
    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherForecast(String city) {
        Map<String, String> weatherData = Map.of(
                "北京", "北京今天天气晴转多云，最低气温8℃，最高气温0℃。",
                "上海", "上海今天天气晴转多云，最低气温9℃，最高气温20℃。",
                "广州", "广州今天天气晴转多云，最低气温10℃，最高气温21℃。",
                "深圳", "深圳今天天气晴转多云，最低气温11℃，最高气温22℃。",
                "杭州", "杭州今天天气晴转多云，最低气温12℃，最高气温23℃。"
        );
        return weatherData.getOrDefault(city, "没有找到该城市的天气信息");
    }
}
