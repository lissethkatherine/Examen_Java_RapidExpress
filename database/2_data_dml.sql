-- =====================================================================
-- RapidExpress - Sistema de Gestion de Flotas y Rutas
-- Script DML: poblacion inicial de la base de datos
-- Motor: MySQL 8.x
-- =====================================================================

USE rapidexpress_db;

-- ---------------------------------------------------------------------
-- CLIENTES (25 registros: remitentes y destinatarios de paquetes)
-- ---------------------------------------------------------------------
INSERT INTO clientes (nombre_completo, tipo_documento, numero_documento, telefono, email, direccion) VALUES
('Carlos Ramirez', 'CC', '10000000', '3185822412', 'carlos.ramirez@correo.com', 'Carrera 43A # 5-15, Medellin'),
('Maria Gonzalez', 'CC', '10000137', '3103356886', 'maria.gonzalez@correo.com', 'Carrera 2 # 8-25, Cartagena'),
('Juan Perez', 'CC', '10000274', '3132868828', 'juan.perez@correo.com', 'Calle 84 # 50-30, Barranquilla'),
('Ana Torres', 'CC', '10000411', '3118728463', 'ana.torres@correo.com', 'Carrera 43A # 5-15, Medellin'),
('Luis Martinez', 'CC', '10000548', '3190825067', 'luis.martinez@correo.com', 'Calle 22 # 4-30, Santa Marta'),
('Sofia Rodriguez', 'CC', '10000685', '3111668732', 'sofia.rodriguez@correo.com', 'Carrera 5 # 15-60, Ibague'),
('Diego Fernandez', 'CC', '10000822', '3156629388', 'diego.fernandez@correo.com', 'Calle 100 # 15-20, Bogota'),
('Valentina Lopez', 'CC', '10000959', '3103999315', 'valentina.lopez@correo.com', 'Carrera 43A # 5-15, Medellin'),
('Andres Diaz', 'CC', '10001096', '3129345092', 'andres.diaz@correo.com', 'Calle 84 # 50-30, Barranquilla'),
('Camila Sanchez', 'CC', '10001233', '3167827638', 'camila.sanchez@correo.com', 'Carrera 5 # 15-60, Ibague'),
('Jorge Morales', 'CC', '10001370', '3103561597', 'jorge.morales@correo.com', 'Calle 22 # 4-30, Santa Marta'),
('Laura Castro', 'CC', '10001507', '3126687537', 'laura.castro@correo.com', 'Calle 22 # 4-30, Santa Marta'),
('Miguel Herrera', 'CC', '10001644', '3156306997', 'miguel.herrera@correo.com', 'Calle 84 # 50-30, Barranquilla'),
('Paula Vargas', 'CC', '10001781', '3160291817', 'paula.vargas@correo.com', 'Carrera 5 # 15-60, Ibague'),
('Ricardo Ortiz', 'CC', '10001918', '3137338124', 'ricardo.ortiz@correo.com', 'Calle 100 # 15-20, Bogota'),
('Daniela Rojas', 'CC', '10002055', '3121429110', 'daniela.rojas@correo.com', 'Avenida 30 de Agosto # 12-40, Pereira'),
('Felipe Guzman', 'CC', '10002192', '3145667651', 'felipe.guzman@correo.com', 'Carrera 2 # 8-25, Cartagena'),
('Isabella Mendoza', 'CC', '10002329', '3120868105', 'isabella.mendoza@correo.com', 'Calle 84 # 50-30, Barranquilla'),
('Sebastian Nino', 'CC', '10002466', '3145176955', 'sebastian.nino@correo.com', 'Carrera 43A # 5-15, Medellin'),
('Gabriela Cruz', 'CC', '10002603', '3112448136', 'gabriela.cruz@correo.com', 'Avenida 30 de Agosto # 12-40, Pereira'),
('Alejandro Pena', 'CC', '10002740', '3112981052', 'alejandro.pena@correo.com', 'Calle 36 # 20-15, Bucaramanga'),
('Natalia Rios', 'CC', '10002877', '3146164955', 'natalia.rios@correo.com', 'Carrera 5 # 15-60, Ibague'),
('Santiago Cardenas', 'CC', '10003014', '3135503389', 'santiago.cardenas@correo.com', 'Calle 100 # 15-20, Bogota'),
('Mariana Suarez', 'CC', '10003151', '3197942942', 'mariana.suarez@correo.com', 'Carrera 23 # 60-10, Manizales'),
('Julian Beltran', 'CC', '10003288', '3171971316', 'julian.beltran@correo.com', 'Carrera 43A # 5-15, Medellin');

