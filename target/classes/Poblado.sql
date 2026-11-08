-- =====================================================================
-- Gestión de Biblioteca - Script para poblar la base de datos (SQLite)
-- TP7 - Programación Orientada a Objetos
--
-- Ejecutar DESPUÉS del script que crea las tablas.
-- Se puede re-ejecutar: primero vacía las tablas.
--
-- Cada fila de libro es un ejemplar: los títulos repetidos son copias.
-- El año es el de la publicación original.
-- =====================================================================

PRAGMA foreign_keys = ON;

-- Se vacían en orden inverso a las dependencias
DELETE FROM prestamo;
DELETE FROM estudiante;
DELETE FROM docente;
DELETE FROM socio;
DELETE FROM libro;
DELETE FROM biblioteca;

-- ---------------------------------------------------------------------
-- BIBLIOTECAS
-- ---------------------------------------------------------------------
INSERT INTO biblioteca (idBiblioteca, nombre) VALUES
    (1, 'Biblioteca Central'),
    (2, 'Biblioteca de Ciencias Exactas'),
    (3, 'Biblioteca Popular');

-- ---------------------------------------------------------------------
-- LIBROS - Biblioteca Central (1): clásicos, poesía y filosofía
-- ---------------------------------------------------------------------
INSERT INTO libro (idLibro, titulo, edicion, editorial, anio, idBiblioteca) VALUES
    -- Jane Austen
    (1,  'Orgullo y prejuicio',                  1, 'Alianza Editorial', 1813, 1),
    (2,  'Orgullo y prejuicio',                  1, 'Alianza Editorial', 1813, 1),
    (3,  'Sentido y sensibilidad',               1, 'Alianza Editorial', 1811, 1),
    (4,  'Emma',                                 1, 'Alianza Editorial', 1815, 1),
    (5,  'Persuasión',                           1, 'Alianza Editorial', 1817, 1),
    (6,  'La abadía de Northanger',              1, 'Alianza Editorial', 1817, 1),
    (7,  'Mansfield Park',                       1, 'Alianza Editorial', 1814, 1),
    -- Dostoievski
    (8,  'Crimen y castigo',                     1, 'Alianza Editorial', 1866, 1),
    (9,  'Crimen y castigo',                     1, 'Alianza Editorial', 1866, 1),
    (10, 'Los hermanos Karamázov',               1, 'Alianza Editorial', 1880, 1),
    (11, 'El idiota',                            1, 'Alianza Editorial', 1869, 1),
    (12, 'Memorias del subsuelo',                1, 'Alianza Editorial', 1864, 1),
    (13, 'Noches blancas',                       1, 'Losada',            1848, 1),
    (14, 'Los demonios',                         1, 'Alianza Editorial', 1872, 1),
    -- Sartre
    (15, 'La náusea',                            1, 'Losada',            1938, 1),
    (16, 'El ser y la nada',                     1, 'Losada',            1943, 1),
    (17, 'A puerta cerrada',                     1, 'Losada',            1944, 1),
    (18, 'El existencialismo es un humanismo',   1, 'Edhasa',            1946, 1),
    (19, 'La edad de la razón',                  1, 'Losada',            1945, 1),
    (20, 'Las palabras',                         1, 'Losada',            1964, 1),
    -- Kafka
    (21, 'La metamorfosis',                      1, 'Alianza Editorial', 1915, 1),
    (22, 'La metamorfosis',                      1, 'Alianza Editorial', 1915, 1),
    (23, 'El proceso',                           1, 'Alianza Editorial', 1925, 1),
    (24, 'El castillo',                          1, 'Alianza Editorial', 1926, 1),
    (25, 'Carta al padre',                       1, 'Alianza Editorial', 1952, 1),
    (26, 'La condena',                           1, 'Alianza Editorial', 1913, 1),
    -- Camus y María Casarès
    (27, 'Correspondencia 1944-1959',            1, 'Anagrama',          2017, 1),
    (28, 'Correspondencia 1944-1959',            1, 'Anagrama',          2017, 1),
    -- Alejandra Pizarnik
    (29, 'Árbol de Diana',                       1, 'Sudamericana',      1962, 1),
    (30, 'Los trabajos y las noches',            1, 'Sudamericana',      1965, 1),
    (31, 'Extracción de la piedra de locura',    1, 'Sudamericana',      1968, 1),
    (32, 'La condesa sangrienta',                1, 'Libros del Zorro Rojo', 1971, 1),
    (33, 'Diarios',                              1, 'Lumen',             2003, 1),
    (34, 'Poesía completa',                      1, 'Lumen',             2000, 1),
    -- Alfonsina Storni
    (35, 'Languidez',                            1, 'Losada',            1920, 1),
    (36, 'Ocre',                                 1, 'Losada',            1925, 1),
    (37, 'Mundo de siete pozos',                 1, 'Losada',            1934, 1),
    (38, 'Mascarilla y trébol',                  1, 'Losada',            1938, 1);

