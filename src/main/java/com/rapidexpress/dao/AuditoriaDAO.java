package com.rapidexpress.dao;

import com.rapidexpress.model.RegistroAuditoria;
import java.util.List;

public interface AuditoriaDAO {
    RegistroAuditoria registrar(RegistroAuditoria registro);
    List<RegistroAuditoria> listarTodos();
}
