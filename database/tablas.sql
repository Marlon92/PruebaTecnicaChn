CREATE TABLE cliente (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_identificacion VARCHAR(13) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(255) NULL,
    correo VARCHAR(150) NULL,
    telefono VARCHAR(15) NULL,
    fecha_registro DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT cliente_identificacion UNIQUE (numero_identificacion)
);

CREATE TABLE solicitud_prestamo (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    monto_solicitado DECIMAL(12,2) NOT NULL,
    plazo_meses INT NOT NULL,
    motivo VARCHAR(255) NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_solicitud DATETIME2 NOT NULL DEFAULT GETDATE(),
    tasa_interes DECIMAL(5,2) NULL,
    monto_total_pagar DECIMAL(12,2) NULL,
    fecha_resolucion DATETIME2 NULL,
    comentario_resolucion VARCHAR(255) NULL,
    CONSTRAINT fk_solicitud_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
);

CREATE TABLE pago (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_pago DATETIME2 NOT NULL DEFAULT GETDATE(),
    observacion VARCHAR(255) NULL,
    CONSTRAINT fk_pago_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitud_prestamo(id) ON DELETE CASCADE
);
