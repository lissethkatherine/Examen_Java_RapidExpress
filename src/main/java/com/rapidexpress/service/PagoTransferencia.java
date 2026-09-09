package com.rapidexpress.service;

import java.math.BigDecimal;

public class PagoTransferencia implements MetodoPago {
    private final String numeroReferencia;

    public PagoTransferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    @Override
    public String describir(BigDecimal monto) {
        return String.format("Pago por TRANSFERENCIA por $%,.0f (ref: %s)", monto, numeroReferencia);
    }
}
