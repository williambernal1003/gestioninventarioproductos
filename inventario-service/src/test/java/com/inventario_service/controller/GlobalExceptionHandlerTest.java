package com.inventario_service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import com.inventario_service.exception.InventarioInsuficienteException;

class GlobalExceptionHandlerTest {

	private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleInventarioInsuficienteException_ReturnsBadRequest() {
        String errorMessage = "No hay suficiente stock para el producto";
        InventarioInsuficienteException exception =
                new InventarioInsuficienteException(errorMessage);
        
        ResponseEntity<Map<String, Object>> response =
                globalExceptionHandler.handleInventarioInsuficienteException(exception);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsKeys("timestamp", "status", "error", "message");
        assertThat(body.get("status")).isEqualTo(BAD_REQUEST.value());
        assertThat(body.get("error")).isEqualTo("Cantidad insuficiente en inventario");
        assertThat(body.get("message")).isEqualTo(errorMessage);
    }
}
