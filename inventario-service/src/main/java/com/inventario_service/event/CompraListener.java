package com.inventario_service.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CompraListener {

	@EventListener
    public void onCompraRealizada(CompraRealizadaEvent event) {
        log.info("Se realizó una compra de {} unidades del producto {}",
                event.getCantidadVendida(),
                event.getInventario().getProducto().getId());
}
}