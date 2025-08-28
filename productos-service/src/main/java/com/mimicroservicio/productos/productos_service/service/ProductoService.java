package com.mimicroservicio.productos.productos_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mimicroservicio.productos.productos_service.entity.Inventario;
import com.mimicroservicio.productos.productos_service.entity.Producto;
import com.mimicroservicio.productos.productos_service.repository.InventarioRepository;
import com.mimicroservicio.productos.productos_service.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

	private final ProductoRepository productoRepository;
	private final InventarioRepository inventarioRepository;

	
    public Producto crearProducto(Producto producto) {
        Producto productoGuardado = productoRepository.save(producto);
        Inventario inventario = Inventario.builder()
                .producto(productoGuardado)
                .cantidad(100)
                .build();
        inventarioRepository.save(inventario);

        return productoGuardado;
    }

    
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setPrecio(productoActualizado.getPrecio());
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
    
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
	
}