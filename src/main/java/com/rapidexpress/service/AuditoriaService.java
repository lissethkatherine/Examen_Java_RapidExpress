package com.rapidexpress.service;

import com.rapidexpress.dao.AuditoriaDAO;
import com.rapidexpress.dao.impl.AuditoriaDAOImpl;
import com.rapidexpress.model.RegistroAuditoria;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio central de auditoria. Cada operacion critica del sistema
 * (creacion de paquetes, inicio/fin de ruta, cambios de estado relevantes)
 * se registra en dos lugares, tal como exige el modulo de Reportes y
 * Auditoria:
 *   1) La tabla "auditoria" en la base de datos (para consultas y reportes).
 *   2) Un archivo de texto centralizado (auditoria.log) en la raiz del
 *      proyecto, como bitacora de respaldo legible fuera del sistema.
 */
public class AuditoriaService {

    private static final String ARCHIVO_LOG = "auditoria.log";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAOImpl();

    public void registrar(String usuarioOperador, String accion, String tablaAfectada,
                           Integer idEntidadAfectada, String descripcion) {
        RegistroAuditoria registro = new RegistroAuditoria(usuarioOperador, accion, tablaAfectada,
                idEntidadAfectada, descripcion);

        // 1) Persistir en base de datos
        try {
            auditoriaDAO.registrar(registro);
        } catch (RuntimeException e) {
            System.err.println("Advertencia: no se pudo registrar la auditoria en base de datos: "
                    + e.getMessage());
        }

        // 2) Persistir en archivo de texto centralizado
        String linea = String.format("[%s] usuario=%s | accion=%s | tabla=%s | id=%s | %s",
                LocalDateTime.now().format(FORMATO), usuarioOperador, accion, tablaAfectada,
                idEntidadAfectada != null ? idEntidadAfectada : "-", descripcion);
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("Advertencia: no se pudo escribir en " + ARCHIVO_LOG + ": " + e.getMessage());
        }
    }
}
