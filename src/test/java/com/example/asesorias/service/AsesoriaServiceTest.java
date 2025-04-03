package com.example.asesorias.service;

import com.example.suscripciones.entity.Asesoria;
import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.repository.AsesoriaRepository;
import com.example.suscripciones.repository.SuscripcionRepository;
import com.example.suscripciones.service.impl.AsesoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsesoriaServiceTest {

    @Mock
    private AsesoriaRepository asesoriaRepository;

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @InjectMocks
    private AsesoriaServiceImpl asesoriaService;

    private Suscripcion suscripcion;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Configuramos una suscripción válida
        suscripcion = new Suscripcion();
        suscripcion.setNumeroSuscripcion("SUS-101");
        suscripcion.setSaldoAsesorias(5);  // saldo suficiente para realizar asesorías
        suscripcion.setFechaCaducidad(LocalDate.of(2025, 12, 31));
    }

    @Test
    void testCreateAsesoria_Success() {
        Asesoria asesoria = new Asesoria();
        asesoria.setFecha(LocalDate.of(2025, 5, 15));
        asesoria.setTipoAsesoria("Legal");
        asesoria.setEstado("ACTIVO");
        asesoria.setCantidad(2);  // Asesoría con 2 unidades

        // Asocia la suscripción con la asesoría
        asesoria.setSuscripcion(suscripcion);

        // Mock suscripción y repositorios
        when(suscripcionRepository.findById("SUS-101")).thenReturn(Optional.of(suscripcion));
        when(asesoriaRepository.save(any(Asesoria.class))).thenReturn(asesoria);
        when(suscripcionRepository.save(any(Suscripcion.class))).thenReturn(suscripcion);

        // Crear asesoría y validar resultados
        Asesoria result = asesoriaService.createAsesoria(asesoria);

        assertNotNull(result);
        assertEquals("Legal", result.getTipoAsesoria());
        assertEquals("ACTIVO", result.getEstado());
        verify(asesoriaRepository, times(1)).save(any(Asesoria.class));
        verify(suscripcionRepository, times(1)).save(any(Suscripcion.class));
    }


    @Test
    void testCreateAsesoria_SuscripcionNoExistente() {
        Asesoria asesoria = new Asesoria();
        asesoria.setFecha(LocalDate.of(2025, 5, 15));
        asesoria.setTipoAsesoria("Legal");
        asesoria.setEstado("ACTIVO");
        asesoria.setCantidad(2);

        // Asocia una suscripción no existente
        asesoria.setSuscripcion(new Suscripcion());
        asesoria.getSuscripcion().setNumeroSuscripcion("SUS-101");

        // Mock suscripción inexistente
        when(suscripcionRepository.findById("SUS-101")).thenReturn(Optional.empty());

        // Lanza la excepción esperada
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> asesoriaService.createAsesoria(asesoria));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No existe la suscripción con ID"));
    }


    @Test
    void testCreateAsesoria_SuscripcionCaducada() {
        // Configuramos la suscripción con fecha de caducidad pasada
        suscripcion.setFechaCaducidad(LocalDate.of(2020, 12, 31));

        // Creamos la asesoría
        Asesoria asesoria = new Asesoria();
        asesoria.setFecha(LocalDate.of(2025, 5, 15));
        asesoria.setTipoAsesoria("Legal");
        asesoria.setEstado("ACTIVO");
        asesoria.setCantidad(2);

        // Aseguramos que la asesoría tenga la suscripción correcta (la suscripción caducada)
        asesoria.setSuscripcion(suscripcion);

        // Mock suscripción caducada
        when(suscripcionRepository.findById("SUS-101")).thenReturn(Optional.of(suscripcion));

        // Lanza la excepción esperada
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> asesoriaService.createAsesoria(asesoria));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("La suscripción ha caducado y no puede realizarse la asesoría"));
    }

    @Test
    void testCreateAsesoria_SaldoInsuficiente() {
        // Configuramos la suscripción con saldo insuficiente
        suscripcion.setSaldoAsesorias(0);  // saldo insuficiente para realizar la asesoría
        suscripcion.setFechaCaducidad(LocalDate.of(2025, 12, 31));

        Asesoria asesoria = new Asesoria();
        asesoria.setFecha(LocalDate.of(2025, 5, 15));
        asesoria.setTipoAsesoria("Legal");
        asesoria.setEstado("ACTIVO");
        asesoria.setCantidad(2);

        // Aseguramos que la asesoría tenga la suscripción correcta (la suscripción con saldo insuficiente)
        asesoria.setSuscripcion(suscripcion);

        // Mock suscripción con saldo insuficiente
        when(suscripcionRepository.findById("SUS-101")).thenReturn(Optional.of(suscripcion));

        // Lanza la excepción esperada
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> asesoriaService.createAsesoria(asesoria));
    }

}
