package com.rapidexpress.model;

/** Estados posibles de un vehiculo dentro de la flota. */
public enum EstadoVehiculo {
    DISPONIBLE("Disponible"),
    EN_RUTA("En Ruta"),
    EN_MANTENIMIENTO("En Mantenimiento");

    private final String etiqueta;

    EstadoVehiculo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    /** Convierte el valor almacenado en la base de datos (ENUM de MySQL) al enum de Java. */
    public static EstadoVehiculo fromEtiqueta(String etiqueta) {
        for (EstadoVehiculo estado : values()) {
            if (estado.etiqueta.equalsIgnoreCase(etiqueta)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de vehiculo invalido: " + etiqueta);
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
