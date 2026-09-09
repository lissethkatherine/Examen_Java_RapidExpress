package com.rapidexpress.controller;

import com.rapidexpress.dao.MantenimientoDAO;
import com.rapidexpress.dao.VehiculoDAO;
import com.rapidexpress.dao.impl.MantenimientoDAOImpl;
import com.rapidexpress.dao.impl.VehiculoDAOImpl;
import com.rapidexpress.model.EstadoVehiculo;
import com.rapidexpress.model.MantenimientoVehiculo;
import com.rapidexpress.model.Vehiculo;
import com.rapidexpress.service.AuditoriaService;

import java.util.List;
import java.util.Optional;

/** Orquesta las reglas de negocio del modulo de Gestion de Flota de Vehiculos. */
public class VehiculoController {

    private final VehiculoDAO vehiculoDAO;
    private final MantenimientoDAO mantenimientoDAO;
    private final AuditoriaService auditoriaService;

    /** Constructor de produccion: usa las implementaciones JDBC reales. */
    public VehiculoController() {
        this(new VehiculoDAOImpl(), new MantenimientoDAOImpl(), new AuditoriaService());
    }

    /** Constructor con inyeccion de dependencias, usado en pruebas unitarias con mocks. */
    public VehiculoController(VehiculoDAO vehiculoDAO, MantenimientoDAO mantenimientoDAO,
                               AuditoriaService auditoriaService) {
        this.vehiculoDAO = vehiculoDAO;
        this.mantenimientoDAO = mantenimientoDAO;
        this.auditoriaService = auditoriaService;
    }

    public Vehiculo registrarVehiculo(Vehiculo vehiculo, String usuarioOperador) {
        Vehiculo creado = vehiculoDAO.crear(vehiculo);
        auditoriaService.registrar(usuarioOperador, "CREAR_VEHICULO", "vehiculos",
                creado.getIdVehiculo(), "Registro de vehiculo con placa " + creado.getPlaca());
        return creado;
    }

    public List<Vehiculo> listarTodos() {
        return vehiculoDAO.listarTodos();
    }

    public List<Vehiculo> listarPorEstado(EstadoVehiculo estado) {
        return vehiculoDAO.listarPorEstado(estado);
    }

    public Optional<Vehiculo> buscarPorId(int idVehiculo) {
        return vehiculoDAO.buscarPorId(idVehiculo);
    }

    public boolean actualizarEstado(int idVehiculo, EstadoVehiculo nuevoEstado, String usuarioOperador) {
        boolean ok = vehiculoDAO.actualizarEstado(idVehiculo, nuevoEstado);
        if (ok) {
            auditoriaService.registrar(usuarioOperador, "CAMBIO_ESTADO_VEHICULO", "vehiculos",
                    idVehiculo, "Vehiculo cambia a estado " + nuevoEstado.getEtiqueta());
        }
        return ok;
    }

    public MantenimientoVehiculo registrarMantenimiento(MantenimientoVehiculo mantenimiento,
                                                          String usuarioOperador) {
        MantenimientoVehiculo creado = mantenimientoDAO.crear(mantenimiento);
        // Un vehiculo en mantenimiento no puede seguir "Disponible" ni "En Ruta"
        vehiculoDAO.actualizarEstado(mantenimiento.getIdVehiculo(), EstadoVehiculo.EN_MANTENIMIENTO);
        auditoriaService.registrar(usuarioOperador, "REGISTRO_MANTENIMIENTO", "mantenimientos_vehiculo",
                creado.getIdMantenimiento(), "Mantenimiento " + creado.getTipoMantenimiento() +
                        " registrado para vehiculo #" + creado.getIdVehiculo());
        return creado;
    }

    public List<MantenimientoVehiculo> historialMantenimiento(int idVehiculo) {
        return mantenimientoDAO.listarPorVehiculo(idVehiculo);
    }
}
