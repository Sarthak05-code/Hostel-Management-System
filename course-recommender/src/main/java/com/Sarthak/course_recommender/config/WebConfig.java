package com.Sarthak.course_recommender.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.DayOfWeek;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(
            String.class,
            DayOfWeek.class,
            s -> DayOfWeek.valueOf(s.toUpperCase())
        );
    }
}