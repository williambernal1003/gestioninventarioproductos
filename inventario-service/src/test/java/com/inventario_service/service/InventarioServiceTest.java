package com.inventario_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.inventario_service.client.ProductoClient;
import com.inventario_service.event.CompraRealizadaEvent;
import com.inventario_service.exception.InventarioInsuficienteException;
import com.inventario_service.exception.ProductoNotFoundException;
import com.inventario_service.model.Inventario;
import com.inventario_service.model.InventarioDTO;
import com.inventario_service.model.ProductoDTO;
import com.inventario_service.repository.InventarioRepository;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class InventarioServiceTest {

	@Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoClient productoClient;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InventarioService inventarioService;

    private ProductoDTO productoDTO;
    private Inventario inventario;

    @BeforeEach
    void setUp() {
        productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Laptop")
                .precio(1000.0)
                .build();

        inventario = new Inventario();
        inventario.setId(10L);
        inventario.setCantidad(20);
    }

    // ---------- consultarInventario ----------

    @Test
    void consultarInventario_ProductoEInventarioExistentes() {
        when(productoClient.obtenerProducto(1L))
                .thenReturn(ResponseEntity.ok(productoDTO));
        when(inventarioRepository.findByProducto_Id(1L))
                .thenReturn(Optional.of(inventario));

        InventarioDTO result = inventarioService.consultarInventario(1L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Laptop", result.getProducto().getNombre());
        assertEquals(20, result.getCantidad());
    }

    @Test
    void consultarInventario_ProductoNoEncontrado_ThrowsException() {
        when(productoClient.obtenerProducto(1L))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(ProductoNotFoundException.class,
                () -> inventarioService.consultarInventario(1L));
    }

    @Test
    void consultarInventario_InventarioNoEncontrado_ThrowsException() {
        when(productoClient.obtenerProducto(1L))
                .thenReturn(ResponseEntity.ok(productoDTO));
        when(inventarioRepository.findByProducto_Id(1L))
                .thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class,
                () -> inventarioService.consultarInventario(1L));
    }

    // ---------- actualizarInventario ----------

    @Test
    void actualizarInventario_Exitoso() {
        when(inventarioRepository.findByProducto_Id(1L))
                .thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Inventario actualizado = inventarioService.actualizarInventario(1L, 5);

        assertEquals(15, actualizado.getCantidad());
        verify(inventarioRepository).save(inventario);
        verify(eventPublisher).publishEvent(any(CompraRealizadaEvent.class));
    }

    @Test
    void actualizarInventario_InventarioNoEncontrado_ThrowsException() {
        when(inventarioRepository.findByProducto_Id(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> inventarioService.actualizarInventario(1L, 5));

        verify(inventarioRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void actualizarInventario_CantidadInsuficiente_ThrowsException() {
        when(inventarioRepository.findByProducto_Id(1L))
                .thenReturn(Optional.of(inventario));

        assertThrows(InventarioInsuficienteException.class,
                () -> inventarioService.actualizarInventario(1L, 25));

        verify(inventarioRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
	
}