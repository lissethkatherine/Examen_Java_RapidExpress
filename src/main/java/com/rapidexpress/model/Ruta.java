package com.rapidexpress.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Hoja de ruta diaria: agrupa un vehiculo, un conductor y un conjunto de paquetes. */
public class Ruta {
    private Integer idRuta;
    private Integer idVehiculo;
    private Integer idConductor;
    private LocalDate fechaRuta;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private EstadoRuta estado;
    private BigDecimal cargaTotalKg;
    private LocalDateTime fechaCreacion;

    public Ruta() {
    }

    public Ruta(Integer idVehiculo, Integer idConductor, LocalDate fechaRuta) {
        this.idVehiculo = idVehiculo;
        this.idConductor = idConductor;
        this.fechaRuta = fechaRuta;
        this.estado = EstadoRuta.PLANIFICADA;
        this.cargaTotalKg = BigDecimal.ZERO;
    }

    public Integer getIdRuta() { return idRuta; }
    public void setIdRuta(Integer idRuta) { this.idRuta = idRuta; }

    public Integer getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(Integer idVehiculo) { this.idVehiculo = idVehiculo; }

    public Integer getIdConductor() { return idConductor; }
    public void setIdConductor(Integer idConductor) { this.idConductor = idConductor; }

    public LocalDate getFechaRuta() { return fechaRuta; }
    public void setFechaRuta(LocalDate fechaRuta) { this.fechaRuta = fechaRuta; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }

    public EstadoRuta getEstado() { return estado; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }

    public BigDecimal getCargaTotalKg() { return cargaTotalKg; }
    public void setCargaTotalKg(BigDecimal cargaTotalKg) { this.cargaTotalKg = cargaTotalKg; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return String.format("[%d] Vehiculo #%d - Conductor #%d - Fecha: %s - Estado: %s - Carga: %s kg",
                idRuta, idVehiculo, idConductor, fechaRuta, estado, cargaTotalKg);
    }
}
