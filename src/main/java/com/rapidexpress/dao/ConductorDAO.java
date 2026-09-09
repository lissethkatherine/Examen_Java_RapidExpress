package com.rapidexpress.dao;

import com.rapidexpress.model.Conductor;
import com.rapidexpress.model.EstadoConductor;
import java.util.List;
import java.util.Optional;

public interface ConductorDAO {
    Conductor crear(Conductor conductor);
    Optional<Conductor> buscarPorId(int idConductor);
    List<Conductor> listarTodos();
    List<Conductor> listarPorEstado(EstadoConductor estado);
    boolean actualizar(Conductor conductor);
    boolean actualizarEstado(int idConductor, EstadoConductor estado);
    /** Indica si el conductor tiene una asignacion de vehiculo actualmente activa. */
    boolean tieneAsignacionActiva(int idConductor);
}
