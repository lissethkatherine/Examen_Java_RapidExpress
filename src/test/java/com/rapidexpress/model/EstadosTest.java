package com.rapidexpress.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de los enums de estado. Verifican que el mapeo entre
 * la etiqueta almacenada en la base de datos (columnas ENUM de MySQL) y el
 * valor de Java sea correcto en ambas direcciones, y que valores invalidos
 * sean rechazados.
 */
class EstadosTest {

    @Test
    void estadoVehiculo_fromEtiqueta_reconoceLosTresValoresValidos() {
        assertEquals(EstadoVehiculo.DISPONIBLE, EstadoVehiculo.fromEtiqueta("Disponible"));
        assertEquals(EstadoVehiculo.EN_RUTA, EstadoVehiculo.fromEtiqueta("En Ruta"));
        assertEquals(EstadoVehiculo.EN_MANTENIMIENTO, EstadoVehiculo.fromEtiqueta("En Mantenimiento"));
    }

    @Test
    void estadoVehiculo_fromEtiqueta_esInsensibleAMayusculas() {
        assertEquals(EstadoVehiculo.DISPONIBLE, EstadoVehiculo.fromEtiqueta("disponible"));
    }

    @Test
    void estadoVehiculo_fromEtiqueta_lanzaExcepcionConValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> EstadoVehiculo.fromEtiqueta("Perdido"));
    }

    @Test
    void estadoConductor_fromEtiqueta_reconoceLosTresValoresValidos() {
        assertEquals(EstadoConductor.ACTIVO, EstadoConductor.fromEtiqueta("Activo"));
        assertEquals(EstadoConductor.DE_VACACIONES, EstadoConductor.fromEtiqueta("De Vacaciones"));
        assertEquals(EstadoConductor.INACTIVO, EstadoConductor.fromEtiqueta("Inactivo"));
    }

    @Test
    void estadoConductor_fromEtiqueta_lanzaExcepcionConValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> EstadoConductor.fromEtiqueta("Suspendido"));
    }

    @Test
    void estadoPaquete_fromEtiqueta_reconoceLosCincoValoresValidos() {
        assertEquals(EstadoPaquete.EN_BODEGA, EstadoPaquete.fromEtiqueta("En Bodega"));
        assertEquals(EstadoPaquete.ASIGNADO_A_RUTA, EstadoPaquete.fromEtiqueta("Asignado a Ruta"));
        assertEquals(EstadoPaquete.EN_TRANSITO, EstadoPaquete.fromEtiqueta("En Transito"));
        assertEquals(EstadoPaquete.ENTREGADO, EstadoPaquete.fromEtiqueta("Entregado"));
        assertEquals(EstadoPaquete.DEVUELTO, EstadoPaquete.fromEtiqueta("Devuelto"));
    }

    @Test
    void estadoPaquete_fromEtiqueta_lanzaExcepcionConValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> EstadoPaquete.fromEtiqueta("Extraviado"));
    }

    @Test
    void estadoRuta_fromEtiqueta_reconoceLosTresValoresValidos() {
        assertEquals(EstadoRuta.PLANIFICADA, EstadoRuta.fromEtiqueta("Planificada"));
        assertEquals(EstadoRuta.EN_CURSO, EstadoRuta.fromEtiqueta("En Curso"));
        assertEquals(EstadoRuta.FINALIZADA, EstadoRuta.fromEtiqueta("Finalizada"));
    }

    @Test
    void estadoRuta_fromEtiqueta_lanzaExcepcionConValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> EstadoRuta.fromEtiqueta("Cancelada"));
    }

    @Test
    void etiqueta_esConsistenteEnAmbasDirecciones() {
        for (EstadoVehiculo estado : EstadoVehiculo.values()) {
            assertEquals(estado, EstadoVehiculo.fromEtiqueta(estado.getEtiqueta()));
        }
        for (EstadoPaquete estado : EstadoPaquete.values()) {
            assertEquals(estado, EstadoPaquete.fromEtiqueta(estado.getEtiqueta()));
        }
    }
}