-- ---------------------------------------------------------------------
-- VEHICULOS (20 registros)
-- ---------------------------------------------------------------------
INSERT INTO vehiculos (placa, marca, modelo, anio_fabricacion, capacidad_carga_kg, estado) VALUES
('RXP100', 'Chevrolet', 'NPR', 2021, 800, 'Disponible'),
('RXP101', 'Hino', '300', 2023, 1200, 'Disponible'),
('RXP102', 'Renault', 'Master', 2024, 3000, 'Disponible'),
('RXP103', 'Ford', 'Transit', 2020, 2000, 'En Ruta'),
('RXP104', 'Nissan', 'NV350', 2018, 2500, 'En Mantenimiento'),
('RXP105', 'Kia', 'K2500', 2016, 800, 'Disponible'),
('RXP106', 'Isuzu', 'ELF', 2018, 3000, 'Disponible'),
('RXP107', 'Mercedes-Benz', 'Sprinter', 2019, 800, 'Disponible'),
('RXP108', 'Foton', 'Aumark', 2018, 3000, 'En Ruta'),
('RXP109', 'JAC', 'N-Series', 2016, 1500, 'En Mantenimiento'),
('RXP110', 'Chevrolet', 'NPR', 2019, 1500, 'Disponible'),
('RXP111', 'Hino', '300', 2020, 1000, 'Disponible'),
('RXP112', 'Renault', 'Master', 2020, 1200, 'Disponible'),
('RXP113', 'Ford', 'Transit', 2018, 2500, 'En Ruta'),
('RXP114', 'Nissan', 'NV350', 2019, 2500, 'En Mantenimiento'),
('RXP115', 'Kia', 'K2500', 2016, 2000, 'Disponible'),
('RXP116', 'Isuzu', 'ELF', 2017, 2000, 'Disponible'),
('RXP117', 'Mercedes-Benz', 'Sprinter', 2018, 1000, 'Disponible'),
('RXP118', 'Foton', 'Aumark', 2022, 1500, 'En Ruta'),
('RXP119', 'JAC', 'N-Series', 2019, 2500, 'En Mantenimiento');

-- ---------------------------------------------------------------------
-- CONDUCTORES (20 registros)
-- ---------------------------------------------------------------------
INSERT INTO conductores (numero_identificacion, nombre_completo, tipo_licencia, numero_contacto, estado) VALUES
('80000000', 'Sofia Rodriguez', 'B1', '3192363534', 'Activo'),
('80000211', 'Diego Fernandez', 'B2', '3174752529', 'Activo'),
('80000422', 'Valentina Lopez', 'C1', '3129476249', 'Activo'),
('80000633', 'Andres Diaz', 'C2', '3191887369', 'De Vacaciones'),
('80000844', 'Camila Sanchez', 'C3', '3143524491', 'Inactivo'),
('80001055', 'Jorge Morales', 'B1', '3107507864', 'Activo'),
('80001266', 'Laura Castro', 'B2', '3130742311', 'Activo'),
('80001477', 'Miguel Herrera', 'C1', '3104308421', 'Activo'),
('80001688', 'Paula Vargas', 'C2', '3142339391', 'De Vacaciones'),
('80001899', 'Ricardo Ortiz', 'C3', '3153843426', 'Inactivo'),
('80002110', 'Daniela Rojas', 'B1', '3135935572', 'Activo'),
('80002321', 'Felipe Guzman', 'B2', '3108883684', 'Activo'),
('80002532', 'Isabella Mendoza', 'C1', '3128317637', 'Activo'),
('80002743', 'Sebastian Nino', 'C2', '3176125617', 'De Vacaciones'),
('80002954', 'Gabriela Cruz', 'C3', '3196356822', 'Inactivo'),
('80003165', 'Alejandro Pena', 'B1', '3142235350', 'Activo'),
('80003376', 'Natalia Rios', 'B2', '3128538251', 'Activo'),
('80003587', 'Santiago Cardenas', 'C1', '3187971488', 'Activo'),
('80003798', 'Mariana Suarez', 'C2', '3167005685', 'De Vacaciones'),
('80004009', 'Julian Beltran', 'C3', '3153100814', 'Inactivo');

