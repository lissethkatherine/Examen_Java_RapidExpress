package com.rapidexpress.model;

/**
 * Excepcion personalizada (checked): se lanza cuando se intenta registrar
 * un abono invalido (mayor al saldo pendiente, o negativo/cero).
 */
public class AbonoInvalidoException extends Exception {
    public AbonoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
