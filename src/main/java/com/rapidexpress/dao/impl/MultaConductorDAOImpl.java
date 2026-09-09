package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.MultaConductorDAO;
import com.rapidexpress.model.EstadoMulta;
import com.rapidexpress.model.MultaConductor;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MultaConductorDAOImpl implements MultaConductorDAO {

    @Override
    public MultaConductor crear(MultaConductor m) {
        String sql = "INSERT INTO multas_conductor (id_conductor, motivo, monto_original, " +
                "saldo_pendiente, fecha_multa, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getIdConductor());
            ps.setString(2, m.getMotivo());
            ps.setBigDecimal(3, m.getMontoOriginal());
            ps.setBigDecimal(4, m.getSaldoPendiente());
            ps.setDate(5, Date.valueOf(m.getFechaMulta()));
            ps.setString(6, m.getEstado().getEtiqueta());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setIdMulta(keys.getInt(1));
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear multa: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<MultaConductor> buscarPorId(int idMulta) {
        String sql = "SELECT * FROM multas_conductor WHERE id_multa = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMulta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar multa: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MultaConductor> listarConSaldoPendiente() {
        String sql = "SELECT * FROM multas_conductor WHERE saldo_pendiente > 0 ORDER BY saldo_pendiente DESC";
        List<MultaConductor> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar multas pendientes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MultaConductor> listarPorConductor(int idConductor) {
        String sql = "SELECT * FROM multas_conductor WHERE id_conductor = ? ORDER BY fecha_multa DESC";
        List<MultaConductor> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConductor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar multas por conductor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarSaldo(int idMulta, BigDecimal nuevoSaldo) {
        // Tal como sugiere el enunciado: UPDATE ... SET saldo = saldo - ? WHERE id = ?
        // Aqui se pasa el saldo ya calculado (mas simple y explicito de leer).
        String estado = nuevoSaldo.compareTo(BigDecimal.ZERO) <= 0 ? "Pagada" : "Pendiente";
        String sql = "UPDATE multas_conductor SET saldo_pendiente = ?, fecha_ultimo_abono = NOW(), " +
                "estado = ? WHERE id_multa = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoSaldo);
            ps.setString(2, estado);
            ps.setInt(3, idMulta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar saldo de multa: " + e.getMessage(), e);
        }
    }

    private MultaConductor mapear(ResultSet rs) throws SQLException {
        MultaConductor m = new MultaConductor();
        m.setIdMulta(rs.getInt("id_multa"));
        m.setIdConductor(rs.getInt("id_conductor"));
        m.setMotivo(rs.getString("motivo"));
        m.setMontoOriginal(rs.getBigDecimal("monto_original"));
        m.setSaldoPendiente(rs.getBigDecimal("saldo_pendiente"));
        m.setFechaMulta(rs.getDate("fecha_multa").toLocalDate());
        Timestamp fa = rs.getTimestamp("fecha_ultimo_abono");
        if (fa != null) m.setFechaUltimoAbono(fa.toLocalDateTime());
        m.setEstado(EstadoMulta.fromEtiqueta(rs.getString("estado")));
        return m;
    }
}
