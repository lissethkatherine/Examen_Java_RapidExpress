-- =====================================================================
-- RapidExpress - Sistema de Gestion de Flotas y Rutas
-- Script DDL: creacion de la base de datos y sus tablas
-- Motor: MySQL 8.x
-- =====================================================================

CREATE DATABASE IF NOT EXISTS rapidexpress_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE rapidexpress_db;

-- ---------------------------------------------------------------------
-- Tabla: clientes
-- Almacena a las personas que intervienen en un envio, ya sea como
-- remitente o como destinatario de un paquete.
-- ---------------------------------------------------------------------
CREATE TABLE clientes (
    id_cliente          INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo      VARCHAR(120) NOT NULL,
    tipo_documento       VARCHAR(20)  NOT NULL,
    numero_documento     VARCHAR(30)  NOT NULL,
    telefono             VARCHAR(20)  NOT NULL,
    email                VARCHAR(120),
    direccion            VARCHAR(200) NOT NULL,
    fecha_registro       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_clientes_documento UNIQUE (tipo_documento, numero_documento)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: vehiculos
-- ---------------------------------------------------------------------
CREATE TABLE vehiculos (
    id_vehiculo          INT AUTO_INCREMENT PRIMARY KEY,
    placa                VARCHAR(10) NOT NULL UNIQUE,
    marca                VARCHAR(50) NOT NULL,
    modelo               VARCHAR(50) NOT NULL,
    anio_fabricacion     SMALLINT NOT NULL,
    capacidad_carga_kg   DECIMAL(8,2) NOT NULL,
    estado               ENUM('Disponible', 'En Ruta', 'En Mantenimiento')
                             NOT NULL DEFAULT 'Disponible',
    fecha_registro       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_vehiculos_anio CHECK (anio_fabricacion BETWEEN 1980 AND 2100),
    CONSTRAINT chk_vehiculos_capacidad CHECK (capacidad_carga_kg > 0)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: conductores
-- ---------------------------------------------------------------------
CREATE TABLE conductores (
    id_conductor          INT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo       VARCHAR(120) NOT NULL,
    tipo_licencia         VARCHAR(10) NOT NULL,
    numero_contacto       VARCHAR(20) NOT NULL,
    estado                ENUM('Activo', 'De Vacaciones', 'Inactivo')
                              NOT NULL DEFAULT 'Activo',
    fecha_registro        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: mantenimientos_vehiculo
-- Historial de mantenimientos preventivos/correctivos de cada vehiculo.
-- ---------------------------------------------------------------------
CREATE TABLE mantenimientos_vehiculo (
    id_mantenimiento      INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo           INT NOT NULL,
    tipo_mantenimiento    ENUM('Preventivo', 'Correctivo') NOT NULL,
    fecha_mantenimiento   DATE NOT NULL,
    descripcion           VARCHAR(255) NOT NULL,
    costo                 DECIMAL(10,2) NOT NULL DEFAULT 0,
    taller                VARCHAR(100),
    fecha_registro        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mantenimiento_vehiculo
        FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: asignaciones_vehiculo_conductor
-- Historial de asignacion de un conductor a un vehiculo. Un conductor
-- solo puede tener una asignacion en estado 'Activa' a la vez; esta
-- regla se valida a nivel de aplicacion (capa de servicio) antes de
-- insertar un nuevo registro.
-- ---------------------------------------------------------------------
CREATE TABLE asignaciones_vehiculo_conductor (
    id_asignacion         INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo           INT NOT NULL,
    id_conductor          INT NOT NULL,
    fecha_asignacion      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_liberacion      DATETIME NULL,
    estado                ENUM('Activa', 'Finalizada') NOT NULL DEFAULT 'Activa',
    CONSTRAINT fk_asignacion_vehiculo
        FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_conductor
        FOREIGN KEY (id_conductor) REFERENCES conductores(id_conductor)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: paquetes
-- ---------------------------------------------------------------------
CREATE TABLE paquetes (
    id_paquete            INT AUTO_INCREMENT PRIMARY KEY,
    codigo_seguimiento    VARCHAR(20) NOT NULL UNIQUE,
    descripcion_contenido VARCHAR(255) NOT NULL,
    peso_kg               DECIMAL(6,2) NOT NULL,
    dimensiones           VARCHAR(50) NOT NULL,
    direccion_origen      VARCHAR(200) NOT NULL,
    direccion_destino     VARCHAR(200) NOT NULL,
    id_remitente          INT NOT NULL,
    id_destinatario       INT NOT NULL,
    estado                ENUM('En Bodega', 'Asignado a Ruta', 'En Transito',
                                'Entregado', 'Devuelto')
                              NOT NULL DEFAULT 'En Bodega',
    fecha_registro        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_paquetes_peso CHECK (peso_kg > 0),
    CONSTRAINT fk_paquete_remitente
        FOREIGN KEY (id_remitente) REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_paquete_destinatario
        FOREIGN KEY (id_destinatario) REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: rutas
-- Representa la "hoja de ruta" diaria: un vehiculo y su conductor
-- asignados para distribuir un conjunto de paquetes.
-- ---------------------------------------------------------------------
CREATE TABLE rutas (
    id_ruta               INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo           INT NOT NULL,
    id_conductor          INT NOT NULL,
    fecha_ruta            DATE NOT NULL,
    hora_inicio           DATETIME NULL,
    hora_fin              DATETIME NULL,
    estado                ENUM('Planificada', 'En Curso', 'Finalizada')
                              NOT NULL DEFAULT 'Planificada',
    carga_total_kg        DECIMAL(8,2) NOT NULL DEFAULT 0,
    fecha_creacion         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ruta_vehiculo
        FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_ruta_conductor
        FOREIGN KEY (id_conductor) REFERENCES conductores(id_conductor)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: ruta_paquetes
-- Tabla de union entre rutas y paquetes (una ruta agrupa muchos
-- paquetes; en la practica cada paquete pertenece a una sola ruta
-- activa, pero se modela como N:M para permitir reintentos/devoluciones
-- reasignadas a una nueva ruta).
-- ---------------------------------------------------------------------
CREATE TABLE ruta_paquetes (
    id_ruta_paquete       INT AUTO_INCREMENT PRIMARY KEY,
    id_ruta               INT NOT NULL,
    id_paquete            INT NOT NULL,
    orden_entrega         INT NOT NULL DEFAULT 1,
    estado_entrega        ENUM('Pendiente', 'Entregado', 'Devuelto')
                              NOT NULL DEFAULT 'Pendiente',
    fecha_entrega         DATETIME NULL,
    CONSTRAINT uq_ruta_paquete UNIQUE (id_ruta, id_paquete),
    CONSTRAINT fk_rutapaquete_ruta
        FOREIGN KEY (id_ruta) REFERENCES rutas(id_ruta)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_rutapaquete_paquete
        FOREIGN KEY (id_paquete) REFERENCES paquetes(id_paquete)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: auditoria
-- Bitacora de operaciones criticas del sistema (creacion de paquetes,
-- inicio/fin de ruta, cambios de estado relevantes, etc.).
-- ---------------------------------------------------------------------
CREATE TABLE auditoria (
    id_auditoria          INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_operador      VARCHAR(100) NOT NULL,
    accion                VARCHAR(100) NOT NULL,
    tabla_afectada        VARCHAR(50) NOT NULL,
    id_entidad_afectada   INT,
    descripcion           VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Indices adicionales para consultas frecuentes
-- ---------------------------------------------------------------------
CREATE INDEX idx_paquetes_estado ON paquetes(estado);
CREATE INDEX idx_vehiculos_estado ON vehiculos(estado);
CREATE INDEX idx_conductores_estado ON conductores(estado);
CREATE INDEX idx_rutas_fecha ON rutas(fecha_ruta);
CREATE INDEX idx_mantenimiento_vehiculo ON mantenimientos_vehiculo(id_vehiculo);
CREATE INDEX idx_asignacion_conductor_estado ON asignaciones_vehiculo_conductor(id_conductor, estado);
