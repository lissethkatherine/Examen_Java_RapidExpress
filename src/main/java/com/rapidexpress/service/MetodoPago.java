package com.rapidexpress.service;

/**
 * Patron de diseno Strategy: cada tipo de abono (efectivo, transferencia)
 * implementa esta interfaz de forma distinta. El codigo que registra el
 * abono (CreditoService/MultaConductorController) no necesita saber CUAL
 * estrategia esta usando, solo le pide que "describa" el pago.
 */
public interface MetodoPago {
    /** Texto descriptivo del metodo de pago, usado en el registro/archivo. */
    String describir(java.math.BigDecimal monto);
}
