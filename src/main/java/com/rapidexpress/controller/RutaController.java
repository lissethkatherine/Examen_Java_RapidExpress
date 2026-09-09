package com.rapidexpress.controller;

import com.rapidexpress.dao.*;
import com.rapidexpress.dao.impl.*;
import com.rapidexpress.model.*;
import com.rapidexpress.service.AuditoriaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Orquesta las reglas de negocio del modulo de Planificacion y Seguimiento
 * de Rutas: creacion de hojas de ruta, validacion de capacidad de carga,
 * inicio y finalizacion de rutas con los cambios de estado en cascada
 * exigidos por el enunciado.
 */
public class RutaController {

    private final RutaDAO rutaDAO;
    private final VehiculoDAO vehiculoDAO;
    private final ConductorDAO conductorDAO;
    private final PaqueteDAO paqueteDAO;
    private final AuditoriaService auditoriaService;

    /** Constructor de produccion: usa las implementaciones JDBC reales. */
    public RutaController() {
        this(new RutaDAOImpl(), new VehiculoDAOImpl(), new ConductorDAOImpl(), new PaqueteDAOImpl(),
                new AuditoriaService());
    }

    /** Constructor con inyeccion de dependencias, usado en pruebas unitarias con mocks. */
    public RutaController(RutaDAO rutaDAO, VehiculoDAO vehiculoDAO, ConductorDAO conductorDAO,
                           PaqueteDAO paqueteDAO, AuditoriaService auditoriaService) {
        this.rutaDAO = rutaDAO;
        this.vehiculoDAO = vehiculoDAO;
        this.conductorDAO = conductorDAO;
        this.paqueteDAO = paqueteDAO;
        this.auditoriaService = auditoriaService;
    }

    /**
     * Crea una hoja de ruta diaria asignando un conjunto de paquetes
     * (que deben estar "En Bodega") a un vehiculo y su conductor.
     * No permite exceder la capacidad de carga del vehiculo.
     */
    public String crearHojaDeRuta(int idVehiculo, int idConductor, LocalDate fecha,
                                   List<Integer> idsPaquetes, String usuarioOperador) {
        Optional<Vehiculo> vehiculoOpt = vehiculoDAO.buscarPorId(idVehiculo);
        if (vehiculoOpt.isEmpty()) {
            return "Error: el vehiculo #" + idVehiculo + " no existe.";
        }
        Vehiculo vehiculo = vehiculoOpt.get();
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            return "Error: el vehiculo #" + idVehiculo + " no esta Disponible.";
        }

        Optional<Conductor> conductorOpt = conductorDAO.buscarPorId(idConductor);
        if (conductorOpt.isEmpty()) {
            return "Error: el conductor #" + idConductor + " no existe.";
        }
        if (conductorOpt.get().getEstado() != EstadoConductor.ACTIVO) {
            return "Error: el conductor #" + idConductor + " no esta Activo.";
        }

        if (idsPaquetes == null || idsPaquetes.isEmpty()) {
            return "Error: debe incluir al menos un paquete en la hoja de ruta.";
        }

        // Validar que todos los paquetes existan, esten "En Bodega" y no excedan la capacidad
        List<Paquete> paquetesValidados = new ArrayList<>();
        BigDecimal cargaTotal = BigDecimal.ZERO;
        for (Integer idPaquete : idsPaquetes) {
            Optional<Paquete> pOpt = paqueteDAO.buscarPorId(idPaquete);
            if (pOpt.isEmpty()) {
                return "Error: el paquete #" + idPaquete + " no existe.";
            }
            Paquete p = pOpt.get();
            if (p.getEstado() != EstadoPaquete.EN_BODEGA) {
                return "Error: el paquete #" + idPaquete + " no esta 'En Bodega' (estado actual: "
                        + p.getEstado() + ").";
            }
            cargaTotal = cargaTotal.add(p.getPesoKg());
            paquetesValidados.add(p);
        }

        if (cargaTotal.compareTo(vehiculo.getCapacidadCargaKg()) > 0) {
            return String.format("Error: la carga total (%s kg) excede la capacidad del vehiculo (%s kg).",
                    cargaTotal, vehiculo.getCapacidadCargaKg());
        }

        // Crear la ruta en estado "Planificada"
        Ruta ruta = new Ruta(idVehiculo, idConductor, fecha);
        ruta.setCargaTotalKg(cargaTotal);
        Ruta creada = rutaDAO.crear(ruta);

        // Vincular paquetes a la ruta y marcarlos como "Asignado a Ruta"
        int orden = 1;
        for (Paquete p : paquetesValidados) {
            RutaPaquete rp = new RutaPaquete(creada.getIdRuta(), p.getIdPaquete(), orden++);
            rutaDAO.asignarPaquete(rp);
            paqueteDAO.actualizarEstado(p.getIdPaquete(), EstadoPaquete.ASIGNADO_A_RUTA);
        }

        auditoriaService.registrar(usuarioOperador, "CREAR_HOJA_RUTA", "rutas", creada.getIdRuta(),
                String.format("Hoja de ruta creada con %d paquetes, carga total %s kg",
                        paquetesValidados.size(), cargaTotal));