-- ---------------------------------------------------------------------
-- LIBROS - Biblioteca Popular (3): fantasía, terror, manga, cómics y best sellers
-- ---------------------------------------------------------------------
INSERT INTO libro (idLibro, titulo, edicion, editorial, anio, idBiblioteca) VALUES
    -- Isabel Allende
    (39, 'La casa de los espíritus',             1, 'Sudamericana',      1982, 3),
    (40, 'La casa de los espíritus',             1, 'Sudamericana',      1982, 3),
    (41, 'Largo pétalo de mar',                  1, 'Plaza & Janés',     2019, 3),
    (42, 'Eva Luna',                             1, 'Plaza & Janés',     1987, 3),
    (43, 'Paula',                                1, 'Plaza & Janés',     1994, 3),
    (44, 'Hija de la fortuna',                   1, 'Plaza & Janés',     1999, 3),
    (45, 'De amor y de sombra',                  1, 'Plaza & Janés',     1984, 3),
    -- Brandon Sanderson: El archivo de las tormentas
    (46, 'El camino de los reyes',               1, 'Nova',              2010, 3),
    (47, 'Palabras radiantes',                   1, 'Nova',              2014, 3),
    (48, 'Juramentada',                          1, 'Nova',              2013, 3),
    (49, 'El ritmo de la guerra',                1, 'Nova',              2020, 3),
    (50, 'Viento y verdad',                      1, 'Nova',              2024, 3),
    -- Brandon Sanderson: Mistborn
    (51, 'El imperio final',                     1, 'Nova',              2006, 3),
    (52, 'El pozo de la ascensión',              1, 'Nova',              2007, 3),
    (53, 'El héroe de las eras',                 1, 'Nova',              2008, 3),
    (54, 'Aleaciones de la ley',                 1, 'Nova',              2011, 3),
    (55, 'Sombras de identidad',                 1, 'Nova',              2015, 3),
    (56, 'Brazales de duelo',                    1, 'Nova',              2016, 3),
    -- Brandon Sanderson: el resto del Cosmere
    (57, 'Elantris',                             1, 'Nova',              2005, 3),
    (58, 'El aliento de los dioses',             1, 'Nova',              2009, 3),
    (59, 'Tress del Mar Esmeralda',              1, 'Nova',              2023, 3),
    (60, 'Yumi y el pintor de pesadillas',       1, 'Nova',              2023, 3),
    (61, 'Arcanum ilimitado',                    1, 'Nova',              2016, 3),
    -- Stephen King
    (62, 'It',                                   1, 'Debolsillo',        1986, 3),
    (63, 'El resplandor',                        1, 'Debolsillo',        1977, 3),
    (64, 'Misery',                               1, 'Debolsillo',        1987, 3),
    (65, 'Carrie',                               1, 'Debolsillo',        1974, 3),
    (66, 'Cementerio de animales',               1, 'Debolsillo',        1983, 3),
    (67, 'La milla verde',                       1, 'Debolsillo',        1996, 3),
    (68, 'Doctor Sueño',                         1, 'Plaza & Janés',     2013, 3),
    (69, 'El instituto',                         1, 'Plaza & Janés',     2019, 3),
    (70, '11/22/63',                             1, 'Plaza & Janés',     2011, 3),
    (71, 'El pistolero (La Torre Oscura I)',     1, 'Debolsillo',        1982, 3),
    -- V. E. Schwab
    (72, 'La vida invisible de Addie LaRue',     1, 'Umbriel',           2020, 3),
    -- Patrick Rothfuss
    (73, 'El nombre del viento',                 1, 'Plaza & Janés',     2007, 3),
    (74, 'El temor de un hombre sabio',          1, 'Plaza & Janés',     2011, 3),
    (75, 'La música del silencio',               1, 'Plaza & Janés',     2014, 3),
    -- Nana (manga)
    (76, 'Nana Vol. 1',                          1, 'Planeta Cómic',     2000, 3),
    (77, 'Nana Vol. 2',                          1, 'Planeta Cómic',     2000, 3),
    (78, 'Nana Vol. 3',                          1, 'Planeta Cómic',     2001, 3),
    (79, 'Nana Vol. 4',                          1, 'Planeta Cómic',     2001, 3),
    (80, 'Nana Vol. 5',                          1, 'Planeta Cómic',     2001, 3),
    -- Spider-Man (cómics)
    (81, 'Spider-Man: La última cacería de Kraven', 1, 'Panini',         1987, 3),
    (82, 'Spider-Man: Azul',                     1, 'Panini',            2002, 3),
    (83, 'Ultimate Spider-Man: Poder y responsabilidad', 1, 'Panini',    2000, 3),
    (84, 'Spider-Man: Un día más',               1, 'Panini',            2007, 3),
    (85, 'Spider-Man: Reinado',                  1, 'Panini',            2007, 3),
    -- League of Legends (cómics)
    (86, 'League of Legends: Ashe, Madre de guerra', 1, 'Dark Horse',    2018, 3),
    (87, 'League of Legends: Lux',               1, 'Dark Horse',        2018, 3),
    (88, 'League of Legends: Zed',               1, 'Dark Horse',        2019, 3),
    (89, 'League of Legends: Reinos de Runaterra', 1, 'Planeta Cómic',   2019, 3),
    -- Best sellers
    (90,  'Harry Potter y la piedra filosofal',  1, 'Salamandra',        1997, 3),
    (91,  'El código Da Vinci',                  1, 'Umbriel',           2003, 3),
    (92,  'Cien años de soledad',                1, 'Sudamericana',      1967, 3),
    (93,  'Juego de tronos',                     1, 'Gigamesh',          1996, 3),
    (94,  'Los juegos del hambre',               1, 'RBA',               2008, 3),
    (95,  'El alquimista',                       1, 'Planeta',           1988, 3),
    (96,  'Sapiens: de animales a dioses',       1, 'Debate',            2011, 3),
    (97,  'El principito',                       1, 'Salamandra',        1943, 3),
    (98,  '1984',                                1, 'Debolsillo',        1949, 3),
    (99,  'Los pilares de la Tierra',            1, 'Debolsillo',        1989, 3),
    (100, 'Los hombres que no amaban a las mujeres', 1, 'Destino',       2005, 3),
    (101, 'Hábitos atómicos',                    1, 'Planeta',           2018, 3),
    (102, 'El nombre de la rosa',                1, 'Debolsillo',        1980, 3);

