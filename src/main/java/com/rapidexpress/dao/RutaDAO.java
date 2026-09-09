package com.rapidexpress.dao;

import com.rapidexpress.model.DetalleEntregaPaquete;
import com.rapidexpress.model.EstadoRuta;
import com.rapidexpress.model.Ruta;
import com.rapidexpress.model.RutaPaquete;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RutaDAO {
    Ruta crear(Ruta ruta);
    Optional<Ruta> buscarPorId(int idRuta);
    List<Ruta> listarTodas();
    List<Ruta> listarPorEstado(EstadoRuta estado);
    List<Ruta> listarPorVehiculo(int idVehiculo);
    List<Ruta> listarPorConductorYFechas(int idConductor, LocalDate desde, LocalDate hasta);

    /**
     * Reporte detallado (paquete por paquete) de todas las entregas
     * realizadas por un conductor dentro de un rango de fechas. Cada fila
     * corresponde a un paquete individual, no a una ruta completa.
     */
    List<DetalleEntregaPaquete> listarDetalleEntregasPorConductorYFechas(int idConductor,
                                                                          LocalDate desde, LocalDate hasta);
    boolean actualizarEstado(int idRuta, EstadoRuta estado);
    boolean marcarInicio(int idRuta);
    boolean marcarFin(int idRuta);

    RutaPaquete asignarPaquete(RutaPaquete rutaPaquete);
    List<RutaPaquete> listarPaquetesDeRuta(int idRuta);
    boolean actualizarEstadoEntrega(int idRutaPaquete, String estadoEntrega);
}
