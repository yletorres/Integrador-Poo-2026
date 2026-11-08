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

-- =====================================================================
-- DATOS DE PRUEBA (basados en el ejemplo de la consigna)
-- =====================================================================

INSERT INTO biblioteca (idBiblioteca, nombre) VALUES (1, 'Biblioteca Central');

INSERT INTO libro (idLibro, titulo, edicion, editorial, anio, idBiblioteca) VALUES
    (1,  'JAVA. Como Programar',                      1, 'Pearson',      2012, 1),
    (2,  'JAVA. Como Programar',                      1, 'Pearson',      2012, 1),
    (3,  'Longman. Diccionario Pocket',               1, 'Longman',      2010, 1),
    (4,  'Vivir para contarla',                       1, 'Sudamericana', 2002, 1),
    (5,  'Programando con JAVA',                      1, 'Alfaomega',    2015, 1),
    (6,  'Estructuras de Datos en Java',              1, 'Pearson',      2014, 1),
    (7,  'Bases de Datos: Conceptos Fundamentales',   1, 'Addison',      2011, 1),
    (8,  'Sistemas Operativos Modernos',              1, 'Pearson',      2009, 1),
    (9,  'Redes de Computadoras',                     1, 'Pearson',      2013, 1),
    (10, 'Ingeniería de Software',                    1, 'Pearson',      2011, 1),
    (11, 'Algoritmos y Programación',                 1, 'Alfaomega',    2008, 1),
    (12, 'Matemática Discreta',                       1, 'Prentice',     2007, 1),
    (13, 'Cálculo en Una Variable',                   1, 'Cengage',      2012, 1),
    (14, 'Física Universitaria',                      1, 'Pearson',      2013, 1);

-- Primero la fila de socio y después la de su subtabla.
-- Docentes: arrancan con 5 días; los responsables tienen días de premio.
-- Estudiantes: 20 días de préstamo.
INSERT INTO socio (dniSocio, nombre, diasPrestamo, tipo, idBiblioteca) VALUES
    (14524782, 'Juan Perez',      25, 'Docente',    1),
    (17982110, 'Juan Fernández',  20, 'Docente',    1),
    (10912002, 'María Alegre',     5, 'Docente',    1),
    (28987498, 'Francisco Paezo', 20, 'Estudiante', 1),
    (31987123, 'Cesar Milstein',  20, 'Estudiante', 1);

INSERT INTO docente (dniSocio, area) VALUES
    (14524782, 'Programación'),
    (17982110, 'Bases de Datos'),
    (10912002, 'Matemática');

INSERT INTO estudiante (dniSocio, carrera) VALUES
    (28987498, 'Licenciatura en Sistemas'),
    (31987123, 'Licenciatura en Sistemas');

-- Préstamos activos (fechaDevolucion NULL)
--   Juan Perez       6 libros  (vencen en noviembre: no hay vencidos)
--   Juan Fernández   1 libro
--   Francisco Paezo  2 libros  (retirados el 5/10 con 20 días: VENCIDOS)
--   Cesar Milstein   3 libros  (tiene "Programando con JAVA")
--   María Alegre     0 libros
INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) VALUES
    ('2026-10-28', NULL, 14524782, 1),
    ('2026-10-28', NULL, 14524782, 2),
    ('2026-10-28', NULL, 14524782, 3),
    ('2026-10-28', NULL, 14524782, 6),
    ('2026-10-28', NULL, 14524782, 7),
    ('2026-10-28', NULL, 14524782, 8),
    ('2026-10-30', NULL, 17982110, 9),
    ('2026-10-05', NULL, 28987498, 10),
    ('2026-10-05', NULL, 28987498, 11),
    ('2026-10-20', NULL, 31987123, 5),
    ('2026-10-20', NULL, 31987123, 4),
    ('2026-10-20', NULL, 31987123, 12);

-- Préstamo ya devuelto: María Alegre (5 días) lo devolvió a los 19 días,
-- o sea con atraso -> NO es docente responsable
INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) VALUES
    ('2026-09-01', '2026-09-20', 10912002, 13);

-- El libro 14 queda sin prestar (para probar prestarLibro)
