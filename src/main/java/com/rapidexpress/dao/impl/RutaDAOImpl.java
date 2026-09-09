package com.rapidexpress.dao.impl;

import com.rapidexpress.config.DatabaseConfig;
import com.rapidexpress.dao.RutaDAO;
import com.rapidexpress.model.DetalleEntregaPaquete;
import com.rapidexpress.model.EstadoRuta;
import com.rapidexpress.model.Ruta;
import com.rapidexpress.model.RutaPaquete;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RutaDAOImpl implements RutaDAO {

    @Override
    public Ruta crear(Ruta r) {
        String sql = "INSERT INTO rutas (id_vehiculo, id_conductor, fecha_ruta, estado, " +
                "carga_total_kg) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdVehiculo());
            ps.setInt(2, r.getIdConductor());
            ps.setDate(3, Date.valueOf(r.getFechaRuta()));
            ps.setString(4, r.getEstado().getEtiqueta());
            ps.setBigDecimal(5, r.getCargaTotalKg());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setIdRuta(keys.getInt(1));
            }
            return r;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Ruta> buscarPorId(int idRuta) {
        String sql = "SELECT * FROM rutas WHERE id_ruta = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ruta> listarTodas() {
        String sql = "SELECT * FROM rutas ORDER BY id_ruta DESC";
        List<Ruta> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.add(mapear(rs));
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar rutas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ruta> listarPorEstado(EstadoRuta estado) {
        String sql = "SELECT * FROM rutas WHERE estado = ? ORDER BY id_ruta DESC";
        List<Ruta> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar rutas por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ruta> listarPorVehiculo(int idVehiculo) {
        String sql = "SELECT * FROM rutas WHERE id_vehiculo = ? ORDER BY fecha_ruta DESC";
        List<Ruta> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar rutas por vehiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ruta> listarPorConductorYFechas(int idConductor, LocalDate desde, LocalDate hasta) {
        String sql = "SELECT * FROM rutas WHERE id_conductor = ? AND fecha_ruta BETWEEN ? AND ? " +
                "ORDER BY fecha_ruta";
        List<Ruta> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConductor);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar rutas por conductor y fechas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DetalleEntregaPaquete> listarDetalleEntregasPorConductorYFechas(int idConductor,
                                                                                  LocalDate desde, LocalDate hasta) {
        String sql = "SELECT r.id_ruta, r.fecha_ruta, p.id_paquete, p.codigo_seguimiento, " +
                "p.descripcion_contenido, p.direccion_destino, rp.estado_entrega, rp.fecha_entrega " +
                "FROM rutas r " +
                "JOIN ruta_paquetes rp ON rp.id_ruta = r.id_ruta " +
                "JOIN paquetes p ON p.id_paquete = rp.id_paquete " +
                "WHERE r.id_conductor = ? AND r.fecha_ruta BETWEEN ? AND ? " +
                "ORDER BY r.fecha_ruta, rp.orden_entrega";
        List<DetalleEntregaPaquete> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConductor);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleEntregaPaquete d = new DetalleEntregaPaquete();
                    d.setIdRuta(rs.getInt("id_ruta"));
                    d.setFechaRuta(rs.getDate("fecha_ruta").toLocalDate());
                    d.setIdPaquete(rs.getInt("id_paquete"));
                    d.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));
                    d.setDescripcionContenido(rs.getString("descripcion_contenido"));
                    d.setDireccionDestino(rs.getString("direccion_destino"));
                    d.setEstadoEntrega(rs.getString("estado_entrega"));
                    Timestamp fe = rs.getTimestamp("fecha_entrega");
                    if (fe != null) d.setFechaEntrega(fe.toLocalDateTime());
                    resultado.add(d);
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al generar el reporte de entregas por conductor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstado(int idRuta, EstadoRuta estado) {
        String sql = "UPDATE rutas SET estado = ? WHERE id_ruta = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getEtiqueta());
            ps.setInt(2, idRuta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean marcarInicio(int idRuta) {
        String sql = "UPDATE rutas SET estado = 'En Curso', hora_inicio = NOW() WHERE id_ruta = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean marcarFin(int idRuta) {
        String sql = "UPDATE rutas SET estado = 'Finalizada', hora_fin = NOW() WHERE id_ruta = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al finalizar ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public RutaPaquete asignarPaquete(RutaPaquete rp) {
        String sql = "INSERT INTO ruta_paquetes (id_ruta, id_paquete, orden_entrega, " +
                "estado_entrega) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rp.getIdRuta());
            ps.setInt(2, rp.getIdPaquete());
            ps.setInt(3, rp.getOrdenEntrega());
            ps.setString(4, rp.getEstadoEntrega());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) rp.setIdRutaPaquete(keys.getInt(1));
            }
            return rp;
        } catch (SQLException e) {
            throw new RuntimeException("Error al asignar paquete a ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RutaPaquete> listarPaquetesDeRuta(int idRuta) {
        String sql = "SELECT * FROM ruta_paquetes WHERE id_ruta = ? ORDER BY orden_entrega";
        List<RutaPaquete> resultado = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RutaPaquete rp = new RutaPaquete();
                    rp.setIdRutaPaquete(rs.getInt("id_ruta_paquete"));
                    rp.setIdRuta(rs.getInt("id_ruta"));
                    rp.setIdPaquete(rs.getInt("id_paquete"));
                    rp.setOrdenEntrega(rs.getInt("orden_entrega"));
                    rp.setEstadoEntrega(rs.getString("estado_entrega"));
                    Timestamp fe = rs.getTimestamp("fecha_entrega");
                    if (fe != null) rp.setFechaEntrega(fe.toLocalDateTime());
                    resultado.add(rp);
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar paquetes de ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstadoEntrega(int idRutaPaquete, String estadoEntrega) {
        String sql = "UPDATE ruta_paquetes SET estado_entrega = ?, " +
                "fecha_entrega = CASE WHEN ? IN ('Entregado','Devuelto') THEN NOW() ELSE fecha_entrega END " +
                "WHERE id_ruta_paquete = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estadoEntrega);
            ps.setString(2, estadoEntrega);
            ps.setInt(3, idRutaPaquete);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de entrega: " + e.getMessage(), e);
        }
    }

    private Ruta mapear(ResultSet rs) throws SQLException {
        Ruta r = new Ruta();
        r.setIdRuta(rs.getInt("id_ruta"));
        r.setIdVehiculo(rs.getInt("id_vehiculo"));
        r.setIdConductor(rs.getInt("id_conductor"));
        r.setFechaRuta(rs.getDate("fecha_ruta").toLocalDate());
        Timestamp hi = rs.getTimestamp("hora_inicio");
        if (hi != null) r.setHoraInicio(hi.toLocalDateTime());
        Timestamp hf = rs.getTimestamp("hora_fin");
        if (hf != null) r.setHoraFin(hf.toLocalDateTime());
        r.setEstado(EstadoRuta.fromEtiqueta(rs.getString("estado")));
        r.setCargaTotalKg(rs.getBigDecimal("carga_total_kg"));
        Timestamp fc = rs.getTimestamp("fecha_creacion");
        if (fc != null) r.setFechaCreacion(fc.toLocalDateTime());
        return r;
    }
}
