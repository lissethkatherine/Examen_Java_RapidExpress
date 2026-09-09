package com.rapidexpress.model;

import java.time.LocalDateTime;

/** Relacion entre una ruta y un paquete asignado a ella. */
public class RutaPaquete {
    private Integer idRutaPaquete;
    private Integer idRuta;
    private Integer idPaquete;
    private int ordenEntrega;
    private String estadoEntrega; // Pendiente | Entregado | Devuelto
    private LocalDateTime fechaEntrega;

    public RutaPaquete() {
    }

    public RutaPaquete(Integer idRuta, Integer idPaquete, int ordenEntrega) {
        this.idRuta = idRuta;
        this.idPaquete = idPaquete;
        this.ordenEntrega = ordenEntrega;
        this.estadoEntrega = "Pendiente";
    }

    public Integer getIdRutaPaquete() { return idRutaPaquete; }
    public void setIdRutaPaquete(Integer idRutaPaquete) { this.idRutaPaquete = idRutaPaquete; }

    public Integer getIdRuta() { return idRuta; }
    public void setIdRuta(Integer idRuta) { this.idRuta = idRuta; }

    public Integer getIdPaquete() { return idPaquete; }
    public void setIdPaquete(Integer idPaquete) { this.idPaquete = idPaquete; }

    public int getOrdenEntrega() { return ordenEntrega; }
    public void setOrdenEntrega(int ordenEntrega) { this.ordenEntrega = ordenEntrega; }

    public String getEstadoEntrega() { return estadoEntrega; }
    public void setEstadoEntrega(String estadoEntrega) { this.estadoEntrega = estadoEntrega; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    @Override
    public String toString() {
        return String.format("Ruta #%d - Paquete #%d - Orden: %d - Estado: %s",
                idRuta, idPaquete, ordenEntrega, estadoEntrega);
    }
}
