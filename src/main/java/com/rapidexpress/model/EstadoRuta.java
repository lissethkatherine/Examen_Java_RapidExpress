package com.rapidexpress.model;

/** Estados posibles de una hoja de ruta. */
public enum EstadoRuta {
    PLANIFICADA("Planificada"),
    EN_CURSO("En Curso"),
    FINALIZADA("Finalizada");

    private final String etiqueta;

    EstadoRuta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static EstadoRuta fromEtiqueta(String etiqueta) {
        for (EstadoRuta estado : values()) {
            if (estado.etiqueta.equalsIgnoreCase(etiqueta)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de ruta invalido: " + etiqueta);
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
