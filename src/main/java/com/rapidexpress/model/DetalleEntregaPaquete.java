package com.rapidexpress.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fila de detalle para el reporte "entregas de un conductor en un rango de
 * fechas": una fila por cada paquete transportado, no por ruta. Es un
 * objeto de solo lectura (DTO) que combina datos de rutas, ruta_paquetes
 * y paquetes mediante un JOIN en {@code RutaDAO}.
 */
public class DetalleEntregaPaquete {
    private Integer idRuta;
    private LocalDate fechaRuta;
    private Integer idPaquete;
    private String codigoSeguimiento;
    private String descripcionContenido;
    private String direccionDestino;
    private String estadoEntrega;
    private LocalDateTime fechaEntrega;

    public DetalleEntregaPaquete() {
    }

    public Integer getIdRuta() { return idRuta; }
    public void setIdRuta(Integer idRuta) { this.idRuta = idRuta; }

    public LocalDate getFechaRuta() { return fechaRuta; }
    public void setFechaRuta(LocalDate fechaRuta) { this.fechaRuta = fechaRuta; }

    public Integer getIdPaquete() { return idPaquete; }
    public void setIdPaquete(Integer idPaquete) { this.idPaquete = idPaquete; }

    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public void setCodigoSeguimiento(String codigoSeguimiento) { this.codigoSeguimiento = codigoSeguimiento; }

    public String getDescripcionContenido() { return descripcionContenido; }
    public void setDescripcionContenido(String descripcionContenido) { this.descripcionContenido = descripcionContenido; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    public String getEstadoEntrega() { return estadoEntrega; }
    public void setEstadoEntrega(String estadoEntrega) { this.estadoEntrega = estadoEntrega; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    @Override
    public String toString() {
        String entrega = fechaEntrega != null ? fechaEntrega.toString() : "pendiente";
        return String.format("Ruta #%d (%s) - Paquete #%d [%s] %s -> %s - Estado: %s - Entrega: %s",
                idRuta, fechaRuta, idPaquete, codigoSeguimiento, descripcionContenido,
                direccionDestino, estadoEntrega, entrega);
    }
}
