package com.example.suscripciones.controller;

import com.example.suscripciones.service.ReportesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReportesService reportesService;

    // Constructor que inyecta el servicio de reportes
    public ReporteController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    // Endpoint para generar el reporte
    @GetMapping
    public ResponseEntity<List<Object[]>> generarReporte(@RequestParam("fecha") String rangoFechas,
                                                         @RequestParam("clienteId") String clienteId) {
        // Convertimos el parámetro de fecha en un rango
        String[] fechas = rangoFechas.split(";");
        LocalDate fechaInicio = LocalDate.parse(fechas[0]);
        LocalDate fechaFin = LocalDate.parse(fechas[1]);

        // Llamamos al servicio de reportes
        List<Object[]> reporte = reportesService.generarReportePorRangoFechas(fechaInicio, fechaFin, clienteId);

        return ResponseEntity.ok(reporte);
    }
}
