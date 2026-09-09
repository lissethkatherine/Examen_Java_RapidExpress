package com.rapidexpress.controller;

import com.rapidexpress.dao.AsignacionDAO;
import com.rapidexpress.dao.ConductorDAO;
import com.rapidexpress.dao.VehiculoDAO;
import com.rapidexpress.model.*;
import com.rapidexpress.service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de ConductorController, en particular de la regla de
 * negocio "un conductor no puede estar asignado a mas de un vehiculo
 * simultaneamente" exigida por el modulo de Gestion de Personal.
 */
@ExtendWith(MockitoExtension.class)
class ConductorControllerTest {

    @Mock private ConductorDAO conductorDAO;
    @Mock private VehiculoDAO vehiculoDAO;
    @Mock private AsignacionDAO asignacionDAO;
    @Mock private AuditoriaService auditoriaService;

    private ConductorController controller;

    @BeforeEach
    void setUp() {
        controller = new ConductorController(conductorDAO, vehiculoDAO, asignacionDAO, auditoriaService);
    }

    private Vehiculo vehiculoDisponible(int id) {
        Vehiculo v = new Vehiculo("RXP" + id, "Kia", "K2500", 2021, BigDecimal.valueOf(1500));
        v.setIdVehiculo(id);
        v.setEstado(EstadoVehiculo.DISPONIBLE);
        return v;
    }

    private Conductor conductorActivo(int id) {
        Conductor c = new Conductor("100000" + id, "Conductor " + id, "C2", "3000000000");
        c.setIdConductor(id);
        c.setEstado(EstadoConductor.ACTIVO);
        return c;
    }

    @Test
    void asignarConductorAVehiculo_conDatosValidos_creaLaAsignacion() {
        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculoDisponible(1)));
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(conductorActivo(1)));
        when(conductorDAO.tieneAsignacionActiva(1)).thenReturn(false);

        String resultado = controller.asignarConductorAVehiculo(1, 1, "operador.test");

        assertTrue(resultado.startsWith("OK"), resultado);
        verify(asignacionDAO, times(1)).crear(any(AsignacionVehiculoConductor.class));
        verify(auditoriaService).registrar(eq("operador.test"), eq("ASIGNAR_CONDUCTOR"),
                eq("asignaciones_vehiculo_conductor"), any(), anyString());
    }

    @Test
    void asignarConductorAVehiculo_conductorYaTieneAsignacionActiva_esRechazada() {
        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculoDisponible(1)));
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(conductorActivo(1)));
        when(conductorDAO.tieneAsignacionActiva(1)).thenReturn(true);

        String resultado = controller.asignarConductorAVehiculo(1, 1, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("ya tiene una asignacion activa"));
        verify(asignacionDAO, never()).crear(any());
    }

    @Test
    void asignarConductorAVehiculo_vehiculoNoDisponible_esRechazada() {
        Vehiculo enMantenimiento = vehiculoDisponible(1);
        enMantenimiento.setEstado(EstadoVehiculo.EN_MANTENIMIENTO);
        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(enMantenimiento));

        String resultado = controller.asignarConductorAVehiculo(1, 1, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("no esta Disponible"));
        verifyNoInteractions(asignacionDAO);
    }

    @Test
    void asignarConductorAVehiculo_conductorNoActivo_esRechazada() {
        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculoDisponible(1)));
        Conductor deVacaciones = conductorActivo(1);
        deVacaciones.setEstado(EstadoConductor.DE_VACACIONES);
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(deVacaciones));

        String resultado = controller.asignarConductorAVehiculo(1, 1, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("no esta Activo"));
        verifyNoInteractions(asignacionDAO);
    }

    @Test
    void asignarConductorAVehiculo_vehiculoInexistente_esRechazada() {
        when(vehiculoDAO.buscarPorId(99)).thenReturn(Optional.empty());

        String resultado = controller.asignarConductorAVehiculo(1, 99, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("no existe"));
        verifyNoInteractions(conductorDAO, asignacionDAO);
    }

    @Test
    void asignarConductorAVehiculo_conductorInexistente_esRechazada() {
        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculoDisponible(1)));
        when(conductorDAO.buscarPorId(42)).thenReturn(Optional.empty());

        String resultado = controller.asignarConductorAVehiculo(42, 1, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("no existe"));
        verifyNoInteractions(asignacionDAO);
    }

    @Test
    void actualizarEstado_exitoso_registraAuditoria() {
        when(conductorDAO.actualizarEstado(1, EstadoConductor.INACTIVO)).thenReturn(true);

        boolean ok = controller.actualizarEstado(1, EstadoConductor.INACTIVO, "operador.test");

        assertTrue(ok);
        verify(auditoriaService).registrar(eq("operador.test"), eq("CAMBIO_ESTADO_CONDUCTOR"),
                eq("conductores"), eq(1), anyString());
    }

    @Test
    void actualizarEstado_conductorInexistente_noRegistraAuditoria() {
        when(conductorDAO.actualizarEstado(999, EstadoConductor.INACTIVO)).thenReturn(false);

        boolean ok = controller.actualizarEstado(999, EstadoConductor.INACTIVO, "operador.test");

        assertFalse(ok);
        verifyNoInteractions(auditoriaService);
    }
}
