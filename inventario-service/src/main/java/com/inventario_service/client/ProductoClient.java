package com.inventario_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.inventario_service.model.ProductoDTO;

import org.springframework.http.ResponseEntity;

@FeignClient(name = "productos-service", url = "${productos.service.url}", configuration = ProductoClientConfig.class)
public interface ProductoClient {

    @GetMapping("/{id}")
    ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable("id") Long id);
}