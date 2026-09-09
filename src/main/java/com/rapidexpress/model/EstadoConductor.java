package com.rapidexpress.model;

/** Estados posibles de un conductor. */
public enum EstadoConductor {
    ACTIVO("Activo"),
    DE_VACACIONES("De Vacaciones"),
    INACTIVO("Inactivo");

    private final String etiqueta;

    EstadoConductor(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static EstadoConductor fromEtiqueta(String etiqueta) {
        for (EstadoConductor estado : values()) {
            if (estado.etiqueta.equalsIgnoreCase(etiqueta)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de conductor invalido: " + etiqueta);
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
