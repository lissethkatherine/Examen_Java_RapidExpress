package com.rapidexpress.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Registro individual del historial de mantenimiento de un vehiculo. */
public class MantenimientoVehiculo {
    private Integer idMantenimiento;
    private Integer idVehiculo;
    private String tipoMantenimiento; // Preventivo | Correctivo
    private LocalDate fechaMantenimiento;
    private String descripcion;
    private BigDecimal costo;
    private String taller;
    private LocalDateTime fechaRegistro;

    public MantenimientoVehiculo() {
    }

    public MantenimientoVehiculo(Integer idVehiculo, String tipoMantenimiento, LocalDate fechaMantenimiento,
                                  String descripcion, BigDecimal costo, String taller) {
        this.idVehiculo = idVehiculo;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fechaMantenimiento = fechaMantenimiento;
        this.descripcion = descripcion;
        this.costo = costo;
        this.taller = taller;
    }

    public Integer getIdMantenimiento() { return idMantenimiento; }
    public void setIdMantenimiento(Integer idMantenimiento) { this.idMantenimiento = idMantenimiento; }

    public Integer getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(Integer idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }

    public LocalDate getFechaMantenimiento() { return fechaMantenimiento; }
    public void setFechaMantenimiento(LocalDate fechaMantenimiento) { this.fechaMantenimiento = fechaMantenimiento; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

    public String getTaller() { return taller; }
    public void setTaller(String taller) { this.taller = taller; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format("[%d] Vehiculo #%d - %s - %s - %s - $%s (%s)",
                idMantenimiento, idVehiculo, tipoMantenimiento, fechaMantenimiento, descripcion, costo, taller);
    }
}
