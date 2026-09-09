package com.rapidexpress.model;

import java.time.LocalDateTime;

/** Historial de asignacion de un conductor a un vehiculo. */
public class AsignacionVehiculoConductor {
    private Integer idAsignacion;
    private Integer idVehiculo;
    private Integer idConductor;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaLiberacion;
    private String estado; // Activa | Finalizada

    public AsignacionVehiculoConductor() {
    }

    public AsignacionVehiculoConductor(Integer idVehiculo, Integer idConductor) {
        this.idVehiculo = idVehiculo;
        this.idConductor = idConductor;
        this.estado = "Activa";
    }

    public Integer getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(Integer idAsignacion) { this.idAsignacion = idAsignacion; }

    public Integer getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(Integer idVehiculo) { this.idVehiculo = idVehiculo; }

    public Integer getIdConductor() { return idConductor; }
    public void setIdConductor(Integer idConductor) { this.idConductor = idConductor; }

    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public LocalDateTime getFechaLiberacion() { return fechaLiberacion; }
    public void setFechaLiberacion(LocalDateTime fechaLiberacion) { this.fechaLiberacion = fechaLiberacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("[%d] Vehiculo #%d - Conductor #%d - Desde: %s - Estado: %s",
                idAsignacion, idVehiculo, idConductor, fechaAsignacion, estado);
    }
}
