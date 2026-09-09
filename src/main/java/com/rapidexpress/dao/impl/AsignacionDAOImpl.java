package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.AsignacionDAO;
import com.rapidexpress.model.AsignacionVehiculoConductor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsignacionDAOImpl implements AsignacionDAO {

    @Override
    public AsignacionVehiculoConductor crear(AsignacionVehiculoConductor a) {
        String sql = "INSERT INTO asignaciones_vehiculo_conductor (id_vehiculo, id_conductor, " +
                "estado) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdVehiculo());
            ps.setInt(2, a.getIdConductor());
            ps.setString(3, a.getEstado());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) a.setIdAsignacion(keys.getInt(1));
            }
            return a;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear asignacion: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AsignacionVehiculoConductor> buscarActivaPorVehiculo(int idVehiculo) {
        String sql = "SELECT * FROM asignaciones_vehiculo_conductor " +
                "WHERE id_vehiculo = ? AND estado = 'Activa' LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar asignacion activa: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean finalizar(int idAsignacion) {
        String sql = "UPDATE asignaciones_vehiculo_conductor SET estado = 'Finalizada', " +
                "fecha_liberacion = NOW() WHERE id_asignacion = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al finalizar asignacion: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AsignacionVehiculoConductor> listarTodas() {
        String sql = "SELECT * FROM asignaciones_vehiculo_conductor ORDER BY id_asignacion DESC";
        List<AsignacionVehiculoConductor> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar asignaciones: " + e.getMessage(), e);
        }
    }

    private AsignacionVehiculoConductor mapear(ResultSet rs) throws SQLException {
        AsignacionVehiculoConductor a = new AsignacionVehiculoConductor();
        a.setIdAsignacion(rs.getInt("id_asignacion"));
        a.setIdVehiculo(rs.getInt("id_vehiculo"));
        a.setIdConductor(rs.getInt("id_conductor"));
        Timestamp fa = rs.getTimestamp("fecha_asignacion");
        if (fa != null) a.setFechaAsignacion(fa.toLocalDateTime());
        Timestamp fl = rs.getTimestamp("fecha_liberacion");
        if (fl != null) a.setFechaLiberacion(fl.toLocalDateTime());
        a.setEstado(rs.getString("estado"));
        return a;
    }
}
