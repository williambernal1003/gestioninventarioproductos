package com.inventario_service.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.inventario_service.model.Inventario;
import com.inventario_service.model.Producto;

@ExtendWith(OutputCaptureExtension.class)
class CompraListenerTest {
	@Test
    void testOnCompraRealizada_LogInfo(CapturedOutput output) {
        Producto producto = new Producto();
        producto.setId(123L);

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);

        CompraRealizadaEvent event = new CompraRealizadaEvent(this, inventario, 5);

        CompraListener listener = new CompraListener();
        listener.onCompraRealizada(event);
        assertThat(output.getOut())
            .contains("Se realizó una compra de 5 unidades del producto 123");
    }
}