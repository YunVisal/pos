package com.sokhamart.template.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InfoController {
    private final String appName;

    private final String appVersion;

    public InfoController(@Value("${spring.application.name}") String appName,
            @Value("${spring.application.version}") String appVersion) {
        this.appName = appName;
        this.appVersion = appVersion;
    }

    @GetMapping("/info")
    InfoResponse getInfo() {
        return new InfoResponse(appName, appVersion);
    }
}
