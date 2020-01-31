-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.1.72-community - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para uf2215_endika
CREATE DATABASE IF NOT EXISTS `uf2215_endika` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `uf2215_endika`;

-- Volcando estructura para tabla uf2215_endika.autores
CREATE TABLE IF NOT EXISTS `autores` (
  `id_autor` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_autor`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla uf2215_endika.autores: ~0 rows (aproximadamente)
DELETE FROM `autores`;
/*!40000 ALTER TABLE `autores` DISABLE KEYS */;
INSERT INTO `autores` (`id_autor`, `nombre`) VALUES
	(2, 'Juan Bas'),
	(1, 'Patrick'),
	(3, 'Rubius'),
	(4, 'York');
/*!40000 ALTER TABLE `autores` ENABLE KEYS */;

-- Volcando estructura para tabla uf2215_endika.libros
CREATE TABLE IF NOT EXISTS `libros` (
  `id_libro` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) DEFAULT NULL,
  `id_autor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_libro`),
  UNIQUE KEY `titulo` (`titulo`),
  KEY `FK_libros_autores` (`id_autor`),
  CONSTRAINT `FK_libros_autores` FOREIGN KEY (`id_autor`) REFERENCES `autores` (`id_autor`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla uf2215_endika.libros: ~0 rows (aproximadamente)
DELETE FROM `libros`;
/*!40000 ALTER TABLE `libros` DISABLE KEYS */;
INSERT INTO `libros` (`id_libro`, `titulo`, `id_autor`) VALUES
	(1, 'El nombre del viento', 1),
	(2, 'Veracidad', 2),
	(3, 'Escuela de gamers', 3),
	(4, 'Fuego y sangre', 4),
	(5, 'Juego de Tronos', 4);
/*!40000 ALTER TABLE `libros` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
