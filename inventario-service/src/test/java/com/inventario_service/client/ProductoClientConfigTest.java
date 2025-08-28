package com.inventario_service.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import feign.RequestInterceptor;
import feign.RequestTemplate;

class ProductoClientConfigTest {

	 private ProductoClientConfig productoClientConfig;
	    private RequestInterceptor interceptor;

	    @BeforeEach
	    void setUp() {
	        productoClientConfig = new ProductoClientConfig();
	        interceptor = productoClientConfig.requestInterceptor();
	    }

	    @Test
	    void testRequestInterceptorNotNull() {
	        assertThat(interceptor).isNotNull();
	    }

	    @Test
	    void testRequestInterceptorAddsApiKeyHeader() {
	        RequestTemplate requestTemplate = new RequestTemplate();

	        interceptor.apply(requestTemplate);

	        assertThat(requestTemplate.headers())
	                .containsKey("x-api-key")
	                .extracting(headers -> headers.get("x-api-key"))
	                .satisfies(apiKeys -> assertThat(apiKeys).containsExactly("MI_API_KEY_SECRETA"));
	    }
	
}
