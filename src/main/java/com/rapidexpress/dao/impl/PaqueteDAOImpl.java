package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.PaqueteDAO;
import com.rapidexpress.model.EstadoPaquete;
import com.rapidexpress.model.Paquete;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaqueteDAOImpl implements PaqueteDAO {

    @Override
    public Paquete crear(Paquete p) {
        // El codigo de seguimiento se genera de forma unica: RX-<anio><timestamp-corto>
        String codigo = generarCodigoSeguimiento();
        String sql = "INSERT INTO paquetes (codigo_seguimiento, descripcion_contenido, peso_kg, " +
                "dimensiones, direccion_origen, direccion_destino, id_remitente, id_destinatario, " +
                "estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, codigo);
            ps.setString(2, p.getDescripcionContenido());
            ps.setBigDecimal(3, p.getPesoKg());
            ps.setString(4, p.getDimensiones());
            ps.setString(5, p.getDireccionOrigen());
            ps.setString(6, p.getDireccionDestino());
            ps.setInt(7, p.getIdRemitente());
            ps.setInt(8, p.getIdDestinatario());
            ps.setString(9, p.getEstado().getEtiqueta());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setIdPaquete(keys.getInt(1));
            }
            p.setCodigoSeguimiento(codigo);
            return p;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear paquete: " + e.getMessage(), e);
        }
    }

    private String generarCodigoSeguimiento() {
        long sufijo = System.currentTimeMillis() % 1_000_000L;
        int anio = LocalDateTime.now().getYear();
        return String.format("RX-%d-%06d", anio, sufijo);
    }

    @Override
    public Optional<Paquete> buscarPorId(int idPaquete) {
        String sql = "SELECT * FROM paquetes WHERE id_paquete = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPaquete);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar paquete: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Paquete> buscarPorCodigo(String codigoSeguimiento) {
        String sql = "SELECT * FROM paquetes WHERE codigo_seguimiento = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoSeguimiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar paquete por codigo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paquete> listarTodos() {
        String sql = "SELECT * FROM paquetes ORDER BY id_paquete";
        List<Paquete> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar paquetes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paquete> listarPorEstado(EstadoPaquete estado) {
        String sql = "SELECT * FROM paquetes WHERE estado = ? ORDER BY id_paquete";
        List<Paquete> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar paquetes por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstado(int idPaquete, EstadoPaquete estado) {
        String sql = "UPDATE paquetes SET estado = ? WHERE id_paquete = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            ps.setInt(2, idPaquete);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de paquete: " + e.getMessage(), e);
        }
    }

    private Paquete mapear(ResultSet rs) throws SQLException {
        Paquete p = new Paquete();
        p.setIdPaquete(rs.getInt("id_paquete"));
        p.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));
        p.setDescripcionContenido(rs.getString("descripcion_contenido"));
        p.setPesoKg(rs.getBigDecimal("peso_kg"));
        p.setDimensiones(rs.getString("dimensiones"));
        p.setDireccionOrigen(rs.getString("direccion_origen"));
        p.setDireccionDestino(rs.getString("direccion_destino"));
        p.setIdRemitente(rs.getInt("id_remitente"));
        p.setIdDestinatario(rs.getInt("id_destinatario"));
        p.setEstado(EstadoPaquete.fromEtiqueta(rs.getString("estado")));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) p.setFechaRegistro(ts.toLocalDateTime());
        return p;
    }
}
