-- =====================================================================
-- Datos de prueba: multas_conductor (20 registros)
-- Asume que ya existen 20 conductores (id_conductor 1-20) del script
-- 2_data_dml.sql
-- =====================================================================

USE rapidexpress_db;

INSERT INTO multas_conductor (id_conductor, motivo, monto_original, saldo_pendiente, fecha_multa, estado) VALUES
(1, 'Exceso de velocidad', 180000, 180000, '2026-06-01', 'Pendiente'),
(2, 'Parqueo en zona prohibida', 90000, 0, '2026-06-02', 'Pagada'),
(3, 'No uso de cinturon de seguridad', 120000, 60000, '2026-06-03', 'Pendiente'),
(4, 'Exceso de velocidad', 180000, 180000, '2026-06-04', 'Pendiente'),
(5, 'Semaforo en rojo', 250000, 0, '2026-06-05', 'Pagada'),
(6, 'Uso de celular al conducir', 200000, 200000, '2026-06-06', 'Pendiente'),
(7, 'Parqueo en zona prohibida', 90000, 30000, '2026-06-07', 'Pendiente'),
(8, 'Exceso de velocidad', 180000, 180000, '2026-06-08', 'Pendiente'),
(9, 'No uso de cinturon de seguridad', 120000, 0, '2026-06-09', 'Pagada'),
(10, 'Semaforo en rojo', 250000, 250000, '2026-06-10', 'Pendiente'),
(11, 'Exceso de velocidad', 180000, 90000, '2026-06-11', 'Pendiente'),
(12, 'Uso de celular al conducir', 200000, 200000, '2026-06-12', 'Pendiente'),
(13, 'Parqueo en zona prohibida', 90000, 0, '2026-06-13', 'Pagada'),
(14, 'No uso de cinturon de seguridad', 120000, 120000, '2026-06-14', 'Pendiente'),
(15, 'Exceso de velocidad', 180000, 45000, '2026-06-15', 'Pendiente'),
(16, 'Semaforo en rojo', 250000, 250000, '2026-06-16', 'Pendiente'),
(17, 'Uso de celular al conducir', 200000, 0, '2026-06-17', 'Pagada'),
(18, 'Parqueo en zona prohibida', 90000, 90000, '2026-06-18', 'Pendiente'),
(19, 'Exceso de velocidad', 180000, 60000, '2026-06-19', 'Pendiente'),
(20, 'No uso de cinturon de seguridad', 120000, 120000, '2026-06-20', 'Pendiente');
