package com.mimicroservicio.productos.productos_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mimicroservicio.productos.productos_service.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}