package com.example.suscripciones.entity;

import java.util.List;

public class ReporteResponse {
    private Suscripcion suscripcion;
    private List<Asesoria> asesorias;

    // Constructor, getters y setters
    public ReporteResponse(Suscripcion suscripcion, List<Asesoria> asesorias) {
        this.suscripcion = suscripcion;
        this.asesorias = asesorias;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public List<Asesoria> getAsesorias() {
        return asesorias;
    }

    public void setAsesorias(List<Asesoria> asesorias) {
        this.asesorias = asesorias;
    }
}
