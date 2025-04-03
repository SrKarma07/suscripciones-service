package com.example.suscripciones.service.impl;

import com.example.suscripciones.entity.Asesoria;
import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.repository.AsesoriaRepository;
import com.example.suscripciones.repository.SuscripcionRepository;
import com.example.suscripciones.service.ReportesService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportesServiceImpl implements ReportesService {

    private final SuscripcionRepository suscripcionRepository;
    private final AsesoriaRepository asesoriaRepository;

    public ReportesServiceImpl(SuscripcionRepository suscripcionRepository, AsesoriaRepository asesoriaRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.asesoriaRepository = asesoriaRepository;
    }

    @Override
    public List<Object[]> generarReportePorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin, String clienteId) {
        // Obtener las suscripciones activas del cliente en el rango de fechas
        List<Suscripcion> suscripciones = suscripcionRepository.findAllByClienteIdAndFechaCaducidadBetween(clienteId, fechaInicio, fechaFin);

        // Crear la lista para el reporte
        List<Object[]> reportData = suscripciones.stream().map(suscripcion -> {
            // Obtener las asesorías asociadas con la suscripción actual
            List<Asesoria> asesorias = asesoriaRepository.findAllByFechaBetweenAndSuscripcion_NumeroSuscripcion(
                    fechaInicio, fechaFin, suscripcion.getNumeroSuscripcion());

            // Agregar la suscripción con sus asesorías
            return new Object[] { suscripcion, asesorias };
        }).collect(Collectors.toList());

        return reportData;
    }
}
