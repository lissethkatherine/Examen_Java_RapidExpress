package com.rapidexpress.controller;

import com.rapidexpress.dao.ClienteDAO;
import com.rapidexpress.dao.PaqueteDAO;
import com.rapidexpress.dao.impl.ClienteDAOImpl;
import com.rapidexpress.dao.impl.PaqueteDAOImpl;
import com.rapidexpress.model.Cliente;
import com.rapidexpress.model.EstadoPaquete;
import com.rapidexpress.model.Paquete;
import com.rapidexpress.service.AuditoriaService;

import java.util.List;
import java.util.Optional;

/** Orquesta las reglas de negocio del modulo de Gestion de Paquetes y Envios. */
public class PaqueteController {

    private final PaqueteDAO paqueteDAO;
    private final ClienteDAO clienteDAO;
    private final AuditoriaService auditoriaService;

    /** Constructor de produccion: usa las implementaciones JDBC reales. */
    public PaqueteController() {
        this(new PaqueteDAOImpl(), new ClienteDAOImpl(), new AuditoriaService());
    }

    /** Constructor con inyeccion de dependencias, usado en pruebas unitarias con mocks. */
    public PaqueteController(PaqueteDAO paqueteDAO, ClienteDAO clienteDAO, AuditoriaService auditoriaService) {
        this.paqueteDAO = paqueteDAO;
        this.clienteDAO = clienteDAO;
        this.auditoriaService = auditoriaService;
    }

    public Cliente registrarCliente(Cliente cliente, String usuarioOperador) {
        Cliente creado = clienteDAO.crear(cliente);
        auditoriaService.registrar(usuarioOperador, "CREAR_CLIENTE", "clientes",
                creado.getIdCliente(), "Registro de cliente " + creado.getNombreCompleto());
        return creado;
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    public List<Cliente> buscarClientesPorNombre(String texto) {
        return clienteDAO.buscarPorNombre(texto);
    }

    public Optional<Cliente> buscarClientePorId(int idCliente) {
        return clienteDAO.buscarPorId(idCliente);
    }

    public Paquete registrarPaquete(Paquete paquete, String usuarioOperador) {
        Paquete creado = paqueteDAO.crear(paquete);
        auditoriaService.registrar(usuarioOperador, "CREAR_PAQUETE", "paquetes",
                creado.getIdPaquete(), "Registro de paquete con codigo " + creado.getCodigoSeguimiento());
        return creado;
    }

    public List<Paquete> listarTodos() {
        return paqueteDAO.listarTodos();
    }

    public List<Paquete> listarPorEstado(EstadoPaquete estado) {
        return paqueteDAO.listarPorEstado(estado);
    }

    public Optional<Paquete> buscarPorId(int idPaquete) {
        return paqueteDAO.buscarPorId(idPaquete);
    }

    public Optional<Paquete> buscarPorCodigo(String codigo) {
        return paqueteDAO.buscarPorCodigo(codigo);
    }

    public boolean actualizarEstado(int idPaquete, EstadoPaquete nuevoEstado, String usuarioOperador) {
        boolean ok = paqueteDAO.actualizarEstado(idPaquete, nuevoEstado);
        if (ok) {
            auditoriaService.registrar(usuarioOperador, "CAMBIO_ESTADO_PAQUETE", "paquetes",
                    idPaquete, "Paquete cambia a estado " + nuevoEstado.getEtiqueta());
        }
        return ok;
    }
}
