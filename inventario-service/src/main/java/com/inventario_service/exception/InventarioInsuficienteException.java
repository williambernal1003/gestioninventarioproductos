package com.inventario_service.exception;

public class InventarioInsuficienteException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    public InventarioInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
