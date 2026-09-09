package com.rapidexpress.model;

import java.time.LocalDateTime;

/** Persona que interviene en un envio como remitente o destinatario. */
public class Cliente {
    private Integer idCliente;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaRegistro;

    public Cliente() {
    }

    public Cliente(String nombreCompleto, String tipoDocumento, String numeroDocumento,
                    String telefono, String email, String direccion) {
        this.nombreCompleto = nombreCompleto;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s %s - Tel: %s", idCliente, nombreCompleto,
                tipoDocumento, numeroDocumento, telefono);
    }
}
