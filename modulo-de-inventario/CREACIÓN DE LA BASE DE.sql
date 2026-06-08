tu ddl -- 1. CREACIÓN DE LA BASE DE DATOS
CREATE DATABASE inventario_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;

-- 2. SELECCIÓN DE LA BASE DE DATOS PARA SU USO
USE inventario_db;

-- 3. CREACIÓN DE LA TABLA: CATEGORIAS (Tabla Padre)
CREATE TABLE categorias (
  id_categoria BIGINT(20) NOT NULL AUTO_INCREMENT,
  nombre_categoria VARCHAR(100) NOT NULL,
  codigo_prefijo VARCHAR(3) NOT NULL,
  PRIMARY KEY (id_categoria),
  UNIQUE KEY UK_codigo_prefijo (codigo_prefijo)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4. CREACIÓN DE LA TABLA: ACTIVOS_TECNOLOGICOS (Tabla Hijo)
CREATE TABLE activos_tecnologicos (
  id_tecnico UUID NOT NULL,
  costo_adquisicion DECIMAL(12,2) NOT NULL,
  estado VARCHAR(30) NOT NULL,
  fecha_ingreso DATETIME(6) DEFAULT NULL,
  folio_inventario VARCHAR(50) NOT NULL,
  marca_modelo VARCHAR(255) NOT NULL,
  numero_serie VARCHAR(100) NOT NULL,
  id_categoria BIGINT(20) NOT NULL,
  PRIMARY KEY (id_tecnico),
  UNIQUE KEY UK_folio_inventario (folio_inventario),
  UNIQUE KEY UK_numero_serie (numero_serie),
  KEY FK_id_categoria_idx (id_categoria),
  CONSTRAINT FK_activos_categorias 
    FOREIGN KEY (id_categoria) 
    REFERENCES categorias (id_categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;