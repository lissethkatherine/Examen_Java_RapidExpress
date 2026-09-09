package com.rapidexpress.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Vehiculo perteneciente al parque automotor de la empresa. */
public class Vehiculo {
    private Integer idVehiculo;
    private String placa;
    private String marca;
    private String modelo;
    private int anioFabricacion;
    private BigDecimal capacidadCargaKg;
    private EstadoVehiculo estado;
    private LocalDateTime fechaRegistro;

    public Vehiculo() {
    }

    public Vehiculo(String placa, String marca, String modelo, int anioFabricacion,
                     BigDecimal capacidadCargaKg) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
        this.capacidadCargaKg = capacidadCargaKg;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    public Integer getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(Integer idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnioFabricacion() { return anioFabricacion; }
    public void setAnioFabricacion(int anioFabricacion) { this.anioFabricacion = anioFabricacion; }

    public BigDecimal getCapacidadCargaKg() { return capacidadCargaKg; }
    public void setCapacidadCargaKg(BigDecimal capacidadCargaKg) { this.capacidadCargaKg = capacidadCargaKg; }

    public EstadoVehiculo getEstado() { return estado; }
    public void setEstado(EstadoVehiculo estado) { this.estado = estado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s %s (%d) - Cap: %s kg - Estado: %s",
                idVehiculo, placa, marca, modelo, anioFabricacion, capacidadCargaKg, estado);
    }
}
