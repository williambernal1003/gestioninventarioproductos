package com.mimicroservicio.productos.productos_service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimicroservicio.productos.productos_service.entity.Producto;
import com.mimicroservicio.productos.productos_service.service.ProductoService;

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearProducto() throws Exception {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Laptop")
                .precio(1500.0)
                .build();

        Mockito.when(productoService.crearProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.nombre", is("Laptop")))
                .andExpect(jsonPath("$.data.precio", is(1500.0)));
    }

    @Test
    void testObtenerProducto_Existe() throws Exception {
        Producto producto = Producto.builder().id(1L).nombre("Tablet").precio(500.0).build();
        Mockito.when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Tablet")))
                .andExpect(jsonPath("$.precio", is(500.0)));
    }

    @Test
    void testObtenerProducto_NoExiste() throws Exception {
        Mockito.when(productoService.obtenerProductoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarProductos() throws Exception {
        Producto p1 = Producto.builder().id(1L).nombre("Laptop").precio(1500.0).build();
        Producto p2 = Producto.builder().id(2L).nombre("Mouse").precio(50.0).build();

        Mockito.when(productoService.obtenerTodosLosProductos()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nombre", is("Laptop")))
                .andExpect(jsonPath("$.data[1].nombre", is("Mouse")));
    }

    @Test
    void testActualizarProducto_Existe() throws Exception {
        Producto actualizado = Producto.builder().id(1L).nombre("Laptop Gamer").precio(2000.0).build();
        Mockito.when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre", is("Laptop Gamer")))
                .andExpect(jsonPath("$.data.precio", is(2000.0)));
    }

    @Test
    void testActualizarProducto_NoExiste() throws Exception {
        Producto producto = Producto.builder().id(99L).nombre("Tablet").precio(300.0).build();
        Mockito.when(productoService.actualizarProducto(eq(99L), any(Producto.class)))
                .thenThrow(new RuntimeException("Producto no encontrado con ID: 99"));

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data", is("Producto no encontrado con ID: 99")));
    }

    @Test
    void testEliminarProducto_Existe() throws Exception {
        Mockito.doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is("Producto eliminado con ID: 1")));
    }

    @Test
    void testEliminarProducto_NoExiste() throws Exception {
        Mockito.doThrow(new RuntimeException("Producto no encontrado con ID: 2"))
                .when(productoService).eliminarProducto(2L);

        mockMvc.perform(delete("/api/productos/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data", is("Producto no encontrado con ID: 2")));
    }
	
}