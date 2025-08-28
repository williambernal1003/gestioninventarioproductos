package com.mimicroservicio.productos.productos_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mimicroservicio.productos.productos_service.entity.Inventario;
import com.mimicroservicio.productos.productos_service.entity.Producto;
import com.mimicroservicio.productos.productos_service.repository.InventarioRepository;
import com.mimicroservicio.productos.productos_service.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
	@Mock
    private ProductoRepository productoRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Laptop")
                .precio(1500.0)
                .build();
    }

    @Test
    void testCrearProducto_GuardaProductoEInventario() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.crearProducto(producto);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Laptop");
        assertThat(result.getPrecio()).isEqualTo(1500.0);

        verify(productoRepository, times(1)).save(producto);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void testObtenerProductoPorId_Existe() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoService.obtenerProductoPorId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Laptop");
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerProductoPorId_NoExiste() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.obtenerProductoPorId(2L);

        assertThat(result).isEmpty();
        verify(productoRepository, times(1)).findById(2L);
    }

    @Test
    void testObtenerTodosLosProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));

        List<Producto> result = productoService.obtenerTodosLosProductos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Laptop");
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testActualizarProducto_Existe() {
        Producto actualizado = Producto.builder()
                .nombre("Laptop Gamer")
                .precio(2000.0)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto result = productoService.actualizarProducto(1L, actualizado);

        assertThat(result.getNombre()).isEqualTo("Laptop Gamer");
        assertThat(result.getPrecio()).isEqualTo(2000.0);

        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testActualizarProducto_NoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto actualizado = Producto.builder()
                .nombre("Tablet")
                .precio(500.0)
                .build();

        assertThatThrownBy(() -> productoService.actualizarProducto(99L, actualizado))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado con ID: 99");
    }

    @Test
    void testEliminarProducto_Existe() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminarProducto(1L);

        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProducto_NoExiste() {
        when(productoRepository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> productoService.eliminarProducto(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado con ID: 2");

        verify(productoRepository, times(1)).existsById(2L);
        verify(productoRepository, never()).deleteById(2L);
    }
}