-- ---------------------------------------------------------------------
-- LIBROS - Biblioteca de Ciencias Exactas (2): programación y POO
-- ---------------------------------------------------------------------
INSERT INTO libro (idLibro, titulo, edicion, editorial, anio, idBiblioteca) VALUES
    (103, 'Java: cómo programar',                10, 'Pearson',          2016, 2),
    (104, 'Java: cómo programar',                10, 'Pearson',          2016, 2),
    (105, 'Piensa en Java',                       4, 'Pearson',          2002, 2),
    (106, 'Effective Java',                       3, 'Addison-Wesley',   2018, 2),
    (107, 'Clean Code',                           1, 'Prentice Hall',    2008, 2),
    (108, 'Head First Java',                      2, 'O''Reilly',        2005, 2),
    (109, 'Head First Design Patterns',           1, 'O''Reilly',        2004, 2),
    (110, 'Design Patterns',                      1, 'Addison-Wesley',   1994, 2),
    (111, 'Fundamentos de programación',          4, 'McGraw-Hill',      2008, 2),
    (112, 'Programación orientada a objetos',     2, 'McGraw-Hill',      1998, 2),
    (113, 'Programación orientada a objetos con Java', 4, 'Pearson',     2007, 2),
    (114, 'Introduction to Algorithms',           3, 'MIT Press',        2009, 2),
    (115, 'Refactoring',                          2, 'Addison-Wesley',   1999, 2),
    (116, 'El programador pragmático',            1, 'Addison-Wesley',   1999, 2),
    (117, 'UML y patrones',                       3, 'Pearson',          2003, 2);

