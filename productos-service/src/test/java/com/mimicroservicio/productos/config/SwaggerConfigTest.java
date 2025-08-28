package com.mimicroservicio.productos.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

class SwaggerConfigTest {

	private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void testCustomOpenAPI_BeanNotNull() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
    }

    @Test
    void testCustomOpenAPI_InfoProperties() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertThat(info.getTitle()).isEqualTo("API Productos");
        assertThat(info.getVersion()).isEqualTo("1.0");
        assertThat(info.getDescription()).isEqualTo("Documentación de API Productos con Swagger");
    }
	
}