-- ---------------------------------------------------------------------
-- MANTENIMIENTOS_VEHICULO (24 registros - historial de mantenimiento)
-- ---------------------------------------------------------------------
INSERT INTO mantenimientos_vehiculo (id_vehiculo, tipo_mantenimiento, fecha_mantenimiento, descripcion, costo, taller) VALUES
(1, 'Preventivo', '2026-01-10', 'Rotacion de llantas', 250000, 'Taller Express Motors'),
(2, 'Correctivo', '2026-02-11', 'Reparacion de sistema de frenos', 120000, 'Servitecnica Andina'),
(3, 'Preventivo', '2026-03-12', 'Cambio de aceite y filtros', 380000, 'Servitecnica Andina'),
(3, 'Correctivo', '2026-04-13', 'Reparacion de sistema de frenos', 600000, 'AutoServicio Norte'),
(4, 'Preventivo', '2026-05-14', 'Cambio de aceite y filtros', 120000, 'Taller Express Motors'),
(5, 'Correctivo', '2026-06-15', 'Cambio de embrague', 120000, 'AutoServicio Norte'),
(6, 'Preventivo', '2026-07-16', 'Cambio de aceite y filtros', 600000, 'Taller Express Motors'),
(6, 'Correctivo', '2026-08-17', 'Reparacion de sistema de frenos', 1200000, 'Servitecnica Andina'),
(7, 'Preventivo', '2026-01-18', 'Cambio de aceite y filtros', 250000, 'Servitecnica Andina'),
(8, 'Correctivo', '2026-02-19', 'Reparacion de sistema de frenos', 600000, 'Servitecnica Andina'),
(9, 'Preventivo', '2026-03-20', 'Revision general de 10.000 km', 120000, 'AutoServicio Norte'),
(9, 'Correctivo', '2026-04-21', 'Reparacion de sistema de frenos', 600000, 'AutoServicio Norte'),
(10, 'Preventivo', '2026-05-22', 'Rotacion de llantas', 450000, 'AutoServicio Norte'),
(11, 'Correctivo', '2026-06-23', 'Reparacion de motor', 120000, 'Servitecnica Andina'),
(12, 'Preventivo', '2026-07-24', 'Rotacion de llantas', 600000, 'AutoServicio Norte'),
(12, 'Correctivo', '2026-08-10', 'Reparacion de sistema de frenos', 600000, 'Servitecnica Andina'),
(13, 'Preventivo', '2026-01-11', 'Alineacion y balanceo', 250000, 'Mecanica del Valle'),
(14, 'Correctivo', '2026-02-12', 'Reparacion de sistema de frenos', 600000, 'Taller Central RX'),
(15, 'Preventivo', '2026-03-13', 'Cambio de bateria', 120000, 'Servitecnica Andina'),
(15, 'Correctivo', '2026-04-14', 'Cambio de amortiguadores', 450000, 'Servitecnica Andina'),
(16, 'Preventivo', '2026-05-15', 'Revision general de 10.000 km', 1200000, 'Mecanica del Valle'),
(17, 'Correctivo', '2026-06-16', 'Cambio de embrague', 600000, 'Taller Express Motors'),
(18, 'Preventivo', '2026-07-17', 'Rotacion de llantas', 380000, 'AutoServicio Norte'),
(18, 'Correctivo', '2026-08-18', 'Cambio de amortiguadores', 850000, 'AutoServicio Norte'),
(19, 'Preventivo', '2026-01-19', 'Cambio de aceite y filtros', 600000, 'Mecanica del Valle'),
(20, 'Correctivo', '2026-02-20', 'Reparacion de motor', 450000, 'Mecanica del Valle');
-- Total mantenimientos generados: 26

