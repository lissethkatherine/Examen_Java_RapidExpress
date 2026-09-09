package com.rapidexpress.controller;

import com.rapidexpress.dao.*;
import com.rapidexpress.model.*;
import com.rapidexpress.service.AuditoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de RutaController: la clase que concentra la logica
 * de negocio mas critica del sistema (planificacion de hojas de ruta,
 * validacion de capacidad de carga, cambios de estado en cascada al
 * iniciar/finalizar una ruta). Los DAO se simulan con Mockito para poder
 * probar las reglas de negocio sin necesitar una base de datos real.
 */
@ExtendWith(MockitoExtension.class)
class RutaControllerTest {

    @Mock private RutaDAO rutaDAO;
    @Mock private VehiculoDAO vehiculoDAO;
    @Mock private ConductorDAO conductorDAO;
    @Mock private PaqueteDAO paqueteDAO;
    @Mock private AuditoriaService auditoriaService;

    private RutaController controller;

    @BeforeEach
    void setUp() {
        controller = new RutaController(rutaDAO, vehiculoDAO, conductorDAO, paqueteDAO, auditoriaService);
    }

    private Vehiculo vehiculoDisponible(int id, double capacidadKg) {
        Vehiculo v = new Vehiculo("RXP" + id, "Chevrolet", "NPR", 2022, BigDecimal.valueOf(capacidadKg));
        v.setIdVehiculo(id);
        v.setEstado(EstadoVehiculo.DISPONIBLE);
        return v;
    }

    private Conductor conductorActivo(int id) {
        Conductor c = new Conductor("100000" + id, "Conductor Prueba " + id, "C2", "3000000000");
        c.setIdConductor(id);
        c.setEstado(EstadoConductor.ACTIVO);
        return c;
    }

    private Paquete paqueteEnBodega(int id, double pesoKg) {
        Paquete p = new Paquete("Contenido de prueba", BigDecimal.valueOf(pesoKg), "10x10x10 cm",
                "Origen", "Destino", 1, 2);
        p.setIdPaquete(id);
        p.setEstado(EstadoPaquete.EN_BODEGA);
        return p;
    }

    // ---------------------- crearHojaDeRuta ----------------------

