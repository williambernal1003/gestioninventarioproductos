package com.mimicroservicio.productos.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

class SecurityConfigTest {

	private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testWebSecurityCustomizerNotNull() {
        WebSecurityCustomizer customizer = securityConfig.webSecurityCustomizer();
        assert customizer != null;
    }

    @Test
    void testWebSecurityCustomizerConfiguresIgnoredPaths() {
        WebSecurityCustomizer customizer = securityConfig.webSecurityCustomizer();
        WebSecurity mockWebSecurity = mock(WebSecurity.class);
        WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer = mock(WebSecurity.IgnoredRequestConfigurer.class);

        when(mockWebSecurity.ignoring()).thenReturn(ignoredRequestConfigurer);
        when(ignoredRequestConfigurer.requestMatchers(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(ignoredRequestConfigurer);

        customizer.customize(mockWebSecurity);
        
        verify(mockWebSecurity, times(1)).ignoring();
        verify(ignoredRequestConfigurer, times(1))
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**"
                );
    }
	
}
