package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.VehiculoDAO;
import com.rapidexpress.model.EstadoVehiculo;
import com.rapidexpress.model.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculoDAOImpl implements VehiculoDAO {

    @Override
    public Vehiculo crear(Vehiculo v) {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, anio_fabricacion, " +
                "capacidad_carga_kg, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnioFabricacion());
            ps.setBigDecimal(5, v.getCapacidadCargaKg());
            ps.setString(6, v.getEstado().getEtiqueta());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) v.setIdVehiculo(keys.getInt(1));
            }
            return v;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear vehiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorId(int idVehiculo) {
        String sql = "SELECT * FROM vehiculos WHERE id_vehiculo = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> listarTodos() {
        String sql = "SELECT * FROM vehiculos ORDER BY id_vehiculo";
        List<Vehiculo> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar vehiculos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vehiculo> listarPorEstado(EstadoVehiculo estado) {
        String sql = "SELECT * FROM vehiculos WHERE estado = ? ORDER BY id_vehiculo";
        List<Vehiculo> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar vehiculos por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Vehiculo v) {
        String sql = "UPDATE vehiculos SET marca = ?, modelo = ?, anio_fabricacion = ?, " +
                "capacidad_carga_kg = ? WHERE id_vehiculo = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModelo());
            ps.setInt(3, v.getAnioFabricacion());
            ps.setBigDecimal(4, v.getCapacidadCargaKg());
            ps.setInt(5, v.getIdVehiculo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar vehiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstado(int idVehiculo, EstadoVehiculo estado) {
        String sql = "UPDATE vehiculos SET estado = ? WHERE id_vehiculo = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            ps.setInt(2, idVehiculo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de vehiculo: " + e.getMessage(), e);
        }
    }

    private Vehiculo mapear(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setIdVehiculo(rs.getInt("id_vehiculo"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAnioFabricacion(rs.getInt("anio_fabricacion"));
        v.setCapacidadCargaKg(rs.getBigDecimal("capacidad_carga_kg"));
        v.setEstado(EstadoVehiculo.fromEtiqueta(rs.getString("estado")));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) v.setFechaRegistro(ts.toLocalDateTime());
        return v;
    }
}
