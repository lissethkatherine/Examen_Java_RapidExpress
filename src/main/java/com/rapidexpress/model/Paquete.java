package com.rapidexpress.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Paquete registrado para su distribucion. */
public class Paquete {
    private Integer idPaquete;
    private String codigoSeguimiento;
    private String descripcionContenido;
    private BigDecimal pesoKg;
    private String dimensiones;
    private String direccionOrigen;
    private String direccionDestino;
    private Integer idRemitente;
    private Integer idDestinatario;
    private EstadoPaquete estado;
    private LocalDateTime fechaRegistro;

    public Paquete() {
    }

    public Paquete(String descripcionContenido, BigDecimal pesoKg, String dimensiones,
                    String direccionOrigen, String direccionDestino, Integer idRemitente,
                    Integer idDestinatario) {
        this.descripcionContenido = descripcionContenido;
        this.pesoKg = pesoKg;
        this.dimensiones = dimensiones;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.idRemitente = idRemitente;
        this.idDestinatario = idDestinatario;
        this.estado = EstadoPaquete.EN_BODEGA;
    }

    public Integer getIdPaquete() { return idPaquete; }
    public void setIdPaquete(Integer idPaquete) { this.idPaquete = idPaquete; }

    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public void setCodigoSeguimiento(String codigoSeguimiento) { this.codigoSeguimiento = codigoSeguimiento; }

    public String getDescripcionContenido() { return descripcionContenido; }
    public void setDescripcionContenido(String descripcionContenido) { this.descripcionContenido = descripcionContenido; }

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }

    public String getDireccionOrigen() { return direccionOrigen; }
    public void setDireccionOrigen(String direccionOrigen) { this.direccionOrigen = direccionOrigen; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    public Integer getIdRemitente() { return idRemitente; }
    public void setIdRemitente(Integer idRemitente) { this.idRemitente = idRemitente; }

    public Integer getIdDestinatario() { return idDestinatario; }
    public void setIdDestinatario(Integer idDestinatario) { this.idDestinatario = idDestinatario; }

    public EstadoPaquete getEstado() { return estado; }
    public void setEstado(EstadoPaquete estado) { this.estado = estado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%s kg) - %s -> %s - Estado: %s",
                idPaquete, codigoSeguimiento, descripcionContenido, pesoKg,
                direccionOrigen, direccionDestino, estado);
    }
}
