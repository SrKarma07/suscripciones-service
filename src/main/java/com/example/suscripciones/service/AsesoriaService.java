package com.example.suscripciones.service;

import com.example.suscripciones.entity.Asesoria;

import java.util.List;

public interface AsesoriaService {

    Asesoria createAsesoria(Asesoria asesoria);

    Asesoria getAsesoriaById(Long id);

    List<Asesoria> getAllAsesorias();

    Asesoria updateAsesoria(Long id, Asesoria asesoria);

    void deleteAsesoria(Long id);

}
