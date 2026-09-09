-- =====================================================================
-- RapidExpress - Extension: Gestion de Multas y Pagos de Conductores
-- Script DDL adicional (no modifica las tablas existentes)
-- =====================================================================

USE rapidexpress_db;

CREATE TABLE multas_conductor (
    id_multa            INT AUTO_INCREMENT PRIMARY KEY,
    id_conductor        INT NOT NULL,
    motivo              VARCHAR(150) NOT NULL,
    monto_original      DECIMAL(10,2) NOT NULL,
    saldo_pendiente     DECIMAL(10,2) NOT NULL,
    fecha_multa         DATE NOT NULL,
    fecha_ultimo_abono  DATETIME NULL,
    estado              ENUM('Pendiente', 'Pagada') NOT NULL DEFAULT 'Pendiente',
    CONSTRAINT chk_multa_montos CHECK (monto_original > 0 AND saldo_pendiente >= 0),
    CONSTRAINT fk_multa_conductor
        FOREIGN KEY (id_conductor) REFERENCES conductores(id_conductor)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_multas_conductor ON multas_conductor(id_conductor);
CREATE INDEX idx_multas_estado ON multas_conductor(estado);
