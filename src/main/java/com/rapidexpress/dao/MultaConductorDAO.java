package com.rapidexpress.dao;

import com.rapidexpress.model.MultaConductor;
import java.util.List;
import java.util.Optional;

public interface MultaConductorDAO {
    MultaConductor crear(MultaConductor multa);
    Optional<MultaConductor> buscarPorId(int idMulta);
    List<MultaConductor> listarConSaldoPendiente();
    List<MultaConductor> listarPorConductor(int idConductor);
    boolean actualizarSaldo(int idMulta, java.math.BigDecimal nuevoSaldo);
}
