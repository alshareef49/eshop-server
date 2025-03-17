package com.eshop.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthCodeConfigTest {
    private AuthCodeConfig authCodeConfig;

    @BeforeEach
    void setUp() {
        authCodeConfig = new AuthCodeConfig();
    }

    @Test
    void shouldSetBearerToken_WhenTokenHasBearerPrefix() {
        AuthCodeConfig.TOKEN = "Bearer valid_token";
        HttpEntity<Void> entity = authCodeConfig.getHeaderEntity();
        HttpHeaders headers = entity.getHeaders();
        assertEquals("Bearer valid_token", headers.getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void shouldSetBearerToken_WhenTokenDoesNotHaveBearerPrefix() {

        AuthCodeConfig.TOKEN = "valid_token_without_prefix";
        HttpEntity<Void> entity = authCodeConfig.getHeaderEntity();
        HttpHeaders headers = entity.getHeaders();
        assertEquals("Bearer valid_token_without_prefix", headers.getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void shouldHaveEmptyAuthorization_WhenTokenIsEmpty() {
        AuthCodeConfig.TOKEN = "";
        HttpEntity<Void> entity = authCodeConfig.getHeaderEntity();
        HttpHeaders headers = entity.getHeaders();
        assertEquals("Bearer ", headers.getFirst(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void shouldSetContentTypeAndBearerToken_WhenUsingBody() {
        AuthCodeConfig.TOKEN = "Bearer valid_token";
        String requestBody = "{ \"name\": \"test\" }";
        HttpEntity<String> entity = authCodeConfig.getHeaderEntityWithBody(requestBody);
        HttpHeaders headers = entity.getHeaders();
        assertEquals("Bearer valid_token", headers.getFirst(HttpHeaders.AUTHORIZATION));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals(requestBody, entity.getBody());
    }
}