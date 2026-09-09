package com.rapidexpress.model;

import java.time.LocalDateTime;

/** Conductor asignable a un vehiculo de la flota. */
public class Conductor {
    private Integer idConductor;
    private String numeroIdentificacion;
    private String nombreCompleto;
    private String tipoLicencia;
    private String numeroContacto;
    private EstadoConductor estado;
    private LocalDateTime fechaRegistro;

    public Conductor() {
    }

    public Conductor(String numeroIdentificacion, String nombreCompleto, String tipoLicencia,
                      String numeroContacto) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.tipoLicencia = tipoLicencia;
        this.numeroContacto = numeroContacto;
        this.estado = EstadoConductor.ACTIVO;
    }

    public Integer getIdConductor() { return idConductor; }
    public void setIdConductor(Integer idConductor) { this.idConductor = idConductor; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(String tipoLicencia) { this.tipoLicencia = tipoLicencia; }

    public String getNumeroContacto() { return numeroContacto; }
    public void setNumeroContacto(String numeroContacto) { this.numeroContacto = numeroContacto; }

    public EstadoConductor getEstado() { return estado; }
    public void setEstado(EstadoConductor estado) { this.estado = estado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format("[%d] %s - CC %s - Licencia %s - Tel: %s - Estado: %s",
                idConductor, nombreCompleto, numeroIdentificacion, tipoLicencia, numeroContacto, estado);
    }
}
