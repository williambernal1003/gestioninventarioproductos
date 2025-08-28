package com.inventario_service.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductoClientConfig {

    @Bean
    RequestInterceptor requestInterceptor() {
        return requestTemplate -> 
            requestTemplate.header("x-api-key", "MI_API_KEY_SECRETA");
    }
}