-- ---------------------------------------------------------------------
-- ASIGNACIONES_VEHICULO_CONDUCTOR (20 registros: historial + activas)
-- ---------------------------------------------------------------------
INSERT INTO asignaciones_vehiculo_conductor (id_vehiculo, id_conductor, fecha_asignacion, fecha_liberacion, estado) VALUES
(5, 1, '2026-08-01 07:00:00', NULL, 'Activa'),
(16, 2, '2026-08-02 07:00:00', NULL, 'Activa'),
(6, 3, '2026-08-03 07:00:00', NULL, 'Activa'),
(1, 6, '2026-08-04 07:00:00', NULL, 'Activa'),
(12, 7, '2026-08-05 07:00:00', NULL, 'Activa'),
(14, 8, '2026-08-06 07:00:00', NULL, 'Activa'),
(4, 11, '2026-08-07 07:00:00', NULL, 'Activa'),
(2, 12, '2026-08-08 07:00:00', NULL, 'Activa'),
(17, 13, '2026-08-09 07:00:00', NULL, 'Activa'),
(10, 16, '2026-08-10 07:00:00', NULL, 'Activa'),
(11, 17, '2026-08-11 07:00:00', NULL, 'Activa'),
(8, 18, '2026-08-12 07:00:00', NULL, 'Activa'),
(2, 1, '2026-01-05 08:00:00', '2026-01-20 17:30:00', 'Finalizada'),
(17, 2, '2026-02-05 08:00:00', '2026-02-20 17:30:00', 'Finalizada'),
(10, 3, '2026-03-05 08:00:00', '2026-03-20 17:30:00', 'Finalizada'),
(11, 6, '2026-04-05 08:00:00', '2026-04-20 17:30:00', 'Finalizada'),
(8, 7, '2026-05-05 08:00:00', '2026-05-20 17:30:00', 'Finalizada'),
(9, 8, '2026-06-05 08:00:00', '2026-06-20 17:30:00', 'Finalizada'),
(13, 11, '2026-01-05 08:00:00', '2026-01-20 17:30:00', 'Finalizada'),
(3, 12, '2026-02-05 08:00:00', '2026-02-20 17:30:00', 'Finalizada');
-- Total asignaciones generadas: 20

