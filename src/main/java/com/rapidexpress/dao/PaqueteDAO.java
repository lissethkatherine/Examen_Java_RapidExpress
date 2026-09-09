package com.rapidexpress.dao;

import com.rapidexpress.model.EstadoPaquete;
import com.rapidexpress.model.Paquete;
import java.util.List;
import java.util.Optional;

public interface PaqueteDAO {
    Paquete crear(Paquete paquete);
    Optional<Paquete> buscarPorId(int idPaquete);
    Optional<Paquete> buscarPorCodigo(String codigoSeguimiento);
    List<Paquete> listarTodos();
    List<Paquete> listarPorEstado(EstadoPaquete estado);
    boolean actualizarEstado(int idPaquete, EstadoPaquete estado);
}
