package com.rapidexpress.dao;

import com.rapidexpress.model.AsignacionVehiculoConductor;
import java.util.List;
import java.util.Optional;

public interface AsignacionDAO {
    AsignacionVehiculoConductor crear(AsignacionVehiculoConductor asignacion);
    Optional<AsignacionVehiculoConductor> buscarActivaPorVehiculo(int idVehiculo);
    boolean finalizar(int idAsignacion);
    List<AsignacionVehiculoConductor> listarTodas();
}
