package com.rapidexpress.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Multa de transito asociada a un conductor, con saldo pendiente de pago. */
public class MultaConductor {
    private Integer idMulta;
    private Integer idConductor;
    private String motivo;
    private BigDecimal montoOriginal;
    private BigDecimal saldoPendiente;
    private LocalDate fechaMulta;
    private LocalDateTime fechaUltimoAbono;
    private EstadoMulta estado;

    public MultaConductor() {
    }

    public MultaConductor(Integer idConductor, String motivo, BigDecimal montoOriginal, LocalDate fechaMulta) {
        this.idConductor = idConductor;
        this.motivo = motivo;
        this.montoOriginal = montoOriginal;
        this.saldoPendiente = montoOriginal;
        this.fechaMulta = fechaMulta;
        this.estado = EstadoMulta.PENDIENTE;
    }

    public Integer getIdMulta() { return idMulta; }
    public void setIdMulta(Integer idMulta) { this.idMulta = idMulta; }

    public Integer getIdConductor() { return idConductor; }
    public void setIdConductor(Integer idConductor) { this.idConductor = idConductor; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public BigDecimal getMontoOriginal() { return montoOriginal; }
    public void setMontoOriginal(BigDecimal montoOriginal) { this.montoOriginal = montoOriginal; }

    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public LocalDate getFechaMulta() { return fechaMulta; }
    public void setFechaMulta(LocalDate fechaMulta) { this.fechaMulta = fechaMulta; }

    public LocalDateTime getFechaUltimoAbono() { return fechaUltimoAbono; }
    public void setFechaUltimoAbono(LocalDateTime fechaUltimoAbono) { this.fechaUltimoAbono = fechaUltimoAbono; }

    public EstadoMulta getEstado() { return estado; }
    public void setEstado(EstadoMulta estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("[Multa #%d] Conductor #%d - %s - Saldo: $%,.0f - Estado: %s",
                idMulta, idConductor, motivo, saldoPendiente, estado);
    }
}