        return "OK: hoja de ruta #" + creada.getIdRuta() + " creada con " + paquetesValidados.size()
                + " paquete(s). Use la opcion 'Iniciar ruta' para comenzar la distribucion.";
    }

    /**
     * Inicia una ruta planificada. Cambia el estado del vehiculo y del
     * conductor a "En Ruta" y el de los paquetes asociados a "En Transito".
     */
    public String iniciarRuta(int idRuta, String usuarioOperador) {
        Optional<Ruta> rutaOpt = rutaDAO.buscarPorId(idRuta);
        if (rutaOpt.isEmpty()) {
            return "Error: la ruta #" + idRuta + " no existe.";
        }
        Ruta ruta = rutaOpt.get();
        if (ruta.getEstado() != EstadoRuta.PLANIFICADA) {
            return "Error: solo se pueden iniciar rutas en estado Planificada (estado actual: "
                    + ruta.getEstado() + ").";
        }

        rutaDAO.marcarInicio(idRuta);
        vehiculoDAO.actualizarEstado(ruta.getIdVehiculo(), EstadoVehiculo.EN_RUTA);
        // Nota: el conductor no tiene una columna de estado "En Ruta" explicita en el
        // enunciado del modulo de personal (Activo/De Vacaciones/Inactivo); se mantiene
        // Activo y su ocupacion se refleja mediante la asignacion activa al vehiculo.

        for (RutaPaquete rp : rutaDAO.listarPaquetesDeRuta(idRuta)) {
            paqueteDAO.actualizarEstado(rp.getIdPaquete(), EstadoPaquete.EN_TRANSITO);
        }

        auditoriaService.registrar(usuarioOperador, "INICIO_RUTA", "rutas", idRuta,
                "Ruta iniciada; vehiculo #" + ruta.getIdVehiculo() + " y paquetes asociados en transito");

        return "OK: ruta #" + idRuta + " iniciada. Vehiculo en Ruta, paquetes En Transito.";
    }

    /** Actualiza el estado de entrega de un paquete dentro de una ruta activa (ej. a Entregado). */
    public String actualizarEntregaPaquete(int idRuta, int idPaquete, String nuevoEstadoEntrega,
                                            String usuarioOperador) {
        List<RutaPaquete> paquetesRuta = rutaDAO.listarPaquetesDeRuta(idRuta);
        RutaPaquete objetivo = paquetesRuta.stream()
                .filter(rp -> rp.getIdPaquete() == idPaquete)
                .findFirst()
                .orElse(null);
        if (objetivo == null) {
            return "Error: el paquete #" + idPaquete + " no pertenece a la ruta #" + idRuta + ".";
        }

        rutaDAO.actualizarEstadoEntrega(objetivo.getIdRutaPaquete(), nuevoEstadoEntrega);
        EstadoPaquete nuevoEstadoPaquete = "Entregado".equals(nuevoEstadoEntrega)
                ? EstadoPaquete.ENTREGADO
                : "Devuelto".equals(nuevoEstadoEntrega) ? EstadoPaquete.DEVUELTO : EstadoPaquete.EN_TRANSITO;
        paqueteDAO.actualizarEstado(idPaquete, nuevoEstadoPaquete);

        auditoriaService.registrar(usuarioOperador, "ACTUALIZAR_ENTREGA", "ruta_paquetes",
                objetivo.getIdRutaPaquete(), "Paquete #" + idPaquete + " en ruta #" + idRuta +
                        " actualizado a " + nuevoEstadoEntrega);

        return "OK: paquete #" + idPaquete + " actualizado a " + nuevoEstadoEntrega + ".";
    }

    /**
     * Finaliza una ruta en curso. Libera el vehiculo (vuelve a Disponible)
     * y cierra la hoja de ruta.
     */
    public String finalizarRuta(int idRuta, String usuarioOperador) {
        Optional<Ruta> rutaOpt = rutaDAO.buscarPorId(idRuta);
        if (rutaOpt.isEmpty()) {
            return "Error: la ruta #" + idRuta + " no existe.";
        }
        Ruta ruta = rutaOpt.get();
        if (ruta.getEstado() != EstadoRuta.EN_CURSO) {
            return "Error: solo se pueden finalizar rutas En Curso (estado actual: " + ruta.getEstado() + ").";
        }

        rutaDAO.marcarFin(idRuta);
        vehiculoDAO.actualizarEstado(ruta.getIdVehiculo(), EstadoVehiculo.DISPONIBLE);

        auditoriaService.registrar(usuarioOperador, "FIN_RUTA", "rutas", idRuta,
                "Ruta finalizada; vehiculo #" + ruta.getIdVehiculo() + " liberado a Disponible");

        return "OK: ruta #" + idRuta + " finalizada. Vehiculo liberado.";
    }

    public List<Ruta> listarTodas() {
        return rutaDAO.listarTodas();
    }

    public List<Ruta> listarPorEstado(EstadoRuta estado) {
        return rutaDAO.listarPorEstado(estado);
    }

    public List<Ruta> listarPorVehiculo(int idVehiculo) {
        return rutaDAO.listarPorVehiculo(idVehiculo);
    }

    public List<Ruta> reporteRutasPorConductorYFechas(int idConductor, LocalDate desde, LocalDate hasta) {
        return rutaDAO.listarPorConductorYFechas(idConductor, desde, hasta);
    }

    /**
     * Reporte detallado de entregas de un conductor: una fila por cada
     * paquete transportado en el rango de fechas indicado (no por ruta).
     */
    public List<DetalleEntregaPaquete> reporteDetalladoEntregasPorConductor(int idConductor,
                                                                             LocalDate desde, LocalDate hasta) {
        return rutaDAO.listarDetalleEntregasPorConductorYFechas(idConductor, desde, hasta);
    }

    public List<RutaPaquete> listarPaquetesDeRuta(int idRuta) {
        return rutaDAO.listarPaquetesDeRuta(idRuta);
    }

    public Optional<Ruta> buscarPorId(int idRuta) {
        return rutaDAO.buscarPorId(idRuta);
    }
}
