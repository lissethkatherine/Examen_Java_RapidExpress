package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.MantenimientoDAO;
import com.rapidexpress.model.MantenimientoVehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDAOImpl implements MantenimientoDAO {

    @Override
    public MantenimientoVehiculo crear(MantenimientoVehiculo m) {
        String sql = "INSERT INTO mantenimientos_vehiculo (id_vehiculo, tipo_mantenimiento, " +
                "fecha_mantenimiento, descripcion, costo, taller) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getIdVehiculo());
            ps.setString(2, m.getTipoMantenimiento());
            ps.setDate(3, Date.valueOf(m.getFechaMantenimiento()));
            ps.setString(4, m.getDescripcion());
            ps.setBigDecimal(5, m.getCosto());
            ps.setString(6, m.getTaller());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setIdMantenimiento(keys.getInt(1));
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar mantenimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MantenimientoVehiculo> listarPorVehiculo(int idVehiculo) {
        String sql = "SELECT * FROM mantenimientos_vehiculo WHERE id_vehiculo = ? " +
                "ORDER BY fecha_mantenimiento DESC";
        List<MantenimientoVehiculo> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MantenimientoVehiculo m = new MantenimientoVehiculo();
                    m.setIdMantenimiento(rs.getInt("id_mantenimiento"));
                    m.setIdVehiculo(rs.getInt("id_vehiculo"));
                    m.setTipoMantenimiento(rs.getString("tipo_mantenimiento"));
                    m.setFechaMantenimiento(rs.getDate("fecha_mantenimiento").toLocalDate());
                    m.setDescripcion(rs.getString("descripcion"));
                    m.setCosto(rs.getBigDecimal("costo"));
                    m.setTaller(rs.getString("taller"));
                    Timestamp ts = rs.getTimestamp("fecha_registro");
                    if (ts != null) m.setFechaRegistro(ts.toLocalDateTime());
                    resultado.add(m);
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar mantenimientos: " + e.getMessage(), e);
        }
    }
}
