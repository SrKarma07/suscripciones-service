package com.example.suscripciones.repository;

import com.example.suscripciones.entity.Asesoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsesoriaRepository extends JpaRepository<Asesoria, Long> {

    /**
     * Encuentra todas las asesorías que tengan el estado proporcionado.
     * Por ejemplo: "ACTIVO", "PENDIENTE", "CANCELADO", etc.
     */
    List<Asesoria> findAllByEstado(String estado);

    /**
     * Encuentra todas las asesorías de un tipo específico (ej. "Financiera", "Legal", etc.).
     */
    List<Asesoria> findAllByTipoAsesoria(String tipoAsesoria);

    /**
     * Encuentra todas las asesorías cuya fecha esté dentro del rango especificado (inclusivo).
     * Aprovecha la anotación @Query para hacer la consulta manual, aunque también
     * podrías usar métodos de Query derivada con Between si tu campo se llama 'fecha'.
     */
    @Query("SELECT a FROM Asesoria a WHERE a.fecha BETWEEN :startDate AND :endDate")
    List<Asesoria> findAllBetweenDates(LocalDate startDate, LocalDate endDate);

    /**
     * Encuentra todas las asesorías que pertenezcan a una suscripción específica,
     * usando el número de suscripción como criterio.
     *
     * Nota: El nombre de la propiedad (Suscripcion suscripcion) y su PK (numeroSuscripcion)
     * se combina en la sintaxis: [propiedad]_[campo de la PK].
     */
    List<Asesoria> findAllBySuscripcion_NumeroSuscripcion(String numeroSuscripcion);

    /**
     * Permite borrar todas las asesorías asociadas a una suscripción dada, si fuera necesario.
     * Requiere @Modifying y @Transactional para ejecutar un DELETE.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Asesoria a WHERE a.suscripcion.numeroSuscripcion = :numeroSuscripcion")
    void deleteBySuscripcion(String numeroSuscripcion);

    List<Asesoria> findAllByFechaBetweenAndSuscripcion_NumeroSuscripcion(LocalDate fechaInicio, LocalDate fechaFin, String numeroSuscripcion);

    // Busca todas las asesorías dentro de un rango de fechas y de una suscripción específica

}
