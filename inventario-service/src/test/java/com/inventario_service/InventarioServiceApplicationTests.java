package com.inventario_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventarioServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
    void mainMethodRunsSuccessfully() {
		InventarioServiceApplication.main(new String[]{});
    }

}
