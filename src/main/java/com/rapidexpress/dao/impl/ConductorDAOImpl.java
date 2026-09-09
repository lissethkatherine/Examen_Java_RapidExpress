package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.ConductorDAO;
import com.rapidexpress.model.Conductor;
import com.rapidexpress.model.EstadoConductor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConductorDAOImpl implements ConductorDAO {

    @Override
    public Conductor crear(Conductor c) {
        String sql = "INSERT INTO conductores (numero_identificacion, nombre_completo, " +
                "tipo_licencia, numero_contacto, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNumeroIdentificacion());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getTipoLicencia());
            ps.setString(4, c.getNumeroContacto());
            ps.setString(5, c.getEstado().getEtiqueta());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setIdConductor(keys.getInt(1));
            }
            return c;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear conductor: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Conductor> buscarPorId(int idConductor) {
        String sql = "SELECT * FROM conductores WHERE id_conductor = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConductor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar conductor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Conductor> listarTodos() {
        String sql = "SELECT * FROM conductores ORDER BY id_conductor";
        List<Conductor> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar conductores: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Conductor> listarPorEstado(EstadoConductor estado) {
        String sql = "SELECT * FROM conductores WHERE estado = ? ORDER BY id_conductor";
        List<Conductor> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar conductores por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Conductor c) {
        String sql = "UPDATE conductores SET nombre_completo = ?, tipo_licencia = ?, " +
                "numero_contacto = ? WHERE id_conductor = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCompleto());
            ps.setString(2, c.getTipoLicencia());
            ps.setString(3, c.getNumeroContacto());
            ps.setInt(4, c.getIdConductor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar conductor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstado(int idConductor, EstadoConductor estado) {
        String sql = "UPDATE conductores SET estado = ? WHERE id_conductor = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            ps.setInt(2, idConductor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de conductor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean tieneAsignacionActiva(int idConductor) {
        String sql = "SELECT COUNT(*) FROM asignaciones_vehiculo_conductor " +
                "WHERE id_conductor = ? AND estado = 'Activa'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConductor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar asignacion activa: " + e.getMessage(), e);
        }
    }

    private Conductor mapear(ResultSet rs) throws SQLException {
        Conductor c = new Conductor();
        c.setIdConductor(rs.getInt("id_conductor"));
        c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setTipoLicencia(rs.getString("tipo_licencia"));
        c.setNumeroContacto(rs.getString("numero_contacto"));
        c.setEstado(EstadoConductor.fromEtiqueta(rs.getString("estado")));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) c.setFechaRegistro(ts.toLocalDateTime());
        return c;
    }
}
