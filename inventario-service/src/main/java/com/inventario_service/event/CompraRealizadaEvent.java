package com.inventario_service.event;

import org.springframework.context.ApplicationEvent;

import com.inventario_service.model.Inventario;

import lombok.Getter;

@Getter
public class CompraRealizadaEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private final transient Inventario inventario;
    private final int cantidadVendida;

    public CompraRealizadaEvent(Object source, Inventario inventario, int cantidadVendida) {
        super(source);
        this.inventario = inventario;
        this.cantidadVendida = cantidadVendida;
    }
	
}
