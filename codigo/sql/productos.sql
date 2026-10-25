-- Definición de la tabla del Sistema de Gestión de Productos (H2).
-- Referencia: la aplicación ejecuta esta sentencia sola al iniciar (ConexionBD.java).

CREATE TABLE IF NOT EXISTS productos (
    codigo      VARCHAR(20)    NOT NULL,
    nombre      VARCHAR(100)   NOT NULL,
    descripcion VARCHAR(255),
    precio      DECIMAL(10,2)  NOT NULL,
    cantidad    INT            NOT NULL,
    CONSTRAINT pk_productos PRIMARY KEY (codigo),
    CONSTRAINT chk_precio   CHECK (precio >= 0),
    CONSTRAINT chk_cantidad CHECK (cantidad >= 0)
);