-- ---------------------------------------------------------------------
-- PAQUETES (26 registros)
-- ---------------------------------------------------------------------
INSERT INTO paquetes (codigo_seguimiento, descripcion_contenido, peso_kg, dimensiones, direccion_origen, direccion_destino, id_remitente, id_destinatario, estado) VALUES
('RX-2026-000001', 'Ropa y calzado', 27.0, '20x15x10 cm', 'Carrera 43A # 5-15, Medellin', 'Carrera 43A # 5-15, Medellin', 1, 8, 'En Bodega'),
('RX-2026-000002', 'Equipo electronico', 18.13, '30x20x15 cm', 'Carrera 2 # 8-25, Cartagena', 'Calle 84 # 50-30, Barranquilla', 2, 9, 'Asignado a Ruta'),
('RX-2026-000003', 'Documentos legales', 20.66, '40x30x20 cm', 'Calle 84 # 50-30, Barranquilla', 'Carrera 5 # 15-60, Ibague', 3, 10, 'En Transito'),
('RX-2026-000004', 'Repuestos automotrices', 33.38, '50x40x30 cm', 'Carrera 43A # 5-15, Medellin', 'Calle 22 # 4-30, Santa Marta', 4, 11, 'Entregado'),
('RX-2026-000005', 'Articulos de hogar', 29.43, '25x25x25 cm', 'Calle 22 # 4-30, Santa Marta', 'Calle 22 # 4-30, Santa Marta', 5, 12, 'Devuelto'),
('RX-2026-000006', 'Libros y papeleria', 28.23, '60x40x35 cm', 'Carrera 5 # 15-60, Ibague', 'Calle 84 # 50-30, Barranquilla', 6, 13, 'En Bodega'),
('RX-2026-000007', 'Alimentos no perecederos', 37.51, '20x15x10 cm', 'Calle 100 # 15-20, Bogota', 'Carrera 5 # 15-60, Ibague', 7, 14, 'Asignado a Ruta'),
('RX-2026-000008', 'Insumos medicos', 3.31, '30x20x15 cm', 'Carrera 43A # 5-15, Medellin', 'Calle 100 # 15-20, Bogota', 8, 15, 'En Transito'),
('RX-2026-000009', 'Juguetes', 2.09, '40x30x20 cm', 'Calle 84 # 50-30, Barranquilla', 'Avenida 30 de Agosto # 12-40, Pereira', 9, 16, 'Entregado'),
('RX-2026-000010', 'Herramientas', 39.64, '50x40x30 cm', 'Carrera 5 # 15-60, Ibague', 'Carrera 2 # 8-25, Cartagena', 10, 17, 'Devuelto'),
('RX-2026-000011', 'Accesorios de computo', 27.18, '25x25x25 cm', 'Calle 22 # 4-30, Santa Marta', 'Calle 84 # 50-30, Barranquilla', 11, 18, 'En Bodega'),
('RX-2026-000012', 'Productos de aseo', 35.13, '60x40x35 cm', 'Calle 22 # 4-30, Santa Marta', 'Carrera 43A # 5-15, Medellin', 12, 19, 'Asignado a Ruta'),
('RX-2026-000013', 'Piezas de maquinaria', 15.02, '20x15x10 cm', 'Calle 84 # 50-30, Barranquilla', 'Avenida 30 de Agosto # 12-40, Pereira', 13, 20, 'En Transito'),
('RX-2026-000014', 'Material de construccion', 26.8, '30x20x15 cm', 'Carrera 5 # 15-60, Ibague', 'Calle 36 # 20-15, Bucaramanga', 14, 21, 'Entregado'),
('RX-2026-000015', 'Artesanias', 9.19, '40x30x20 cm', 'Calle 100 # 15-20, Bogota', 'Carrera 5 # 15-60, Ibague', 15, 22, 'Devuelto'),
('RX-2026-000016', 'Ropa y calzado', 10.9, '50x40x30 cm', 'Avenida 30 de Agosto # 12-40, Pereira', 'Calle 100 # 15-20, Bogota', 16, 23, 'En Bodega'),
('RX-2026-000017', 'Equipo electronico', 13.59, '25x25x25 cm', 'Carrera 2 # 8-25, Cartagena', 'Carrera 23 # 60-10, Manizales', 17, 24, 'Asignado a Ruta'),
('RX-2026-000018', 'Documentos legales', 0.7, '60x40x35 cm', 'Calle 84 # 50-30, Barranquilla', 'Carrera 43A # 5-15, Medellin', 18, 25, 'En Transito'),
('RX-2026-000019', 'Repuestos automotrices', 4.28, '20x15x10 cm', 'Carrera 43A # 5-15, Medellin', 'Carrera 43A # 5-15, Medellin', 19, 1, 'Entregado'),
('RX-2026-000020', 'Articulos de hogar', 29.64, '30x20x15 cm', 'Avenida 30 de Agosto # 12-40, Pereira', 'Carrera 2 # 8-25, Cartagena', 20, 2, 'Devuelto'),
('RX-2026-000021', 'Libros y papeleria', 18.6, '40x30x20 cm', 'Calle 36 # 20-15, Bucaramanga', 'Calle 84 # 50-30, Barranquilla', 21, 3, 'En Bodega'),
('RX-2026-000022', 'Alimentos no perecederos', 25.03, '50x40x30 cm', 'Carrera 5 # 15-60, Ibague', 'Carrera 43A # 5-15, Medellin', 22, 4, 'Asignado a Ruta'),
('RX-2026-000023', 'Insumos medicos', 41.96, '25x25x25 cm', 'Calle 100 # 15-20, Bogota', 'Calle 22 # 4-30, Santa Marta', 23, 5, 'En Transito'),
('RX-2026-000024', 'Juguetes', 4.2, '60x40x35 cm', 'Carrera 23 # 60-10, Manizales', 'Carrera 5 # 15-60, Ibague', 24, 6, 'Entregado'),
('RX-2026-000025', 'Herramientas', 11.8, '20x15x10 cm', 'Carrera 43A # 5-15, Medellin', 'Calle 100 # 15-20, Bogota', 25, 7, 'Devuelto'),
('RX-2026-000026', 'Accesorios de computo', 34.23, '30x20x15 cm', 'Carrera 43A # 5-15, Medellin', 'Carrera 43A # 5-15, Medellin', 1, 8, 'En Bodega');
-- Total paquetes generados: 26

