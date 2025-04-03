package com.example.suscripciones.service.impl;

import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.repository.SuscripcionRepository;
import com.example.suscripciones.service.SuscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    // RestTemplate para comunicarte con el microservicio personas-service
    // (en producción, podrías usar Feign o config de Spring Cloud)
    private final RestTemplate restTemplate = new RestTemplate();

    // Cambia la URL a la ubicación real de tu personas-service
    private static final String PERSONAS_SERVICE_URL = "http://localhost:8080/api/clientes/";

    public SuscripcionServiceImpl(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    @Override
    public Suscripcion createSuscripcion(Suscripcion suscripcion) {
        // 1. Verificar que el cliente exista
        String clienteId = suscripcion.getClienteId();
        validarClienteExiste(clienteId);

        // 2. Verificar que la suscripción no exista
        if (suscripcionRepository.existsById(suscripcion.getNumeroSuscripcion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La suscripción con ID " + suscripcion.getNumeroSuscripcion() + " ya existe.");
        }

        // 3. Validar que la fecha de caducidad no esté en el pasado
        if (suscripcion.getFechaCaducidad().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de caducidad no puede ser en el pasado.");
        }

        // 4. Validar que el saldo de asesorías no sea negativo
        if (suscripcion.getSaldoAsesorias() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El saldo de asesorías no puede ser negativo.");
        }

        // 5. Guardar en la BD local de suscripciones
        return suscripcionRepository.save(suscripcion);
    }


    @Override
    public Suscripcion getSuscripcionById(String numeroSuscripcion) {
        return suscripcionRepository.findById(numeroSuscripcion)
                .orElseThrow(() -> new RuntimeException("No existe la suscripción con ID " + numeroSuscripcion));
    }

    @Override
    public List<Suscripcion> getAllSuscripciones() {
        return suscripcionRepository.findAll();
    }

    @Override
    public Suscripcion updateSuscripcion(String numeroSuscripcion, Suscripcion nuevaData) {
        // 1. Obtener la suscripción existente
        Suscripcion existente = getSuscripcionById(numeroSuscripcion);

        // 2. Si cambió el clienteId, verificamos en personas-service
        if (!nuevaData.getClienteId().equals(existente.getClienteId())) {
            validarClienteExiste(nuevaData.getClienteId());
        }

        // 3. Actualizar campos
        existente.setClienteId(nuevaData.getClienteId());
        existente.setTipoSuscripcion(nuevaData.getTipoSuscripcion());
        existente.setCantidadAsesorias(nuevaData.getCantidadAsesorias());
        existente.setSaldoAsesorias(nuevaData.getSaldoAsesorias());
        existente.setFechaCaducidad(nuevaData.getFechaCaducidad());
        existente.setEstado(nuevaData.getEstado());

        return suscripcionRepository.save(existente);
    }

    @Override
    public void deleteSuscripcion(String numeroSuscripcion) {
        if (!suscripcionRepository.existsById(numeroSuscripcion)) {
            throw new RuntimeException("No existe la suscripción con ID " + numeroSuscripcion);
        }
        suscripcionRepository.deleteById(numeroSuscripcion);
    }

    /**
     * Método privado para llamar al microservicio personas-service
     * y verificar que el cliente exista.
     */
    private void validarClienteExiste(String clienteId) {
        try {
            restTemplate.getForObject(PERSONAS_SERVICE_URL + clienteId, Object.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el cliente con ID: " + clienteId);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al validar cliente ID: " + clienteId);
        }
    }

}
