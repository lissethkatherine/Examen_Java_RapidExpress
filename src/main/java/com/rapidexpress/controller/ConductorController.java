package com.rapidexpress.controller;

import com.rapidexpress.dao.AsignacionDAO;
import com.rapidexpress.dao.ConductorDAO;
import com.rapidexpress.dao.VehiculoDAO;
import com.rapidexpress.dao.impl.AsignacionDAOImpl;
import com.rapidexpress.dao.impl.ConductorDAOImpl;
import com.rapidexpress.dao.impl.VehiculoDAOImpl;
import com.rapidexpress.model.*;
import com.rapidexpress.service.AuditoriaService;

import java.util.List;
import java.util.Optional;

/** Orquesta las reglas de negocio del modulo de Gestion de Personal (Conductores). */
public class ConductorController {

    private final ConductorDAO conductorDAO;
    private final VehiculoDAO vehiculoDAO;
    private final AsignacionDAO asignacionDAO;
    private final AuditoriaService auditoriaService;

    /** Constructor de produccion: usa las implementaciones JDBC reales. */
    public ConductorController() {
        this(new ConductorDAOImpl(), new VehiculoDAOImpl(), new AsignacionDAOImpl(), new AuditoriaService());
    }

    /** Constructor con inyeccion de dependencias, usado en pruebas unitarias con mocks. */
    public ConductorController(ConductorDAO conductorDAO, VehiculoDAO vehiculoDAO, AsignacionDAO asignacionDAO,
                                AuditoriaService auditoriaService) {
        this.conductorDAO = conductorDAO;
        this.vehiculoDAO = vehiculoDAO;
        this.asignacionDAO = asignacionDAO;
        this.auditoriaService = auditoriaService;
    }

    public Conductor registrarConductor(Conductor conductor, String usuarioOperador) {
        Conductor creado = conductorDAO.crear(conductor);
        auditoriaService.registrar(usuarioOperador, "CREAR_CONDUCTOR", "conductores",
                creado.getIdConductor(), "Registro de conductor " + creado.getNombreCompleto());
        return creado;
    }

    public List<Conductor> listarTodos() {
        return conductorDAO.listarTodos();
    }

    public List<Conductor> listarPorEstado(EstadoConductor estado) {
        return conductorDAO.listarPorEstado(estado);
    }

    public Optional<Conductor> buscarPorId(int idConductor) {
        return conductorDAO.buscarPorId(idConductor);
    }

    public boolean actualizarEstado(int idConductor, EstadoConductor nuevoEstado, String usuarioOperador) {
        boolean ok = conductorDAO.actualizarEstado(idConductor, nuevoEstado);
        if (ok) {
            auditoriaService.registrar(usuarioOperador, "CAMBIO_ESTADO_CONDUCTOR", "conductores",
                    idConductor, "Conductor cambia a estado " + nuevoEstado.getEtiqueta());
        }
        return ok;
    }

    /**
     * Asigna un conductor a un vehiculo Disponible.
     * Reglas de negocio aplicadas:
     *  - El vehiculo debe existir y estar en estado "Disponible".
     *  - El conductor debe existir y estar en estado "Activo".
     *  - Un conductor no puede tener mas de una asignacion activa simultanea.
     */
    public String asignarConductorAVehiculo(int idConductor, int idVehiculo, String usuarioOperador) {
        Optional<Vehiculo> vehiculoOpt = vehiculoDAO.buscarPorId(idVehiculo);
        if (vehiculoOpt.isEmpty()) {
            return "Error: el vehiculo #" + idVehiculo + " no existe.";
        }
        if (vehiculoOpt.get().getEstado() != EstadoVehiculo.DISPONIBLE) {
            return "Error: el vehiculo #" + idVehiculo + " no esta Disponible (estado actual: "
                    + vehiculoOpt.get().getEstado() + ").";
        }

        Optional<Conductor> conductorOpt = conductorDAO.buscarPorId(idConductor);
        if (conductorOpt.isEmpty()) {
            return "Error: el conductor #" + idConductor + " no existe.";
        }
        if (conductorOpt.get().getEstado() != EstadoConductor.ACTIVO) {
            return "Error: el conductor #" + idConductor + " no esta Activo (estado actual: "
                    + conductorOpt.get().getEstado() + ").";
        }

        if (conductorDAO.tieneAsignacionActiva(idConductor)) {
            return "Error: el conductor #" + idConductor + " ya tiene una asignacion activa a otro vehiculo.";
        }

        AsignacionVehiculoConductor asignacion = new AsignacionVehiculoConductor(idVehiculo, idConductor);
        asignacionDAO.crear(asignacion);

        auditoriaService.registrar(usuarioOperador, "ASIGNAR_CONDUCTOR", "asignaciones_vehiculo_conductor",
                asignacion.getIdAsignacion(), "Conductor #" + idConductor + " asignado a vehiculo #" + idVehiculo);

        return "OK: conductor #" + idConductor + " asignado correctamente al vehiculo #" + idVehiculo + ".";
    }

    public List<AsignacionVehiculoConductor> listarAsignaciones() {
        return asignacionDAO.listarTodas();
    }
}
