package com.inventario_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {
	
	private Long id;
	
    private ProductoDTO producto;

    private int cantidad;
}
