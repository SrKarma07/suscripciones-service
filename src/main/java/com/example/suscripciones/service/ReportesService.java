package com.example.suscripciones.service;

import com.example.suscripciones.entity.ReporteResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportesService {

    // Método que genera un reporte con suscripciones y las asesorías en el rango de fechas para un cliente
    List<ReporteResponse> generarReportePorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin, String clienteId);
}
