package com.inventario_service.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProductoNotFoundExceptionTest {

	@Test
    void testConstructorWithMessage() {
        String mensaje = "Producto no encontrado con ID 123";
        ProductoNotFoundException exception = new ProductoNotFoundException(mensaje);

        assertNotNull(exception, "La excepción no debería ser nula");
        assertEquals(mensaje, exception.getMessage(), "El mensaje de la excepción debería coincidir con el esperado");
    }

    @Test
    void testInstanceOfRuntimeException() {
        ProductoNotFoundException exception = new ProductoNotFoundException("Error");
        assertTrue(exception instanceof RuntimeException, 
                   "La excepción debería ser una instancia de RuntimeException");
    }

    @Test
    void testSerialVersionUIDExists() throws NoSuchFieldException {
        long expectedSerialVersionUID = 1L;
        long actualSerialVersionUID = 1;
		try {
			actualSerialVersionUID = ProductoNotFoundException.class.getDeclaredField("serialVersionUID")
			        .getLong(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

        assertEquals(expectedSerialVersionUID, actualSerialVersionUID);
    }
	
}