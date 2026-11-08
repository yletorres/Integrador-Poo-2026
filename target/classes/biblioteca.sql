-- =====================================================================
-- Gestión de Biblioteca - Script de base de datos (SQLite)
-- TP7 - Programación Orientada a Objetos
--
-- Cómo usarlo en DB Browser for SQLite:
--   1. File > New Database > guardar como biblioteca.db en la carpeta Modelo (src/Modelo)
--   2. Pestaña "Execute SQL" > abrir este archivo > ejecutar todo (F5)
--   3. Write Changes (Ctrl+S)
--
-- Modelo (una tabla por clase, la herencia se respeta):
--   biblioteca  <- Biblioteca
--   libro       <- Libro
--   socio       <- Socio (datos comunes)
--   docente     <- Docente    (solo dni + area)
--   estudiante  <- Estudiante (solo dni + carrera)
--   prestamo    <- Prestamo
--
-- Fechas: se guardan como texto 'AAAA-MM-DD'. Valen para new GregorianCalendar(...)
-- Foreign keys: SQLite las ignora salvo que cada conexión active
--   PRAGMA foreign_keys = ON;   (ya está en la clase Conexion)
-- =====================================================================

-- Se borran en orden inverso a las dependencias, para poder re-ejecutar el script
DROP TABLE IF EXISTS prestamo;
DROP TABLE IF EXISTS estudiante;
DROP TABLE IF EXISTS docente;
DROP TABLE IF EXISTS socio;
DROP TABLE IF EXISTS libro;
DROP TABLE IF EXISTS biblioteca;

-- ---------------------------------------------------------------------
-- BIBLIOTECA
-- ---------------------------------------------------------------------
CREATE TABLE biblioteca (
    idBiblioteca INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre       TEXT    NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------
-- LIBRO  (cada fila es un ejemplar; un título puede estar repetido)
-- ---------------------------------------------------------------------
CREATE TABLE libro (
    idLibro      INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo       TEXT    NOT NULL,
    edicion      INTEGER NOT NULL DEFAULT 1,
    editorial    TEXT    NOT NULL,
    anio         INTEGER NOT NULL,
    idBiblioteca INTEGER NOT NULL,
    FOREIGN KEY (idBiblioteca) REFERENCES biblioteca (idBiblioteca)
);

-- ---------------------------------------------------------------------
-- SOCIO  (superclase: datos comunes a docentes y estudiantes)
--   tipo + UNIQUE(dniSocio, tipo) permiten que las subtablas garanticen
--   que cada socio sea de UN solo tipo.
-- ---------------------------------------------------------------------
CREATE TABLE socio (
    dniSocio     INTEGER PRIMARY KEY,
    nombre       TEXT    NOT NULL,
    diasPrestamo INTEGER NOT NULL CHECK (diasPrestamo > 0),
    tipo         TEXT    NOT NULL CHECK (tipo IN ('Docente', 'Estudiante')),
    idBiblioteca INTEGER NOT NULL,
    UNIQUE (dniSocio, tipo),
    FOREIGN KEY (idBiblioteca) REFERENCES biblioteca (idBiblioteca)
);

-- ---------------------------------------------------------------------
-- DOCENTE  (subclase: solo el dni y su dato propio, sin columnas vacías)
-- ---------------------------------------------------------------------
CREATE TABLE docente (
    dniSocio INTEGER PRIMARY KEY,
    tipo     TEXT    NOT NULL DEFAULT 'Docente' CHECK (tipo = 'Docente'),
    area     TEXT    NOT NULL,
    FOREIGN KEY (dniSocio, tipo) REFERENCES socio (dniSocio, tipo)
);

-- ---------------------------------------------------------------------
-- ESTUDIANTE  (subclase: solo el dni y su dato propio)
-- ---------------------------------------------------------------------
CREATE TABLE estudiante (
    dniSocio INTEGER PRIMARY KEY,
    tipo     TEXT    NOT NULL DEFAULT 'Estudiante' CHECK (tipo = 'Estudiante'),
    carrera  TEXT    NOT NULL,
    FOREIGN KEY (dniSocio, tipo) REFERENCES socio (dniSocio, tipo)
);

-- ---------------------------------------------------------------------
-- PRESTAMO
--   fechaDevolucion NULL = el libro todavía no se devolvió
--   (no reemplazar por otra fecha: Libro.prestado() depende de eso)
--   Las claves foráneas impiden quitar un socio o libro con préstamos.
-- ---------------------------------------------------------------------
CREATE TABLE prestamo (
    idPrestamo      INTEGER PRIMARY KEY AUTOINCREMENT,
    fechaRetiro     TEXT    NOT NULL,
    fechaDevolucion TEXT,
    dniSocio        INTEGER NOT NULL,
    idLibro         INTEGER NOT NULL,
    FOREIGN KEY (dniSocio) REFERENCES socio (dniSocio),
    FOREIGN KEY (idLibro)  REFERENCES libro (idLibro),
    CHECK (fechaDevolucion IS NULL OR fechaDevolucion >= fechaRetiro)
);

