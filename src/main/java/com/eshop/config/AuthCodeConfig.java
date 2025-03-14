package com.eshop.config;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AuthCodeConfig {
    public static String TOKEN = "";

    public HttpEntity<Void> getHeaderEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (TOKEN.startsWith("Bearer ")) {
            headers.setBearerAuth(TOKEN.substring(7));
        } else {
            headers.setBearerAuth(TOKEN);
        }
        return new HttpEntity<>(headers);
    }
    public <T> HttpEntity<T> getHeaderEntityWithBody(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (TOKEN.startsWith("Bearer ")) {
            headers.setBearerAuth(TOKEN.substring(7));
        } else {
            headers.setBearerAuth(TOKEN);
        }
        return new HttpEntity<>(body, headers);
    }
}
