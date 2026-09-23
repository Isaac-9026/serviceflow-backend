CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE perfiles_tecnicos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT UNIQUE NOT NULL REFERENCES usuarios(id),
    especialidades VARCHAR(255),
    zona VARCHAR(100)
);

CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    direccion VARCHAR(255),
    notas TEXT,
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorias_servicio (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE solicitudes_servicio (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    categoria_id BIGINT REFERENCES categorias_servicio(id),
    descripcion TEXT NOT NULL,
    direccion VARCHAR(255),
    estado VARCHAR(50) NOT NULL,
    creado_por BIGINT REFERENCES usuarios(id),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cotizaciones (
    id BIGSERIAL PRIMARY KEY,
    solicitud_id BIGINT NOT NULL REFERENCES solicitudes_servicio(id),
    descripcion TEXT,
    monto DECIMAL(12,2) NOT NULL,
    valido_hasta DATE,
    estado VARCHAR(50) NOT NULL,
    aprobado_por BIGINT REFERENCES usuarios(id),
    aprobado_en TIMESTAMP,
    creado_por BIGINT REFERENCES usuarios(id),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ordenes_trabajo (
    id BIGSERIAL PRIMARY KEY,
    solicitud_id BIGINT UNIQUE NOT NULL REFERENCES solicitudes_servicio(id),
    cotizacion_id BIGINT REFERENCES cotizaciones(id),
    descripcion TEXT,
    fecha_programada TIMESTAMP,
    estado VARCHAR(50) NOT NULL,
    completado_en TIMESTAMP,
    creado_por BIGINT REFERENCES usuarios(id),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE asignaciones_orden (
    id BIGSERIAL PRIMARY KEY,
    orden_id BIGINT NOT NULL REFERENCES ordenes_trabajo(id),
    tecnico_id BIGINT NOT NULL REFERENCES usuarios(id),
    es_principal BOOLEAN DEFAULT FALSE,
    asignado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evidencias (
    id BIGSERIAL PRIMARY KEY,
    orden_id BIGINT NOT NULL REFERENCES ordenes_trabajo(id),
    tipo VARCHAR(50) NOT NULL,
    ruta_archivo VARCHAR(500),
    nota TEXT,
    subido_por BIGINT REFERENCES usuarios(id),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE registros_auditoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuarios(id),
    accion VARCHAR(100) NOT NULL,
    tipo_entidad VARCHAR(50) NOT NULL,
    entidad_id BIGINT NOT NULL,
    detalles TEXT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
