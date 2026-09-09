package com.rapidexpress.dao;

import com.rapidexpress.model.MantenimientoVehiculo;
import java.util.List;

public interface MantenimientoDAO {
    MantenimientoVehiculo crear(MantenimientoVehiculo mantenimiento);
    List<MantenimientoVehiculo> listarPorVehiculo(int idVehiculo);
}
