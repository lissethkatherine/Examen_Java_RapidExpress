package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.AuditoriaDAO;
import com.rapidexpress.model.RegistroAuditoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAOImpl implements AuditoriaDAO {

    @Override
    public RegistroAuditoria registrar(RegistroAuditoria r) {
        String sql = "INSERT INTO auditoria (usuario_operador, accion, tabla_afectada, " +
                "id_entidad_afectada, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getUsuarioOperador());
            ps.setString(2, r.getAccion());
            ps.setString(3, r.getTablaAfectada());
            if (r.getIdEntidadAfectada() != null) {
                ps.setInt(4, r.getIdEntidadAfectada());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, r.getDescripcion());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setIdAuditoria(keys.getInt(1));
            }
            return r;
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar auditoria: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RegistroAuditoria> listarTodos() {
        String sql = "SELECT * FROM auditoria ORDER BY id_auditoria DESC";
        List<RegistroAuditoria> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RegistroAuditoria r = new RegistroAuditoria();
                r.setIdAuditoria(rs.getInt("id_auditoria"));
                Timestamp fh = rs.getTimestamp("fecha_hora");
                if (fh != null) r.setFechaHora(fh.toLocalDateTime());
                r.setUsuarioOperador(rs.getString("usuario_operador"));
                r.setAccion(rs.getString("accion"));
                r.setTablaAfectada(rs.getString("tabla_afectada"));
                int idEntidad = rs.getInt("id_entidad_afectada");
                r.setIdEntidadAfectada(rs.wasNull() ? null : idEntidad);
                r.setDescripcion(rs.getString("descripcion"));
                resultado.add(r);
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar auditoria: " + e.getMessage(), e);
        }
    }
}
