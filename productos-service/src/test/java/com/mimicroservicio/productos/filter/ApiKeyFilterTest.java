package com.mimicroservicio.productos.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class ApiKeyFilterTest {

	private ApiKeyFilter apiKeyFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        apiKeyFilter = new ApiKeyFilter();
        ReflectionTestUtils.setField(apiKeyFilter, "apiKey", "12345");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void whenApiKeyIsValid_thenFilterContinues() throws Exception {
        when(request.getHeader("X-API-KEY")).thenReturn("12345");

        apiKeyFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void whenApiKeyIsInvalid_thenUnauthorizedResponse() throws Exception {
        when(request.getHeader("X-API-KEY")).thenReturn("wrong-key");
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        apiKeyFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer, times(1)).write("API key inválida");
        verify(filterChain, never()).doFilter(request, response);
    }
}