-- ---------------------------------------------------------------------
-- SOCIOS (primero la tabla socio, después las subtablas)
--   Docentes: 15 días de préstamo - Estudiantes: 7 días
--   Central: 3 socios - Ciencias Exactas: 4 socios - Popular: 9 socios
-- ---------------------------------------------------------------------
INSERT INTO socio (dniSocio, nombre, diasPrestamo, tipo, idBiblioteca) VALUES
    -- Biblioteca Central
    (18234567, 'Marcela Fernández', 15, 'Docente',    1),
    (30123456, 'Lucía Benítez',      7, 'Estudiante', 1),
    (42987654, 'Tomás Aguirre',      7, 'Estudiante', 1),
    -- Biblioteca de Ciencias Exactas
    (17456789, 'Ricardo Domínguez', 15, 'Docente',    2),
    (20555111, 'Sergio Ledesma',    15, 'Docente',    2),
    (44012345, 'Camila Romero',      7, 'Estudiante', 2),
    (45678901, 'Joaquín Acosta',     7, 'Estudiante', 2),
    -- Biblioteca Popular
    (25789012, 'Silvia Ojeda',      15, 'Docente',    3),
    (27345678, 'Norma Cabrera',     15, 'Docente',    3),
    (41234567, 'Valentina Giménez',  7, 'Estudiante', 3),
    (43876543, 'Matías Sosa',        7, 'Estudiante', 3),
    (46123789, 'Agustina Villalba',  7, 'Estudiante', 3),
    (40567123, 'Bruno Escalante',    7, 'Estudiante', 3),
    (44789012, 'Julieta Maidana',    7, 'Estudiante', 3),
    (45901234, 'Franco Ibarra',      7, 'Estudiante', 3),
    (47012345, 'Delfina Pérez',      7, 'Estudiante', 3);

INSERT INTO docente (dniSocio, area) VALUES
    (18234567, 'Literatura'),
    (17456789, 'Programación'),
    (20555111, 'Bases de Datos'),
    (25789012, 'Lengua y Literatura'),
    (27345678, 'Historia');

INSERT INTO estudiante (dniSocio, carrera) VALUES
    (30123456, 'Licenciatura en Letras'),
    (42987654, 'Licenciatura en Filosofía'),
    (44012345, 'Licenciatura en Sistemas de Información'),
    (45678901, 'Ingeniería en Sistemas'),
    (41234567, 'Profesorado en Inglés'),
    (43876543, 'Diseño Gráfico'),
    (46123789, 'Psicología'),
    (40567123, 'Licenciatura en Comunicación Social'),
    (44789012, 'Arquitectura'),
    (45901234, 'Profesorado en Letras'),
    (47012345, 'Abogacía');

-- ---------------------------------------------------------------------
-- PRÉSTAMOS
--   fechaDevolucion NULL = todavía no se devolvió
--   Cada socio retira libros de su propia biblioteca
-- ---------------------------------------------------------------------
INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) VALUES
    -- Biblioteca Central
    ('2026-10-20', NULL,         18234567, 8),
    ('2026-09-01', '2026-09-12', 18234567, 21),
    ('2026-11-02', NULL,         30123456, 29),
    ('2026-10-05', '2026-10-11', 30123456, 35),
    ('2026-11-05', NULL,         30123456, 1),
    ('2026-10-28', NULL,         42987654, 15),
    ('2026-10-01', '2026-10-07', 42987654, 27),
    -- Biblioteca de Ciencias Exactas
    ('2026-10-10', NULL,         17456789, 106),
    ('2026-09-15', '2026-09-30', 17456789, 110),
    ('2026-11-01', NULL,         20555111, 114),
    ('2026-11-03', NULL,         44012345, 103),
    ('2026-10-12', '2026-10-18', 44012345, 107),
    ('2026-10-30', NULL,         45678901, 104),
    ('2026-09-20', '2026-09-27', 45678901, 108),
    -- Biblioteca Popular
    ('2026-10-25', NULL,         25789012, 39),
    ('2026-10-18', NULL,         27345678, 51),
    ('2026-11-01', NULL,         41234567, 72),
    ('2026-10-02', '2026-10-09', 41234567, 90),
    ('2026-09-10', '2026-09-17', 41234567, 76),
    ('2026-10-22', NULL,         43876543, 46),
    ('2026-10-05', '2026-10-10', 43876543, 81),
    ('2026-11-04', NULL,         43876543, 86),
    ('2026-10-29', NULL,         46123789, 62),
    ('2026-09-25', '2026-10-04', 46123789, 73),
    ('2026-11-06', NULL,         46123789, 77),
    ('2026-11-03', NULL,         40567123, 66),
    ('2026-09-28', '2026-10-05', 40567123, 91),
    ('2026-10-15', '2026-10-20', 44789012, 97),
    ('2026-11-02', NULL,         44789012, 41),
    ('2026-10-27', NULL,         45901234, 78),
    ('2026-10-08', '2026-10-14', 45901234, 87),
    ('2026-11-05', NULL,         47012345, 98),
    ('2026-09-30', '2026-10-06', 47012345, 64);