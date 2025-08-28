package com.inventario_service.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import com.inventario_service.model.Inventario;
import com.inventario_service.model.InventarioDTO;
import com.inventario_service.model.Producto;
import com.inventario_service.model.ProductoDTO;
import com.inventario_service.service.InventarioService;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@MockitoBean
    private InventarioService inventarioService;

    @Test
    void testObtenerInventario_Retorna200() throws Exception {      
        ProductoDTO productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Camiseta")
                .precio(150.0)
                .build();
        InventarioDTO inventarioDTO = InventarioDTO.builder()
                .id(1L)
                .producto(productoDTO)
                .cantidad(10)
                .build();
        
        Mockito.when(inventarioService.consultarInventario(1L)).thenReturn(inventarioDTO);
        
        mockMvc.perform(get("/api/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.producto.id").value(1L))
                .andExpect(jsonPath("$.producto.nombre").value("Camiseta"))
                .andExpect(jsonPath("$.cantidad").value(10));
    }

    @Test
    void testActualizarInventario_Retorna200() throws Exception {
        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Mouse");

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(5);

        Mockito.when(inventarioService.actualizarInventario(anyLong(), anyInt())).thenReturn(inventario);

        mockMvc.perform(put("/api/inventarios/2/comprar")
                        .param("cantidad", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(2L))
                .andExpect(jsonPath("$.nombreProducto").value("Mouse"))
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void testActualizarInventario_InventarioNoEncontrado_Retorna404() throws Exception {
        Mockito.when(inventarioService.actualizarInventario(anyLong(), anyInt())).thenReturn(null);

        mockMvc.perform(put("/api/inventarios/99/comprar")
                        .param("cantidad", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Inventario no encontrado para productoId: 99"));
    }
	
}