-- ---------------------------------------------------------------------
-- RUTAS (22 registros: hojas de ruta diarias)
-- ---------------------------------------------------------------------
INSERT INTO rutas (id_vehiculo, id_conductor, fecha_ruta, hora_inicio, hora_fin, estado, carga_total_kg) VALUES
(5, 1, '2026-08-01', '2026-08-01 07:15:00', '2026-08-01 16:45:00', 'Finalizada', 836.14),
(16, 2, '2026-08-02', '2026-08-02 07:15:00', '2026-08-02 16:45:00', 'Finalizada', 856.31),
(6, 3, '2026-08-03', '2026-08-03 07:30:00', NULL, 'En Curso', 808.57),
(1, 6, '2026-08-04', NULL, NULL, 'Planificada', 121.02),
(12, 7, '2026-08-05', '2026-08-05 07:15:00', '2026-08-05 16:45:00', 'Finalizada', 553.22),
(14, 8, '2026-08-06', '2026-08-06 07:15:00', '2026-08-06 16:45:00', 'Finalizada', 410.19),
(4, 11, '2026-08-07', '2026-08-07 07:30:00', NULL, 'En Curso', 500.57),
(2, 12, '2026-08-08', NULL, NULL, 'Planificada', 160.76),
(17, 13, '2026-08-09', '2026-08-09 07:15:00', '2026-08-09 16:45:00', 'Finalizada', 213.2),
(10, 16, '2026-08-10', '2026-08-10 07:15:00', '2026-08-10 16:45:00', 'Finalizada', 427.89),
(11, 17, '2026-08-11', '2026-08-11 07:30:00', NULL, 'En Curso', 237.88),
(8, 18, '2026-08-12', NULL, NULL, 'Planificada', 436.78),
(5, 1, '2026-08-13', '2026-08-13 07:15:00', '2026-08-13 16:45:00', 'Finalizada', 71.05),
(16, 2, '2026-08-14', '2026-08-14 07:15:00', '2026-08-14 16:45:00', 'Finalizada', 122.91),
(6, 3, '2026-08-15', '2026-08-15 07:30:00', NULL, 'En Curso', 653.51),
(1, 6, '2026-08-16', NULL, NULL, 'Planificada', 408.06),
(12, 7, '2026-08-17', '2026-08-17 07:15:00', '2026-08-17 16:45:00', 'Finalizada', 485.74),
(14, 8, '2026-08-18', '2026-08-18 07:15:00', '2026-08-18 16:45:00', 'Finalizada', 674.08),
(4, 11, '2026-08-19', '2026-08-19 07:30:00', NULL, 'En Curso', 355.15),
(2, 12, '2026-08-20', NULL, NULL, 'Planificada', 98.92),
(17, 13, '2026-08-21', '2026-08-21 07:15:00', '2026-08-21 16:45:00', 'Finalizada', 715.64),
(10, 16, '2026-08-22', '2026-08-22 07:15:00', '2026-08-22 16:45:00', 'Finalizada', 550.75);
-- Total rutas generadas: 22

