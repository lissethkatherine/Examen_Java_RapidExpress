package com.rapidexpress.dao;

import com.rapidexpress.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteDAO {
    Cliente crear(Cliente cliente);
    Optional<Cliente> buscarPorId(int idCliente);
    List<Cliente> listarTodos();
    List<Cliente> buscarPorNombre(String texto);
    boolean actualizar(Cliente cliente);
}
