package com.inventario_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario_service.model.Inventario;
import com.inventario_service.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/inventarios")
@Tag(name = "Inventarios", description = "Operaciones relacionadas con el inventario")
public class InventarioController {

	private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(summary = "Obtener inventario por producto", description = "Retorna la información de inventario de un producto por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{productoId}")
    public ResponseEntity<Object> obtenerInventario(@PathVariable Long productoId) {
    	return ResponseEntity.ok(inventarioService.consultarInventario(productoId));
    }

    @Operation(summary = "Actualizar inventario por compra", description = "Resta la cantidad comprada del inventario de un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado para el producto")
    })
    @PutMapping("/{productoId}/comprar")
    public ResponseEntity<Object> actualizarInventario(
            @PathVariable Long productoId, 
            @RequestParam int cantidad) {

        Inventario inventario = inventarioService.actualizarInventario(productoId, cantidad);
        if (inventario == null) {
            return ResponseEntity.status(404).body("Inventario no encontrado para productoId: " + productoId);
        }

        Map<String, Object> response = Map.of(
            "productoId", inventario.getProducto().getId(),
            "nombreProducto", inventario.getProducto().getNombre(),
            "cantidad", inventario.getCantidad()
        );

        return ResponseEntity.ok(response);
    }
	
}