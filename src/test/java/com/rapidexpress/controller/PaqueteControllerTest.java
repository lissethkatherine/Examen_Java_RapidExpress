package com.rapidexpress.controller;

import com.rapidexpress.dao.ClienteDAO;
import com.rapidexpress.dao.PaqueteDAO;
import com.rapidexpress.model.EstadoPaquete;
import com.rapidexpress.model.Paquete;
import com.rapidexpress.service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaqueteControllerTest {

    @Mock private PaqueteDAO paqueteDAO;
    @Mock private ClienteDAO clienteDAO;
    @Mock private AuditoriaService auditoriaService;

    private PaqueteController controller;

    @BeforeEach
    void setUp() {
        controller = new PaqueteController(paqueteDAO, clienteDAO, auditoriaService);
    }

    @Test
    void registrarPaquete_delegaEnElDaoYRegistraAuditoriaConElCodigoGenerado() {
        Paquete paquete = new Paquete("Libros", BigDecimal.valueOf(3.2), "20x15x10 cm",
                "Origen X", "Destino Y", 1, 2);
        Paquete creado = new Paquete("Libros", BigDecimal.valueOf(3.2), "20x15x10 cm",
                "Origen X", "Destino Y", 1, 2);
        creado.setIdPaquete(15);
        creado.setCodigoSeguimiento("RX-2026-000015");
        when(paqueteDAO.crear(paquete)).thenReturn(creado);

        Paquete resultado = controller.registrarPaquete(paquete, "operador.test");

        assertEquals("RX-2026-000015", resultado.getCodigoSeguimiento());
        verify(auditoriaService).registrar(eq("operador.test"), eq("CREAR_PAQUETE"), eq("paquetes"),
                eq(15), anyString());
    }

    @Test
    void actualizarEstado_exitoso_registraAuditoriaConElNuevoEstado() {
        when(paqueteDAO.actualizarEstado(15, EstadoPaquete.ENTREGADO)).thenReturn(true);

        boolean ok = controller.actualizarEstado(15, EstadoPaquete.ENTREGADO, "operador.test");

        assertTrue(ok);
        verify(auditoriaService).registrar(eq("operador.test"), eq("CAMBIO_ESTADO_PAQUETE"),
                eq("paquetes"), eq(15), contains("Entregado"));
    }

    @Test
    void actualizarEstado_paqueteInexistente_noRegistraAuditoria() {
        when(paqueteDAO.actualizarEstado(999, EstadoPaquete.DEVUELTO)).thenReturn(false);

        boolean ok = controller.actualizarEstado(999, EstadoPaquete.DEVUELTO, "operador.test");

        assertFalse(ok);
        verifyNoInteractions(auditoriaService);
    }
}