    @Test
    void crearHojaDeRuta_conDatosValidos_creaLaRutaYActualizaPaquetes() {
        Vehiculo vehiculo = vehiculoDisponible(1, 1000);
        Conductor conductor = conductorActivo(1);
        Paquete paquete1 = paqueteEnBodega(10, 100);
        Paquete paquete2 = paqueteEnBodega(11, 150);

        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculo));
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(conductor));
        when(paqueteDAO.buscarPorId(10)).thenReturn(Optional.of(paquete1));
        when(paqueteDAO.buscarPorId(11)).thenReturn(Optional.of(paquete2));

        Ruta rutaCreada = new Ruta(1, 1, LocalDate.of(2026, 8, 20));
        rutaCreada.setIdRuta(99);
        when(rutaDAO.crear(any(Ruta.class))).thenReturn(rutaCreada);

        String resultado = controller.crearHojaDeRuta(1, 1, LocalDate.of(2026, 8, 20),
                List.of(10, 11), "operador.test");

        assertTrue(resultado.startsWith("OK"), "Se esperaba una creacion exitosa: " + resultado);
        assertTrue(resultado.contains("99"));

        // Se debe haber creado la ruta y asignado ambos paquetes
        verify(rutaDAO, times(1)).crear(any(Ruta.class));
        verify(rutaDAO, times(1)).asignarPaquete(argThat(rp -> rp.getIdPaquete() == 10));
        verify(rutaDAO, times(1)).asignarPaquete(argThat(rp -> rp.getIdPaquete() == 11));

        // Ambos paquetes deben quedar en estado "Asignado a Ruta"
        verify(paqueteDAO).actualizarEstado(10, EstadoPaquete.ASIGNADO_A_RUTA);
        verify(paqueteDAO).actualizarEstado(11, EstadoPaquete.ASIGNADO_A_RUTA);

        verify(auditoriaService).registrar(eq("operador.test"), eq("CREAR_HOJA_RUTA"), eq("rutas"),
                eq(99), anyString());
    }

    @Test
    void crearHojaDeRuta_excedeCapacidadDelVehiculo_esRechazada() {
        Vehiculo vehiculo = vehiculoDisponible(1, 100); // capacidad pequena
        Conductor conductor = conductorActivo(1);
        Paquete paqueteDemasiadoPesado = paqueteEnBodega(10, 250);

        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculo));
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(conductor));
        when(paqueteDAO.buscarPorId(10)).thenReturn(Optional.of(paqueteDemasiadoPesado));

        String resultado = controller.crearHojaDeRuta(1, 1, LocalDate.now(), List.of(10), "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("excede la capacidad"));
        // No se debe haber creado ninguna ruta ni tocado ningun paquete
        verify(rutaDAO, never()).crear(any());
        verify(paqueteDAO, never()).actualizarEstado(anyInt(), any());
    }

    @Test
    void crearHojaDeRuta_vehiculoNoDisponible_esRechazada() {
        Vehiculo vehiculoEnRuta = vehiculoDisponible(1, 1000);
        vehiculoEnRuta.setEstado(EstadoVehiculo.EN_RUTA);
        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculoEnRuta));

        String resultado = controller.crearHojaDeRuta(1, 1, LocalDate.now(), List.of(10), "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("Disponible"));
        verifyNoInteractions(rutaDAO);
    }

    @Test
    void crearHojaDeRuta_conductorNoActivo_esRechazada() {
        Vehiculo vehiculo = vehiculoDisponible(1, 1000);
        Conductor conductorInactivo = conductorActivo(1);
        conductorInactivo.setEstado(EstadoConductor.INACTIVO);

        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculo));
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(conductorInactivo));

        String resultado = controller.crearHojaDeRuta(1, 1, LocalDate.now(), List.of(10), "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("Activo"));
    }

    @Test
    void crearHojaDeRuta_paqueteQueNoEstaEnBodega_esRechazada() {
        Vehiculo vehiculo = vehiculoDisponible(1, 1000);
        Conductor conductor = conductorActivo(1);
        Paquete paqueteYaEntregado = paqueteEnBodega(10, 50);
        paqueteYaEntregado.setEstado(EstadoPaquete.ENTREGADO);

        when(vehiculoDAO.buscarPorId(1)).thenReturn(Optional.of(vehiculo));
        when(conductorDAO.buscarPorId(1)).thenReturn(Optional.of(conductor));
        when(paqueteDAO.buscarPorId(10)).thenReturn(Optional.of(paqueteYaEntregado));

        String resultado = controller.crearHojaDeRuta(1, 1, LocalDate.now(), List.of(10), "operador.test");

        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("En Bodega"));
    }

    @Test
    void crearHojaDeRuta_sinPaquetes_esRechazada() {
        String resultado = controller.crearHojaDeRuta(1, 1, LocalDate.now(), List.of(), "operador.test");
        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("al menos un paquete"));
        verifyNoInteractions(vehiculoDAO, conductorDAO, rutaDAO);
    }

    @Test
    void crearHojaDeRuta_vehiculoInexistente_esRechazada() {
        when(vehiculoDAO.buscarPorId(99)).thenReturn(Optional.empty());
        String resultado = controller.crearHojaDeRuta(99, 1, LocalDate.now(), List.of(10), "operador.test");
        assertTrue(resultado.startsWith("Error"));
        assertTrue(resultado.contains("no existe"));
    }

    // ---------------------- iniciarRuta ----------------------

    @Test
    void iniciarRuta_rutaPlanificada_actualizaVehiculoYPaquetesATransito() {
        Ruta ruta = new Ruta(1, 1, LocalDate.now());
        ruta.setIdRuta(5);
        ruta.setEstado(EstadoRuta.PLANIFICADA);
        when(rutaDAO.buscarPorId(5)).thenReturn(Optional.of(ruta));

        RutaPaquete rp1 = new RutaPaquete(5, 10, 1);
        RutaPaquete rp2 = new RutaPaquete(5, 11, 2);
        when(rutaDAO.listarPaquetesDeRuta(5)).thenReturn(List.of(rp1, rp2));

        String resultado = controller.iniciarRuta(5, "operador.test");

        assertTrue(resultado.startsWith("OK"));
        verify(rutaDAO).marcarInicio(5);
        verify(vehiculoDAO).actualizarEstado(1, EstadoVehiculo.EN_RUTA);
        verify(paqueteDAO).actualizarEstado(10, EstadoPaquete.EN_TRANSITO);
        verify(paqueteDAO).actualizarEstado(11, EstadoPaquete.EN_TRANSITO);
    }

    @Test
    void iniciarRuta_queNoEstaPlanificada_esRechazada() {
        Ruta ruta = new Ruta(1, 1, LocalDate.now());
        ruta.setIdRuta(5);
        ruta.setEstado(EstadoRuta.EN_CURSO);
        when(rutaDAO.buscarPorId(5)).thenReturn(Optional.of(ruta));

        String resultado = controller.iniciarRuta(5, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        verify(rutaDAO, never()).marcarInicio(anyInt());
        verify(vehiculoDAO, never()).actualizarEstado(anyInt(), any());
    }

    @Test
    void iniciarRuta_inexistente_esRechazada() {
        when(rutaDAO.buscarPorId(999)).thenReturn(Optional.empty());
        String resultado = controller.iniciarRuta(999, "operador.test");
        assertTrue(resultado.startsWith("Error"));
    }

    // ---------------------- finalizarRuta ----------------------

    @Test
    void finalizarRuta_rutaEnCurso_liberaElVehiculo() {
        Ruta ruta = new Ruta(3, 1, LocalDate.now());
        ruta.setIdRuta(7);
        ruta.setEstado(EstadoRuta.EN_CURSO);
        when(rutaDAO.buscarPorId(7)).thenReturn(Optional.of(ruta));

        String resultado = controller.finalizarRuta(7, "operador.test");

        assertTrue(resultado.startsWith("OK"));
        verify(rutaDAO).marcarFin(7);
        verify(vehiculoDAO).actualizarEstado(3, EstadoVehiculo.DISPONIBLE);
    }

    @Test
    void finalizarRuta_queNoEstaEnCurso_esRechazada() {
        Ruta ruta = new Ruta(3, 1, LocalDate.now());
        ruta.setIdRuta(7);
        ruta.setEstado(EstadoRuta.PLANIFICADA);
        when(rutaDAO.buscarPorId(7)).thenReturn(Optional.of(ruta));

        String resultado = controller.finalizarRuta(7, "operador.test");

        assertTrue(resultado.startsWith("Error"));
        verify(rutaDAO, never()).marcarFin(anyInt());
        verify(vehiculoDAO, never()).actualizarEstado(anyInt(), any());
    }
}
