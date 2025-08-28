package com.inventario_service.service;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventario_service.client.ProductoClient;
import com.inventario_service.event.CompraRealizadaEvent;
import com.inventario_service.exception.InventarioInsuficienteException;
import com.inventario_service.exception.ProductoNotFoundException;
import com.inventario_service.model.Inventario;
import com.inventario_service.model.InventarioDTO;
import com.inventario_service.model.ProductoDTO;
import com.inventario_service.repository.InventarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {
	
	private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;
    private final ApplicationEventPublisher eventPublisher;

    public InventarioDTO consultarInventario(Long productoId) {
    	ResponseEntity<ProductoDTO> response = productoClient.obtenerProducto(productoId);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ProductoNotFoundException("Producto con ID " + productoId + " no existe");
        }

        Inventario inventario = inventarioRepository.findByProducto_Id(productoId)
                .orElseThrow(() -> new ProductoNotFoundException("Inventario no encontrado para producto " + productoId));

        return InventarioDTO.builder()
                .id(inventario.getId())
                .producto(response.getBody())
                .cantidad(inventario.getCantidad())
                .build();
    }

    public Inventario actualizarInventario(Long productoId, int cantidadVendida) {
    	
    	Optional<Inventario> inventarioOpt = inventarioRepository.findByProducto_Id(productoId);

        Inventario inventario = inventarioOpt.orElseThrow(
            () -> new RuntimeException("Inventario no encontrado para el productoId: " + productoId)
        );

        if (cantidadVendida > inventario.getCantidad()) {
            log.info("Advertencia: intento de venta de {} unidades supera la cantidad disponible de {} para el productoId={}",
                    cantidadVendida, inventario.getCantidad(), productoId);
            throw new InventarioInsuficienteException(
                    "Cantidad vendida (" + cantidadVendida + ") supera la cantidad disponible (" + inventario.getCantidad() + ")"
            );
        }

        inventario.setCantidad(inventario.getCantidad() - cantidadVendida);
        Inventario actualizado = inventarioRepository.save(inventario);

        log.info("Inventario actualizado: productoId={}, nueva cantidad={}", 
                productoId, actualizado.getCantidad());

        eventPublisher.publishEvent(new CompraRealizadaEvent(this, actualizado, cantidadVendida));

        return actualizado;
    }
}
