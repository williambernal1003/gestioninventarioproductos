package com.mimicroservicio.productos.productos_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mimicroservicio.productos.productos_service.entity.Producto;
import com.mimicroservicio.productos.productos_service.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {

	private final ProductoService productoService;

	
	@Operation(summary = "Crear un nuevo producto", description = "Crea un producto con la información proporcionada")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crearProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuestaJsonApi(nuevoProducto));
    }

    
	@Operation(summary = "Obtener producto por ID", description = "Retorna un producto según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
	@Operation(summary = "Listar todos los productos", description = "Retorna una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(respuestaJsonApi(productos));
    }
    
    
	@Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarProducto(@PathVariable Long id,
                                                                   @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(respuestaJsonApi(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaJsonApi(e.getMessage()));
        }
    }

    
	@Operation(summary = "Eliminar un producto", description = "Elimina un producto existente según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(respuestaJsonApi("Producto eliminado con ID: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaJsonApi(e.getMessage()));
        }
    }

    
    private Map<String, Object> respuestaJsonApi(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        return response;
    }
	
}