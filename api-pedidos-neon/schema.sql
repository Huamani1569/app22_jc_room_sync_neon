CREATE TABLE IF NOT EXISTS pedidos (
    id             SERIAL PRIMARY KEY,
    uuid           UUID         NOT NULL UNIQUE,              -- generado en el celular; evita duplicados
    producto       VARCHAR(120) NOT NULL,
    cantidad       INTEGER      NOT NULL CHECK (cantidad > 0),
    fecha_registro TIMESTAMPTZ  NOT NULL,                     -- cuándo se registró en el celular
    recibido_en    TIMESTAMPTZ  NOT NULL DEFAULT now()        -- cuándo llegó a Neon
);
