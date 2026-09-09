package com.rapidexpress.controller;

import com.rapidexpress.dao.MantenimientoDAO;
import com.rapidexpress.dao.VehiculoDAO;
import com.rapidexpress.model.EstadoVehiculo;
import com.rapidexpress.model.MantenimientoVehiculo;
import com.rapidexpress.model.Vehiculo;
import com.rapidexpress.service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculoControllerTest {

    @Mock private VehiculoDAO vehiculoDAO;
    @Mock private MantenimientoDAO mantenimientoDAO;
    @Mock private AuditoriaService auditoriaService;

    private VehiculoController controller;

    @BeforeEach
    void setUp() {
        controller = new VehiculoController(vehiculoDAO, mantenimientoDAO, auditoriaService);
    }

    @Test
    void registrarMantenimiento_cambiaElVehiculoAEnMantenimientoYAuditando() {
        MantenimientoVehiculo mantenimiento = new MantenimientoVehiculo(5, "Preventivo",
                LocalDate.now(), "Cambio de aceite", BigDecimal.valueOf(250000), "Taller Central");
        MantenimientoVehiculo creado = new MantenimientoVehiculo(5, "Preventivo",
                LocalDate.now(), "Cambio de aceite", BigDecimal.valueOf(250000), "Taller Central");
        creado.setIdMantenimiento(77);
        when(mantenimientoDAO.crear(mantenimiento)).thenReturn(creado);

        MantenimientoVehiculo resultado = controller.registrarMantenimiento(mantenimiento, "operador.test");

        assertEquals(77, resultado.getIdMantenimiento());
        verify(vehiculoDAO).actualizarEstado(5, EstadoVehiculo.EN_MANTENIMIENTO);
        verify(auditoriaService).registrar(eq("operador.test"), eq("REGISTRO_MANTENIMIENTO"),
                eq("mantenimientos_vehiculo"), eq(77), anyString());
    }

    @Test
    void registrarVehiculo_delegaEnElDaoYRegistraAuditoria() {
        Vehiculo vehiculo = new Vehiculo("RXP999", "Ford", "Transit", 2023, BigDecimal.valueOf(1200));
        Vehiculo creado = new Vehiculo("RXP999", "Ford", "Transit", 2023, BigDecimal.valueOf(1200));
        creado.setIdVehiculo(50);
        when(vehiculoDAO.crear(vehiculo)).thenReturn(creado);

        Vehiculo resultado = controller.registrarVehiculo(vehiculo, "operador.test");

        assertEquals(50, resultado.getIdVehiculo());
        verify(auditoriaService).registrar(eq("operador.test"), eq("CREAR_VEHICULO"), eq("vehiculos"),
                eq(50), anyString());
    }

    @Test
    void actualizarEstado_vehiculoInexistente_noRegistraAuditoria() {
        when(vehiculoDAO.actualizarEstado(404, EstadoVehiculo.DISPONIBLE)).thenReturn(false);

        boolean ok = controller.actualizarEstado(404, EstadoVehiculo.DISPONIBLE, "operador.test");

        assertFalse(ok);
        verifyNoInteractions(auditoriaService);
    }
}
