package com.example.suscripciones.controller;

import com.example.suscripciones.entity.Asesoria;
import com.example.suscripciones.service.AsesoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asesorias")
public class AsesoriaController {

    private final AsesoriaService asesoriaService;

    // Inyectamos el servicio vía constructor
    public AsesoriaController(AsesoriaService asesoriaService) {
        this.asesoriaService = asesoriaService;
    }

    // POST - Crear una nueva asesoría
    @PostMapping
    public ResponseEntity<Asesoria> create(@RequestBody Asesoria asesoria) {
        Asesoria nueva = asesoriaService.createAsesoria(asesoria);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Asesoria> getById(@PathVariable Long id) {
        Asesoria asesoria = asesoriaService.getAsesoriaById(id);
        return ResponseEntity.ok(asesoria);
    }

    // GET all
    @GetMapping
    public ResponseEntity<List<Asesoria>> getAll() {
        List<Asesoria> lista = asesoriaService.getAllAsesorias();
        return ResponseEntity.ok(lista);
    }

    // UPDATE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Asesoria> update(@PathVariable Long id, @RequestBody Asesoria asesoria) {
        Asesoria actualizada = asesoriaService.updateAsesoria(id, asesoria);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        asesoriaService.deleteAsesoria(id);
        return ResponseEntity.noContent().build();
    }

    // Manejar errores de la creación de asesorías
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