-- ---------------------------------------------------------------------
-- RUTA_PAQUETES (20 registros: paquetes distribuidos en rutas)
-- ---------------------------------------------------------------------
INSERT INTO ruta_paquetes (id_ruta, id_paquete, orden_entrega, estado_entrega, fecha_entrega) VALUES
(13, 2, 1, 'Pendiente', NULL),
(4, 3, 1, 'Pendiente', NULL),
(1, 4, 1, 'Entregado', '2026-08-15 14:20:00'),
(5, 5, 1, 'Devuelto', '2026-08-16 11:05:00'),
(17, 7, 1, 'Pendiente', NULL),
(19, 8, 1, 'Pendiente', NULL),
(14, 9, 1, 'Entregado', '2026-08-15 14:20:00'),
(9, 10, 1, 'Devuelto', '2026-08-16 11:05:00'),
(15, 12, 1, 'Pendiente', NULL),
(3, 13, 1, 'Pendiente', NULL),
(8, 14, 1, 'Entregado', '2026-08-15 14:20:00'),
(2, 15, 1, 'Devuelto', '2026-08-16 11:05:00'),
(16, 17, 1, 'Pendiente', NULL),
(21, 18, 1, 'Pendiente', NULL),
(11, 19, 1, 'Entregado', '2026-08-15 14:20:00'),
(10, 20, 1, 'Devuelto', '2026-08-16 11:05:00'),
(18, 22, 1, 'Pendiente', NULL),
(12, 23, 1, 'Pendiente', NULL),
(7, 24, 1, 'Entregado', '2026-08-15 14:20:00'),
(20, 25, 1, 'Devuelto', '2026-08-16 11:05:00');
-- Total ruta_paquetes generados: 20

