package com.rapidexpress.model;

import java.time.LocalDateTime;

/** Entrada de la bitacora de auditoria del sistema. */
public class RegistroAuditoria {
    private Integer idAuditoria;
    private LocalDateTime fechaHora;
    private String usuarioOperador;
    private String accion;
    private String tablaAfectada;
    private Integer idEntidadAfectada;
    private String descripcion;

    public RegistroAuditoria() {
    }

    public RegistroAuditoria(String usuarioOperador, String accion, String tablaAfectada,
                              Integer idEntidadAfectada, String descripcion) {
        this.usuarioOperador = usuarioOperador;
        this.accion = accion;
        this.tablaAfectada = tablaAfectada;
        this.idEntidadAfectada = idEntidadAfectada;
        this.descripcion = descripcion;
    }

    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getUsuarioOperador() { return usuarioOperador; }
    public void setUsuarioOperador(String usuarioOperador) { this.usuarioOperador = usuarioOperador; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getTablaAfectada() { return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada) { this.tablaAfectada = tablaAfectada; }

    public Integer getIdEntidadAfectada() { return idEntidadAfectada; }
    public void setIdEntidadAfectada(Integer idEntidadAfectada) { this.idEntidadAfectada = idEntidadAfectada; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s#%d) - %s",
                fechaHora, usuarioOperador, accion, tablaAfectada, idEntidadAfectada, descripcion);
    }
}
