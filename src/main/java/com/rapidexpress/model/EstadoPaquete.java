package com.rapidexpress.model;

/** Ciclo de vida de un paquete dentro del sistema. */
public enum EstadoPaquete {
    EN_BODEGA("En Bodega"),
    ASIGNADO_A_RUTA("Asignado a Ruta"),
    EN_TRANSITO("En Transito"),
    ENTREGADO("Entregado"),
    DEVUELTO("Devuelto");

    private final String etiqueta;

    EstadoPaquete(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static EstadoPaquete fromEtiqueta(String etiqueta) {
        for (EstadoPaquete estado : values()) {
            if (estado.etiqueta.equalsIgnoreCase(etiqueta)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de paquete invalido: " + etiqueta);
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
