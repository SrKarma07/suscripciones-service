package com.example.suscripciones.controller;

import com.example.suscripciones.entity.Suscripcion;
import com.example.suscripciones.service.SuscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
