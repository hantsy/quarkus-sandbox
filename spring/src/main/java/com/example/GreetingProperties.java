package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("greeting")
public class GreetingProperties {

    private String text = "Default message";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
