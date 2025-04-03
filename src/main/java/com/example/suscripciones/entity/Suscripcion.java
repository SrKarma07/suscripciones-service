package com.example.suscripciones.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "suscripcion")
public class Suscripcion {

    @Id
    private String numeroSuscripcion;

    // NUEVO: en lugar de un objeto Cliente, solo guardamos su ID.
    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    private String tipoSuscripcion;
    private int cantidadAsesorias;
    private int saldoAsesorias;
    private LocalDate fechaCaducidad;
    private String estado;

    // Getters y Setters (completos)
    public String getNumeroSuscripcion() {
        return numeroSuscripcion;
    }

    public void setNumeroSuscripcion(String numeroSuscripcion) {
        this.numeroSuscripcion = numeroSuscripcion;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipoSuscripcion() {
        return tipoSuscripcion;
    }

    public void setTipoSuscripcion(String tipoSuscripcion) {
        this.tipoSuscripcion = tipoSuscripcion;
    }

    public int getCantidadAsesorias() {
        return cantidadAsesorias;
    }

    public void setCantidadAsesorias(int cantidadAsesorias) {
        this.cantidadAsesorias = cantidadAsesorias;
    }

    public int getSaldoAsesorias() {
        return saldoAsesorias;
    }

    public void setSaldoAsesorias(int saldoAsesorias) {
        this.saldoAsesorias = saldoAsesorias;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
