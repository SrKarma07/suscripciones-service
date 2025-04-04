package com.example.suscripciones.controller;

import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.service.SuscripcionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @PostMapping
    public ResponseEntity<Suscripcion> create(@RequestBody Suscripcion suscripcion) {
        Suscripcion nueva = suscripcionService.createSuscripcion(suscripcion);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Suscripcion> getById(@PathVariable("id") String id) {
        Suscripcion sus = suscripcionService.getSuscripcionById(id);
        return ResponseEntity.ok(sus);
    }

    @GetMapping
    public ResponseEntity<List<Suscripcion>> getAll() {
        return ResponseEntity.ok(suscripcionService.getAllSuscripciones());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suscripcion> update(@PathVariable("id") String id,
                                              @RequestBody Suscripcion suscripcion) {
        Suscripcion actualizada = suscripcionService.updateSuscripcion(id, suscripcion);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        suscripcionService.deleteSuscripcion(id);
        return ResponseEntity.noContent().build();
    }

    // Manejo de excepciones
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        // Estructura de la respuesta
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());
        body.put("error", ex.getStatusCode().toString());
        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }
}
