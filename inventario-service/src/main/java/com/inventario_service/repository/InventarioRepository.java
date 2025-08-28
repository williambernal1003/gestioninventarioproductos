package com.inventario_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario_service.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
	Optional<Inventario> findByProducto_Id(Long productoId);
}