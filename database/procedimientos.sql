
CREATE OR ALTER PROCEDURE sp_cliente_listar
AS
BEGIN
    SELECT
        id,
        nombre,
        apellido,
        numero_identificacion,
        fecha_nacimiento,
        direccion,
        correo,
        telefono,
        fecha_registro
    FROM cliente
    ORDER BY apellido, nombre;
END
GO

CREATE OR ALTER PROCEDURE sp_cliente_obtener
    @id BIGINT
AS
BEGIN
    SELECT
        id,
        nombre,
        apellido,
        numero_identificacion,
        fecha_nacimiento,
        direccion,
        correo,
        telefono,
        fecha_registro
    FROM cliente
    WHERE id = @id;
END
GO

CREATE OR ALTER PROCEDURE sp_cliente_buscar_por_identificacion
    @numeroIdentificacion VARCHAR(30)
AS
BEGIN
    SELECT
        id,
        nombre,
        apellido,
        numero_identificacion,
        fecha_nacimiento,
        direccion,
        correo,
        telefono,
        fecha_registro
    FROM cliente
    WHERE numero_identificacion = @numeroIdentificacion;
END
GO

CREATE OR ALTER PROCEDURE sp_cliente_crear
    @nombre VARCHAR(100),
    @apellido VARCHAR(100),
    @numeroIdentificacion VARCHAR(13),
    @fechaNacimiento DATE,
    @direccion VARCHAR(255),
    @correo VARCHAR(150),
    @telefono VARCHAR(15),
    @id BIGINT OUTPUT
AS
BEGIN
    INSERT INTO cliente (
        nombre,
        apellido,
        numero_identificacion,
        fecha_nacimiento,
        direccion,
        correo,
        telefono,
        fecha_registro
    )
    VALUES (
        @nombre,
        @apellido,
        @numeroIdentificacion,
        @fechaNacimiento,
        @direccion,
        @correo,
        @telefono,
        GETDATE()
    );

    SET @id = SCOPE_IDENTITY();
END
GO

CREATE OR ALTER PROCEDURE sp_cliente_actualizar
    @id BIGINT,
    @nombre VARCHAR(100),
    @apellido VARCHAR(100),
    @numeroIdentificacion VARCHAR(13),
    @fechaNacimiento DATE,
    @direccion VARCHAR(255),
    @correo VARCHAR(150),
    @telefono VARCHAR(15)
AS
BEGIN
    UPDATE cliente
    SET
        nombre = @nombre,
        apellido = @apellido,
        numero_identificacion = @numeroIdentificacion,
        fecha_nacimiento = @fechaNacimiento,
        direccion = @direccion,
        correo = @correo,
        telefono = @telefono
    WHERE id = @id;
END
GO

CREATE OR ALTER PROCEDURE sp_cliente_eliminar
    @id BIGINT
AS
BEGIN
    DELETE FROM cliente
    WHERE id = @id;
END
GO

CREATE OR ALTER PROCEDURE sp_solicitud_listar
AS
BEGIN
    SELECT
        s.id,
        s.cliente_id,
        c.nombre,
        c.apellido,
        s.monto_solicitado,
        s.plazo_meses,
        s.motivo,
        s.estado,
        s.fecha_solicitud,
        s.tasa_interes,
        s.monto_total_pagar,
        s.fecha_resolucion,
        s.comentario_resolucion
    FROM solicitud_prestamo s
    INNER JOIN cliente c ON c.id = s.cliente_id
    ORDER BY s.fecha_solicitud DESC;
END
GO

CREATE OR ALTER PROCEDURE sp_solicitud_listar_por_cliente
    @clienteId BIGINT
