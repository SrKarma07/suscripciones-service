package com.example.suscripciones.repository;

import com.example.suscripciones.entity.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, String> {

    /**
     * Encuentra todas las suscripciones por estado (por ejemplo: "ACTIVO", "INACTIVO").
     */
    List<Suscripcion> findAllByEstado(String estado);

    /**
     * Encuentra todas las suscripciones por tipo (ej. "BASICA", "PREMIUM", etc.).
     */
    List<Suscripcion> findAllByTipoSuscripcion(String tipoSuscripcion);

    /**
     * Encuentra todas las suscripciones cuya fecha de caducidad sea anterior o igual
     * a la proporcionada (ej. para verificar suscripciones vencidas).
     */
    List<Suscripcion> findAllByFechaCaducidadBefore(LocalDate fecha);

    /**
     * Encuentra todas las suscripciones cuya fecha de caducidad esté entre
     * un rango (startDate y endDate), si lo requieres.
     */
    List<Suscripcion> findAllByFechaCaducidadBetween(LocalDate startDate, LocalDate endDate);

    // Naturalmente, también tienes todos los métodos CRUD de JpaRepository:
    //  - findById(String numeroSuscripcion)
    //  - findAll()
    //  - save(Suscripcion s)
    //  - deleteById(String numeroSuscripcion)
    //  etc.

}