-- ---------------------------------------------------------------------
-- AUDITORIA (24 registros: bitacora de operaciones criticas)
-- ---------------------------------------------------------------------
INSERT INTO auditoria (fecha_hora, usuario_operador, accion, tabla_afectada, id_entidad_afectada, descripcion) VALUES
('2026-08-01 08:05:00', 'operador.logistica1', 'CREAR_PAQUETE', 'paquetes', 1, 'Registro de nuevo paquete con codigo RX-2026-000001'),
('2026-08-01 08:10:00', 'operador.logistica1', 'CREAR_PAQUETE', 'paquetes', 2, 'Registro de nuevo paquete con codigo RX-2026-000002'),
('2026-08-02 07:20:00', 'operador.rutas1', 'INICIO_RUTA', 'rutas', 1, 'Inicio de hoja de ruta; vehiculo y conductor cambian a estado En Ruta'),
('2026-08-02 16:50:00', 'operador.rutas1', 'FIN_RUTA', 'rutas', 1, 'Finalizacion de hoja de ruta; vehiculo y conductor liberados'),
('2026-08-03 09:00:00', 'operador.flota1', 'CAMBIO_ESTADO_VEHICULO', 'vehiculos', 3, 'Vehiculo RXP102 cambia a estado En Mantenimiento'),
('2026-08-03 09:15:00', 'operador.flota1', 'REGISTRO_MANTENIMIENTO', 'mantenimientos_vehiculo', 3, 'Registro de mantenimiento preventivo para vehiculo RXP102'),
('2026-08-04 10:30:00', 'operador.personal1', 'ASIGNAR_CONDUCTOR', 'asignaciones_vehiculo_conductor', 1, 'Asignacion de conductor a vehiculo disponible'),
('2026-08-04 11:00:00', 'operador.personal1', 'CAMBIO_ESTADO_CONDUCTOR', 'conductores', 4, 'Conductor cambia a estado De Vacaciones'),
('2026-08-05 13:20:00', 'operador.logistica2', 'CAMBIO_ESTADO_PAQUETE', 'paquetes', 5, 'Paquete cambia de En Bodega a Asignado a Ruta'),
('2026-08-05 13:45:00', 'operador.logistica2', 'CAMBIO_ESTADO_PAQUETE', 'paquetes', 6, 'Paquete cambia de Asignado a Ruta a En Transito'),
('2026-08-06 15:10:00', 'operador.rutas2', 'ENTREGA_PAQUETE', 'ruta_paquetes', 7, 'Paquete marcado como Entregado durante ruta en curso'),
('2026-08-06 15:40:00', 'operador.rutas2', 'DEVOLUCION_PAQUETE', 'ruta_paquetes', 8, 'Paquete marcado como Devuelto por destinatario ausente'),
('2026-08-07 08:00:00', 'operador.rutas1', 'INICIO_RUTA', 'rutas', 5, 'Inicio de hoja de ruta diaria'),
('2026-08-07 17:05:00', 'operador.rutas1', 'FIN_RUTA', 'rutas', 5, 'Cierre de hoja de ruta diaria'),
('2026-08-08 09:40:00', 'operador.flota1', 'REGISTRO_MANTENIMIENTO', 'mantenimientos_vehiculo', 10, 'Registro de mantenimiento correctivo por falla electrica'),
('2026-08-09 12:00:00', 'operador.personal1', 'LIBERAR_ASIGNACION', 'asignaciones_vehiculo_conductor', 13, 'Finalizacion de asignacion conductor-vehiculo'),
('2026-08-10 08:15:00', 'operador.logistica1', 'CREAR_PAQUETE', 'paquetes', 15, 'Registro de nuevo paquete con codigo RX-2026-000015'),
('2026-08-11 07:50:00', 'operador.rutas2', 'INICIO_RUTA', 'rutas', 9, 'Inicio de hoja de ruta diaria'),
('2026-08-11 16:30:00', 'operador.rutas2', 'FIN_RUTA', 'rutas', 9, 'Cierre de hoja de ruta diaria'),
('2026-08-12 10:05:00', 'operador.flota1', 'CAMBIO_ESTADO_VEHICULO', 'vehiculos', 7, 'Vehiculo vuelve a estado Disponible tras mantenimiento'),
('2026-08-13 14:00:00', 'operador.logistica2', 'CAMBIO_ESTADO_PAQUETE', 'paquetes', 20, 'Paquete cambia a estado Entregado'),
('2026-08-14 09:30:00', 'operador.personal1', 'ASIGNAR_CONDUCTOR', 'asignaciones_vehiculo_conductor', 18, 'Asignacion de conductor a vehiculo disponible'),
('2026-08-15 11:15:00', 'operador.rutas1', 'MONITOREO_RUTA', 'rutas', 14, 'Consulta de estado de ruta activa y actualizacion de paquetes'),
('2026-08-16 15:50:00', 'operador.logistica2', 'DEVOLUCION_PAQUETE', 'ruta_paquetes', 19, 'Paquete marcado como Devuelto tras intento fallido de entrega');
-- Total registros de auditoria generados: 24

