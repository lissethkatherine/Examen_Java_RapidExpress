package com.rapidexpress.service;

import java.math.BigDecimal;

public class PagoEfectivo implements MetodoPago {
    @Override
    public String describir(BigDecimal monto) {
        return String.format("Pago en EFECTIVO por $%,.0f", monto);
    }
}
