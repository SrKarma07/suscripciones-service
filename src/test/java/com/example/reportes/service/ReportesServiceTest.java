package com.example.reportes.service;

import com.example.suscripciones.entity.Asesoria;
import com.example.suscripciones.entity.ReporteResponse;
import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.repository.AsesoriaRepository;
import com.example.suscripciones.repository.SuscripcionRepository;
import com.example.suscripciones.service.impl.ReportesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportesServiceTest {

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private AsesoriaRepository asesoriaRepository;

    @InjectMocks
    private ReportesServiceImpl reportesService;

    private Suscripcion suscripcion;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Configurar la suscripción de prueba
        suscripcion = new Suscripcion();
        suscripcion.setNumeroSuscripcion("SUS-101");
        suscripcion.setFechaCaducidad(LocalDate.of(2025, 12, 31));
    }

    @Test
    void testGenerarReportePorRangoFechas() {
        // Configurar datos de prueba
        LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
        LocalDate fechaFin = LocalDate.of(2025, 12, 31);

        Asesoria asesoria = new Asesoria();
        asesoria.setFecha(LocalDate.of(2025, 5, 15));
        asesoria.setTipoAsesoria("Legal");
        asesoria.setEstado("ACTIVO");
        asesoria.setCantidad(1);
        asesoria.setSaldoAsesorias(3);

        // Configurar comportamiento de los repositorios
        when(suscripcionRepository.findAllByClienteIdAndFechaCaducidadBetween("cliente-id", fechaInicio, fechaFin))
                .thenReturn(List.of(suscripcion));

        when(asesoriaRepository.findAllByFechaBetweenAndSuscripcion_NumeroSuscripcion(fechaInicio, fechaFin, "SUS-101"))
                .thenReturn(List.of(asesoria));

        // Llamar al método del servicio
        List<ReporteResponse> reporte = reportesService.generarReportePorRangoFechas(fechaInicio, fechaFin, "cliente-id");

        // Verificar que se generó correctamente el reporte
        assertNotNull(reporte);
        assertEquals(1, reporte.size());
        assertEquals("SUS-101", reporte.get(0).getSuscripcion().getNumeroSuscripcion());
        assertEquals(1, reporte.get(0).getAsesorias().size());
    }

    @Test
    void testGenerarReporteSinAsesorias() {
        // Configurar datos de prueba sin asesorías
        LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
        LocalDate fechaFin = LocalDate.of(2025, 12, 31);

        // Configurar comportamiento de los repositorios
        when(suscripcionRepository.findAllByClienteIdAndFechaCaducidadBetween("cliente-id", fechaInicio, fechaFin))
                .thenReturn(List.of(suscripcion));

        when(asesoriaRepository.findAllByFechaBetweenAndSuscripcion_NumeroSuscripcion(fechaInicio, fechaFin, "SUS-101"))
                .thenReturn(List.of());

        // Llamar al método del servicio
        List<ReporteResponse> reporte = reportesService.generarReportePorRangoFechas(fechaInicio, fechaFin, "cliente-id");

        // Verificar que no haya asesorías en el reporte
        assertNotNull(reporte);
        assertEquals(1, reporte.size());
        assertEquals("SUS-101", reporte.get(0).getSuscripcion().getNumeroSuscripcion());
        assertTrue(reporte.get(0).getAsesorias().isEmpty());
    }

    @Test
    void testGenerarReporteSinSuscripciones() {
        // Configurar datos de prueba sin suscripciones
        LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
        LocalDate fechaFin = LocalDate.of(2025, 12, 31);

        // Configurar comportamiento de los repositorios
        when(suscripcionRepository.findAllByClienteIdAndFechaCaducidadBetween("cliente-id", fechaInicio, fechaFin))
                .thenReturn(List.of());

        // Llamar al método del servicio
        List<ReporteResponse> reporte = reportesService.generarReportePorRangoFechas(fechaInicio, fechaFin, "cliente-id");

        // Verificar que el reporte esté vacío
        assertNotNull(reporte);
        assertTrue(reporte.isEmpty());
    }
}
