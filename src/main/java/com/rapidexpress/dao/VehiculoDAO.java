package com.rapidexpress.dao;

import com.rapidexpress.model.EstadoVehiculo;
import com.rapidexpress.model.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoDAO {
    Vehiculo crear(Vehiculo vehiculo);
    Optional<Vehiculo> buscarPorId(int idVehiculo);
    List<Vehiculo> listarTodos();
    List<Vehiculo> listarPorEstado(EstadoVehiculo estado);
    boolean actualizar(Vehiculo vehiculo);
    boolean actualizarEstado(int idVehiculo, EstadoVehiculo estado);
}