AS
BEGIN
    SELECT
        s.id,
        s.cliente_id,
        c.nombre,
        c.apellido,
        s.monto_solicitado,
        s.plazo_meses,
        s.motivo,
        s.estado,
        s.fecha_solicitud,
        s.tasa_interes,
        s.monto_total_pagar,
        s.fecha_resolucion,
        s.comentario_resolucion
    FROM solicitud_prestamo s
    INNER JOIN cliente c ON c.id = s.cliente_id
    WHERE s.cliente_id = @clienteId
    ORDER BY s.fecha_solicitud DESC;
END
GO

CREATE OR ALTER PROCEDURE sp_solicitud_obtener
    @id BIGINT
AS
BEGIN
    SELECT
        s.id,
        s.cliente_id,
        c.nombre,
        c.apellido,
        s.monto_solicitado,
        s.plazo_meses,
        s.motivo,
        s.estado,
        s.fecha_solicitud,
        s.tasa_interes,
        s.monto_total_pagar,
        s.fecha_resolucion,
        s.comentario_resolucion
    FROM solicitud_prestamo s
    INNER JOIN cliente c ON c.id = s.cliente_id
    WHERE s.id = @id;
END
GO

CREATE OR ALTER PROCEDURE sp_solicitud_crear
    @clienteId BIGINT,
    @montoSolicitado DECIMAL(12,2),
    @plazoMeses INT,
    @motivo VARCHAR(255),
    @id BIGINT OUTPUT
AS
BEGIN
    INSERT INTO solicitud_prestamo (
        cliente_id,
        monto_solicitado,
        plazo_meses,
        motivo,
        estado,
        fecha_solicitud
    )
    VALUES (
        @clienteId,
        @montoSolicitado,
        @plazoMeses,
        @motivo,
        'PENDIENTE',
        GETDATE()
    );

    SET @id = SCOPE_IDENTITY();
END
GO

CREATE OR ALTER PROCEDURE sp_solicitud_aprobar
    @id BIGINT,
    @tasaInteres DECIMAL(5,2),
    @montoTotalPagar DECIMAL(12,2),
    @comentario VARCHAR(255)
AS
BEGIN
    UPDATE solicitud_prestamo
    SET
        estado = 'APROBADA',
        tasa_interes = @tasaInteres,
        monto_total_pagar = @montoTotalPagar,
        fecha_resolucion = GETDATE(),
        comentario_resolucion = @comentario
    WHERE id = @id;
END
GO

CREATE OR ALTER PROCEDURE sp_solicitud_rechazar
    @id BIGINT,
    @comentario VARCHAR(255)
AS
BEGIN
    UPDATE solicitud_prestamo
    SET
        estado = 'RECHAZADA',
        fecha_resolucion = GETDATE(),
        comentario_resolucion = @comentario
    WHERE id = @id;
END
GO

CREATE OR ALTER PROCEDURE sp_pago_listar_por_solicitud
    @solicitudId BIGINT
AS
BEGIN
    SELECT
        id,
        solicitud_id,
        monto,
        fecha_pago,
        observacion
    FROM pago
    WHERE solicitud_id = @solicitudId
    ORDER BY fecha_pago DESC;
END
GO

CREATE OR ALTER PROCEDURE sp_pago_crear
    @solicitudId BIGINT,
    @monto DECIMAL(12,2),
    @observacion VARCHAR(255),
    @id BIGINT OUTPUT,
    @fechaPago DATETIME2 OUTPUT
AS
BEGIN
    SET @fechaPago = GETDATE();

    INSERT INTO pago (
        solicitud_id,
        monto,
        fecha_pago,
        observacion
    )
    VALUES (
        @solicitudId,
        @monto,
        @fechaPago,
        @observacion
    );

    SET @id = SCOPE_IDENTITY();
END
GO

CREATE OR ALTER PROCEDURE sp_pago_total_por_solicitud
    @solicitudId BIGINT,
    @total DECIMAL(12,2) OUTPUT
AS
BEGIN
    SELECT @total = ISNULL(SUM(monto), 0)
    FROM pago
    WHERE solicitud_id = @solicitudId;
END
GO
