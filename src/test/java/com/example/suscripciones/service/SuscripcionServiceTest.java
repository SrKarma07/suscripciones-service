package com.example.suscripciones.service;

import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.repository.SuscripcionRepository;
import com.example.suscripciones.service.impl.SuscripcionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SuscripcionServiceTest {

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SuscripcionServiceImpl suscripcionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSuscripcion_Success() {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setNumeroSuscripcion("SUS-101");
        suscripcion.setClienteId("CLI-001");
        suscripcion.setFechaCaducidad(LocalDate.of(2025, 12, 31));
        suscripcion.setSaldoAsesorias(10);

        when(suscripcionRepository.existsById("SUS-101")).thenReturn(false);
        when(suscripcionRepository.save(any(Suscripcion.class))).thenReturn(suscripcion);

        Suscripcion result = suscripcionService.createSuscripcion(suscripcion);

        assertNotNull(result);
        assertEquals("SUS-101", result.getNumeroSuscripcion());
        verify(suscripcionRepository, times(1)).save(any(Suscripcion.class));
    }

    @Test
    void testCreateSuscripcion_AlreadyExists() {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setNumeroSuscripcion("SUS-101");
        suscripcion.setClienteId("CLI-001");  // Asegúrate de que el clienteId esté configurado correctamente
        suscripcion.setFechaCaducidad(LocalDate.of(2025, 12, 31));  // Fecha futura
        suscripcion.setSaldoAsesorias(10);

        // Simula que la suscripción ya existe en el repositorio
        when(suscripcionRepository.existsById("SUS-101")).thenReturn(true);

        // Realiza la prueba para verificar que se lanza la excepción ResponseStatusException
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> suscripcionService.createSuscripcion(suscripcion));

        // Verifica que el código de estado sea 400 (Bad Request) y que el mensaje de la excepción sea el esperado
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("La suscripción con ID SUS-101 ya existe."));
    }



    @Test
    void testCreateSuscripcion_FechaCaducidadPasada() {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setNumeroSuscripcion("SUS-104");
        suscripcion.setClienteId("CLI-001");
        suscripcion.setFechaCaducidad(LocalDate.of(2020, 12, 31));  // Fecha en el pasado
        suscripcion.setSaldoAsesorias(10);

        when(suscripcionRepository.existsById("SUS-104")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> suscripcionService.createSuscripcion(suscripcion));

        assertTrue(ex.getMessage().contains("La fecha de caducidad no puede ser en el pasado"));
    }

    @Test
    void testCreateSuscripcion_SaldoNegativo() {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setNumeroSuscripcion("SUS-105");
        suscripcion.setClienteId("CLI-001");
        suscripcion.setFechaCaducidad(LocalDate.of(2025, 12, 31));
        suscripcion.setSaldoAsesorias(-5);  // Saldo negativo

        when(suscripcionRepository.existsById("SUS-105")).thenReturn(false);

        // Ahora lanzará ResponseStatusException
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> suscripcionService.createSuscripcion(suscripcion));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("El saldo de asesorías no puede ser negativo"));
    }

}
