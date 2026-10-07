package com.sokhamart.platform.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.sokhamart.platform.web.error.GlobalExceptionHandler;

@AutoConfiguration
public class PlatformWebAutoConfiguration {
    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
