package com.rapidexpress.model;

public enum EstadoMulta {
    PENDIENTE("Pendiente"),
    PAGADA("Pagada");

    private final String etiqueta;

    EstadoMulta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() { return etiqueta; }

    public static EstadoMulta fromEtiqueta(String etiqueta) {
        for (EstadoMulta e : values()) {
            if (e.etiqueta.equalsIgnoreCase(etiqueta)) return e;
        }
        throw new IllegalArgumentException("Estado de multa invalido: " + etiqueta);
    }

    @Override
    public String toString() { return etiqueta; }
}
