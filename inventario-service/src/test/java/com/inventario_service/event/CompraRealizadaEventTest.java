package com.inventario_service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.inventario_service.model.Inventario;

class CompraRealizadaEventTest {

	@Test
    void constructorDebeInicializarCamposCorrectamente() {
        Object source = new Object();
        Inventario inventario = new Inventario();
        int cantidadVendida = 5;
        
        CompraRealizadaEvent event = new CompraRealizadaEvent(source, inventario, cantidadVendida);
        
        assertEquals(source, event.getSource());
        assertEquals(inventario, event.getInventario());
        assertEquals(cantidadVendida, event.getCantidadVendida());
    }

    @Test
    void getInventarioDebeRetornarInventarioCorrecto() {
        Inventario inventario = new Inventario();
        CompraRealizadaEvent event = new CompraRealizadaEvent(new Object(), inventario, 3);

        assertSame(inventario, event.getInventario());
    }

    @Test
    void getCantidadVendidaDebeRetornarValorCorrecto() {
        CompraRealizadaEvent event = new CompraRealizadaEvent(new Object(), new Inventario(), 10);

        assertEquals(10, event.getCantidadVendida());
    }
	
}