package com.example.suscripciones.service.impl;

import com.example.suscripciones.entity.Asesoria;
import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.repository.AsesoriaRepository;
import com.example.suscripciones.repository.SuscripcionRepository;
import com.example.suscripciones.service.AsesoriaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AsesoriaServiceImpl implements AsesoriaService {

    private final AsesoriaRepository asesoriaRepository;
    private final SuscripcionRepository suscripcionRepository;

    // Inyectamos el repositorio
    public AsesoriaServiceImpl(AsesoriaRepository asesoriaRepository, SuscripcionRepository suscripcionRepository) {
        this.asesoriaRepository = asesoriaRepository;
        this.suscripcionRepository = suscripcionRepository;
    }

    @Override
    public Asesoria createAsesoria(Asesoria asesoria) {
        // 1. Obtener la suscripción utilizando el numeroSuscripcion desde el objeto 'suscripcion'
        String numeroSuscripcion = asesoria.getSuscripcion().getNumeroSuscripcion();  // Accedemos al numeroSuscripcion a través del objeto Suscripcion

        // Verificamos que la suscripción existe
        Suscripcion suscripcion = suscripcionRepository.findById(numeroSuscripcion)
                .orElseThrow(() -> new RuntimeException("No existe la suscripción con ID: " + numeroSuscripcion));

        // 2. Verificamos si la suscripción está activa y no ha caducado
        LocalDate fechaActual = LocalDate.now();
        if (suscripcion.getFechaCaducidad().isBefore(fechaActual)) {
            throw new RuntimeException("La suscripción ha caducado y no puede realizarse la asesoría.");
        }

        // 3. Verificamos si hay saldo suficiente para la asesoría (saldo <= 0)
        if (suscripcion.getSaldoAsesorias() <= 0) {
            throw new RuntimeException("Saldo no disponible para realizar la asesoría.");
        }

        // 4. Decrementar el saldo de asesorías de la suscripción
        suscripcion.setSaldoAsesorias(suscripcion.getSaldoAsesorias() - asesoria.getCantidad());

        // 5. Asignamos la suscripción a la asesoría antes de guardarla
        asesoria.setSuscripcion(suscripcion);

        // 6. Guardamos la nueva asesoría
        Asesoria nuevaAsesoria = asesoriaRepository.save(asesoria);

        // 7. Guardamos la suscripción con el saldo actualizado
        suscripcionRepository.save(suscripcion);

        return nuevaAsesoria;
    }



    @Override
    public Asesoria getAsesoriaById(Long id) {
        Optional<Asesoria> optional = asesoriaRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("No existe la asesoría con ID: " + id);
        }
        return optional.get();
    }

    @Override
    public List<Asesoria> getAllAsesorias() {
        return asesoriaRepository.findAll();
    }

    @Override
    public Asesoria updateAsesoria(Long id, Asesoria asesoria) {
        // Verificamos existencia
        Asesoria existente = getAsesoriaById(id);

        // Actualizamos los campos
        existente.setFecha(asesoria.getFecha());
        existente.setTipoAsesoria(asesoria.getTipoAsesoria());
        existente.setEstado(asesoria.getEstado());
        existente.setCantidad(asesoria.getCantidad());
        existente.setSaldoAsesorias(asesoria.getSaldoAsesorias());

        // Si está relacionado con Suscripcion, también podemos actualizarlo
        existente.setSuscripcion(asesoria.getSuscripcion());

        return asesoriaRepository.save(existente);
    }

    @Override
    public void deleteAsesoria(Long id) {
        if (!asesoriaRepository.existsById(id)) {
            throw new RuntimeException("No existe la asesoría con ID: " + id);
        }
        asesoriaRepository.deleteById(id);
    }
}
