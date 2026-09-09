package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.ClienteDAO;
import com.rapidexpress.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente crear(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre_completo, tipo_documento, numero_documento, " +
                "telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getTipoDocumento());
            ps.setString(3, cliente.getNumeroDocumento());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getDireccion());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setIdCliente(keys.getInt(1));
                }
            }
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(int idCliente) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM clientes ORDER BY id_cliente";
        List<Cliente> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> buscarPorNombre(String texto) {
        String sql = "SELECT * FROM clientes WHERE nombre_completo LIKE ? ORDER BY nombre_completo";
        List<Cliente> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre_completo = ?, telefono = ?, email = ?, direccion = ? " +
                "WHERE id_cliente = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setTipoDocumento(rs.getString("tipo_documento"));
        c.setNumeroDocumento(rs.getString("numero_documento"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) c.setFechaRegistro(ts.toLocalDateTime());
        return c;
    }
}
