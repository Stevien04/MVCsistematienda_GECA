-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.32-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para bdsistienda_geca
CREATE DATABASE IF NOT EXISTS `bdsistienda_geca` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `bdsistienda_geca`;

-- Volcando estructura para tabla bdsistienda_geca.tbcargo
CREATE TABLE IF NOT EXISTS `tbcargo` (
  `idcargo` int(11) NOT NULL AUTO_INCREMENT,
  `nombrecargo` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idcargo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbcargo: ~3 rows (aproximadamente)
INSERT INTO `tbcargo` (`idcargo`, `nombrecargo`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Administrador', 'Encargado de la gestión del sistema', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 'Vendedor', 'Atención al cliente y ventas', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Almacenero', 'Control de inventario y productos', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbcategoria
CREATE TABLE IF NOT EXISTS `tbcategoria` (
  `idcategoria` int(11) NOT NULL AUTO_INCREMENT,
  `nombrecategoria` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idcategoria`),
  UNIQUE KEY `nombrecategoria` (`nombrecategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbcategoria: ~5 rows (aproximadamente)
INSERT INTO `tbcategoria` (`idcategoria`, `nombrecategoria`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Tecnología', 'Productos electrónicos y tecnológicos ejemplo edicion.', 1, '2025-10-04 00:36:48', '2025-10-18 00:25:57'),
	(2, 'Accesorios', 'Accesorios para computadoras y dispositivos moviles y artefactos electronicos.', 1, '2025-10-04 00:36:48', '2025-10-18 00:25:57'),
	(3, 'Oficina', 'Productos de oficina y papelería', 1, '2025-10-04 00:36:48', '2025-10-18 00:25:57'),
	(5, 'Ropa', 'Prendas de vestir', 1, '2025-10-04 00:36:48', '2025-10-18 00:25:57'),
	(6, 'Prueba', 'Prueba de la prueba que se prueba y se aprueba.', 0, '2025-10-11 23:09:03', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbcliente
CREATE TABLE IF NOT EXISTS `tbcliente` (
  `idcliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dni` varchar(8) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_registro` date DEFAULT curdate(),
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idcliente`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbcliente: ~4 rows (aproximadamente)
INSERT INTO `tbcliente` (`idcliente`, `nombre`, `apellido`, `dni`, `telefono`, `email`, `direccion`, `fecha_registro`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Ana', 'Rodríguez', '44332211', '987111222', 'ana@cliente.com', 'Av. Los Olivos 123', '2025-10-01', 1, '2025-10-18 00:25:57', '2025-10-24 15:16:09'),
	(2, 'Luis', 'Martínez', '55667788', '987333444', 'luis@cliente.com', 'Jr. Las Flores 456', '2025-10-01', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Sofía', 'García', '99887766', '987555666', 'sofia@cliente.com', 'Calle Primavera 789', '2025-10-01', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 'Pedro', 'Hernández', '22334455', '987777888', 'pedro@cliente.com', 'Av. Siempre Viva 321', '2025-10-01', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbcolor
CREATE TABLE IF NOT EXISTS `tbcolor` (
  `idcolor` int(11) NOT NULL AUTO_INCREMENT,
  `nombrecolor` varchar(50) NOT NULL,
  `codigo_hex` varchar(7) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idcolor`),
  UNIQUE KEY `nombrecolor` (`nombrecolor`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbcolor: ~10 rows (aproximadamente)
INSERT INTO `tbcolor` (`idcolor`, `nombrecolor`, `codigo_hex`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Negro', '#000000', 'Color negro', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 'Blanco', '#FFFFFF', 'Color blanco', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Rojo', '#FF0000', 'Color rojo', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 'Azul Marino', '#000080', 'Azul marino', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(5, 'Verde Oliva', '#808000', 'Verde oliva', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(6, 'Gris', '#808080', 'Color gris', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(7, 'Azul', '#0000FF', 'Color azul', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(8, 'Verde', '#008000', 'Color verde', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(9, 'Amarillo', '#FFFF00', 'Color amarillo', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(10, 'Rosa', '#FFC0CB', 'Color rosa', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbempleado
CREATE TABLE IF NOT EXISTS `tbempleado` (
  `idempleado` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dni` varchar(8) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_contrato` date DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `idcargo` int(11) DEFAULT NULL,
  `usuario` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idempleado`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `usuario` (`usuario`),
  KEY `idcargo` (`idcargo`),
  CONSTRAINT `tbempleado_ibfk_1` FOREIGN KEY (`idcargo`) REFERENCES `tbcargo` (`idcargo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbempleado: ~5 rows (aproximadamente)
INSERT INTO `tbempleado` (`idempleado`, `nombre`, `apellido`, `dni`, `telefono`, `email`, `direccion`, `fecha_contrato`, `salario`, `estado`, `idcargo`, `usuario`, `password`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Gabriela', 'Cohaila', '12345678', '987654321', 'gabriela@empresa.com', 'Av. Principal 123', '2023-01-15', 2500.00, 1, 1, 'gcohaila', '1234', '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 'María', 'Gómez', '87654321', '987654322', 'maria@empresa.com', 'Jr. Secundaria 456', '2023-02-20', 1800.00, 1, 2, 'mgomez', '1234', '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Carlos', 'López', '11223344', '987654323', 'carlos@empresa.com', 'Calle 789', '2023-03-10', 1600.00, 1, 3, 'clopez', '1234', '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 'Admin', 'Prueba', '99999999', '999999999', 'admin@empresa.com', 'Av Test 1', '2025-01-01', 3000.00, 1, 1, 'admin', 'admin123', '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(5, 'Vendedor', 'Prueba', '88888888', '988888888', 'vendedor@empresa.com', 'Av Test 2', '2025-01-01', 1500.00, 1, 2, 'vendedor', 'vendedor123', '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbmarca
CREATE TABLE IF NOT EXISTS `tbmarca` (
  `idmarca` int(11) NOT NULL AUTO_INCREMENT,
  `nombremarca` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idmarca`),
  UNIQUE KEY `nombremarca` (`nombremarca`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbmarca: ~6 rows (aproximadamente)
INSERT INTO `tbmarca` (`idmarca`, `nombremarca`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Nike', 'Marca deportiva y de ropa casual', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 'Adidas', 'Marca deportiva y de ropa casual', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Zara', 'Marca de moda y ropa casual', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 'HP', 'Tecnología y computadoras', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(5, 'Samsung', 'Electrónicos y tecnología', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(6, 'Logitech', 'Accesorios para computadoras', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbmodelo
CREATE TABLE IF NOT EXISTS `tbmodelo` (
  `idmodelo` int(11) NOT NULL AUTO_INCREMENT,
  `idmarca` int(11) NOT NULL,
  `nombremodelo` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idmodelo`),
  UNIQUE KEY `unique_modelo_marca` (`idmarca`,`nombremodelo`),
  CONSTRAINT `fk_modelo_marca` FOREIGN KEY (`idmarca`) REFERENCES `tbmarca` (`idmarca`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbmodelo: ~15 rows (aproximadamente)
INSERT INTO `tbmodelo` (`idmodelo`, `idmarca`, `nombremodelo`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 1, 'Air Force 1', 'Zapatillas deportivas clásicas', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 1, 'Dri-FIT Legend', 'Camisetas deportivas de tecnología Dri-FIT', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 1, 'Air Max', 'Zapatillas con tecnología Air Max', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 2, 'Ultraboost', 'Zapatillas running con tecnología Boost', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(5, 2, 'Superstar', 'Zapatillas clásicas', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(6, 2, 'Tiro', 'Pantalones deportivos', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(7, 3, 'Pantalón Tapered', 'Pantalón de corte tapered', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(8, 3, 'Blusa Basic', 'Blusa básica de vestir', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(9, 3, 'Chaleco Puffer', 'Chaleco acolchado', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(10, 4, 'Pavilion', 'Línea de laptops Pavilion', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(11, 4, 'Omen', 'Línea gaming Omen', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(12, 5, 'Galaxy S', 'Serie de smartphones Galaxy S', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(13, 5, 'QLED', 'Televisores QLED', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(14, 6, 'MX Master', 'Mouse ergonómico para productividad', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(15, 6, 'K380', 'Teclado inalámbrico compacto', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbproducto
CREATE TABLE IF NOT EXISTS `tbproducto` (
  `idproducto` int(11) NOT NULL AUTO_INCREMENT,
  `idcategoria` int(11) DEFAULT NULL,
  `idmodelo` int(11) DEFAULT NULL,
  `idcolor` int(11) DEFAULT NULL,
  `nombreproducto` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `fecha_creacion` date DEFAULT curdate(),
  `estado` int(11) DEFAULT 1,
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idproducto`),
  KEY `idcategoria` (`idcategoria`),
  KEY `idmodelo` (`idmodelo`),
  KEY `idcolor` (`idcolor`),
  CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`idcategoria`) REFERENCES `tbcategoria` (`idcategoria`),
  CONSTRAINT `fk_producto_color` FOREIGN KEY (`idcolor`) REFERENCES `tbcolor` (`idcolor`),
  CONSTRAINT `fk_producto_modelo` FOREIGN KEY (`idmodelo`) REFERENCES `tbmodelo` (`idmodelo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbproducto: ~2 rows (aproximadamente)
INSERT INTO `tbproducto` (`idproducto`, `idcategoria`, `idmodelo`, `idcolor`, `nombreproducto`, `descripcion`, `precio`, `stock`, `fecha_creacion`, `estado`, `fecha_actualizacion`) VALUES
	(7, 2, 13, 7, 'Stevie', '123', 50.00, 3, '2025-10-24', 1, '2025-10-24 14:45:46'),
	(8, 5, 4, 9, 'POLO AZURE', 'BUEN NEGOCIO', 30.00, 20, '2025-10-24', 1, '2025-10-24 15:18:42');

-- Volcando estructura para tabla bdsistienda_geca.tbproducto_talla
CREATE TABLE IF NOT EXISTS `tbproducto_talla` (
  `idproducto_talla` int(11) NOT NULL AUTO_INCREMENT,
  `idproducto` int(11) NOT NULL,
  `idtalla` int(11) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idproducto_talla`),
  UNIQUE KEY `unique_producto_talla` (`idproducto`,`idtalla`),
  KEY `fk_productotalla_talla` (`idtalla`),
  CONSTRAINT `fk_productotalla_producto` FOREIGN KEY (`idproducto`) REFERENCES `tbproducto` (`idproducto`),
  CONSTRAINT `fk_productotalla_talla` FOREIGN KEY (`idtalla`) REFERENCES `tbtalla` (`idtalla`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbproducto_talla: ~0 rows (aproximadamente)

-- Volcando estructura para tabla bdsistienda_geca.tbtalla
CREATE TABLE IF NOT EXISTS `tbtalla` (
  `idtalla` int(11) NOT NULL AUTO_INCREMENT,
  `idtipotalla` int(11) NOT NULL,
  `nombretalla` varchar(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idtalla`),
  UNIQUE KEY `unique_talla_tipo` (`idtipotalla`,`nombretalla`),
  CONSTRAINT `fk_talla_tipotalla` FOREIGN KEY (`idtipotalla`) REFERENCES `tbtipotalla` (`idtipotalla`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbtalla: ~19 rows (aproximadamente)
INSERT INTO `tbtalla` (`idtalla`, `idtipotalla`, `nombretalla`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 1, 'XS', 'Extra Small', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 1, 'S', 'Small', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 1, 'M', 'Medium', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 1, 'L', 'Large', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(5, 1, 'XL', 'Extra Large', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(6, 1, 'XXL', 'Double Extra Large', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(7, 2, '28', 'Talla 28 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(8, 2, '30', 'Talla 30 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(9, 2, '32', 'Talla 32 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(10, 2, '34', 'Talla 34 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(11, 2, '36', 'Talla 36 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(12, 2, '38', 'Talla 38 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(13, 3, '6', 'Talla 6 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(14, 3, '7', 'Talla 7 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(15, 3, '8', 'Talla 8 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(16, 3, '9', 'Talla 9 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(17, 3, '10', 'Talla 10 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(18, 3, '11', 'Talla 11 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(19, 3, '12', 'Talla 12 USA', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbtipodocumento
CREATE TABLE IF NOT EXISTS `tbtipodocumento` (
  `idtipodocumento` int(11) NOT NULL AUTO_INCREMENT,
  `nombretipodocumento` varchar(100) NOT NULL,
  `abreviatura` varchar(10) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idtipodocumento`),
  UNIQUE KEY `nombretipodocumento` (`nombretipodocumento`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbtipodocumento: ~4 rows (aproximadamente)
INSERT INTO `tbtipodocumento` (`idtipodocumento`, `nombretipodocumento`, `abreviatura`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Documento Nacional de Identidad', 'DNI', 'Documento de identidad personal', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 'Carnet de Extranjería', 'CE', 'Documento para extranjeros', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Pasaporte', 'PAS', 'Documento para viajes internacionales', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(4, 'Registro Único de Contribuyentes', 'RUC', 'Documento tributario', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para tabla bdsistienda_geca.tbtipotalla
CREATE TABLE IF NOT EXISTS `tbtipotalla` (
  `idtipotalla` int(11) NOT NULL AUTO_INCREMENT,
  `nombretipotalla` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` int(11) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_actualizacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`idtipotalla`),
  UNIQUE KEY `nombretipotalla` (`nombretipotalla`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bdsistienda_geca.tbtipotalla: ~3 rows (aproximadamente)
INSERT INTO `tbtipotalla` (`idtipotalla`, `nombretipotalla`, `descripcion`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
	(1, 'Internacional', 'Tallas internacionales S, M, L, XL, etc.', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(2, 'Numérica USA', 'Tallas numéricas para ropa (28, 30, 32, etc.)', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57'),
	(3, 'Calzado USA', 'Tallas de calzado estadounidense', 1, '2025-10-18 00:25:57', '2025-10-18 00:25:57');

-- Volcando estructura para procedimiento bdsistienda_geca.usp_categoria_create
DELIMITER //
CREATE PROCEDURE `usp_categoria_create`(
    IN p_nombrecategoria VARCHAR(100),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO tbcategoria (nombrecategoria, descripcion)
    VALUES (p_nombrecategoria, p_descripcion);
    
    SELECT LAST_INSERT_ID() as idcategoria;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_categoria_delete
DELIMITER //
CREATE PROCEDURE `usp_categoria_delete`(
    IN p_idcategoria INT
)
BEGIN
    UPDATE tbcategoria
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idcategoria = p_idcategoria;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_categoria_read
DELIMITER //
CREATE PROCEDURE `usp_categoria_read`()
BEGIN
    SELECT 
        idcategoria,
        nombrecategoria,
        descripcion,
        estado,
        fecha_creacion,
        fecha_actualizacion
    FROM tbcategoria
    WHERE estado = 1
    ORDER BY nombrecategoria;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_categoria_update
DELIMITER //
CREATE PROCEDURE `usp_categoria_update`(
    IN p_idcategoria INT,
    IN p_nombrecategoria VARCHAR(100),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    UPDATE tbcategoria
    SET nombrecategoria = p_nombrecategoria,
        descripcion = p_descripcion,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idcategoria = p_idcategoria;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_color_create
DELIMITER //
CREATE PROCEDURE `usp_color_create`(
    IN p_nombrecolor VARCHAR(50),
    IN p_codigo_hex VARCHAR(7),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO tbcolor (nombrecolor, codigo_hex, descripcion)
    VALUES (p_nombrecolor, p_codigo_hex, p_descripcion);
    
    SELECT LAST_INSERT_ID() as idcolor;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_color_delete
DELIMITER //
CREATE PROCEDURE `usp_color_delete`(
    IN p_idcolor INT
)
BEGIN
    UPDATE tbcolor
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idcolor = p_idcolor;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_color_read
DELIMITER //
CREATE PROCEDURE `usp_color_read`()
BEGIN
    SELECT 
        idcolor,
        nombrecolor,
        codigo_hex,
        descripcion,
        estado,
        fecha_creacion,
        fecha_actualizacion
    FROM tbcolor
    WHERE estado = 1
    ORDER BY nombrecolor;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_color_update
DELIMITER //
CREATE PROCEDURE `usp_color_update`(
    IN p_idcolor INT,
    IN p_nombrecolor VARCHAR(50),
    IN p_codigo_hex VARCHAR(7),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    UPDATE tbcolor
    SET nombrecolor = p_nombrecolor,
        codigo_hex = p_codigo_hex,
        descripcion = p_descripcion,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idcolor = p_idcolor;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_marca_activate
DELIMITER //
CREATE PROCEDURE `usp_marca_activate`(
    IN p_idmarca INT
)
BEGIN
    UPDATE tbmarca
    SET estado = 1,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idmarca = p_idmarca;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_marca_create
DELIMITER //
CREATE PROCEDURE `usp_marca_create`(
    IN p_nombremarca VARCHAR(100),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO tbmarca (nombremarca, descripcion)
    VALUES (p_nombremarca, p_descripcion);
    
    SELECT LAST_INSERT_ID() as idmarca;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_marca_delete
DELIMITER //
CREATE PROCEDURE `usp_marca_delete`(
    IN p_idmarca INT
)
BEGIN
    UPDATE tbmarca
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idmarca = p_idmarca;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_marca_read
DELIMITER //
CREATE PROCEDURE `usp_marca_read`()
BEGIN
    SELECT 
        idmarca,
        nombremarca,
        descripcion,
        estado,
        fecha_creacion,
        fecha_actualizacion
    FROM tbmarca
    WHERE estado = 1
    ORDER BY nombremarca;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_marca_read_by_id
DELIMITER //
CREATE PROCEDURE `usp_marca_read_by_id`(
    IN p_idmarca INT
)
BEGIN
    SELECT 
        idmarca,
        nombremarca,
        descripcion,
        estado,
        fecha_creacion,
        fecha_actualizacion
    FROM tbmarca
    WHERE idmarca = p_idmarca;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_marca_update
DELIMITER //
CREATE PROCEDURE `usp_marca_update`(
    IN p_idmarca INT,
    IN p_nombremarca VARCHAR(100),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    UPDATE tbmarca
    SET nombremarca = p_nombremarca,
        descripcion = p_descripcion,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idmarca = p_idmarca;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_activate
DELIMITER //
CREATE PROCEDURE `usp_modelo_activate`(
    IN p_idmodelo INT
)
BEGIN
    UPDATE tbmodelo
    SET estado = 1,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idmodelo = p_idmodelo;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_create
DELIMITER //
CREATE PROCEDURE `usp_modelo_create`(
    IN p_idmarca INT,
    IN p_nombremodelo VARCHAR(100),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO tbmodelo (idmarca, nombremodelo, descripcion)
    VALUES (p_idmarca, p_nombremodelo, p_descripcion);
    
    SELECT LAST_INSERT_ID() as idmodelo;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_delete
DELIMITER //
CREATE PROCEDURE `usp_modelo_delete`(
    IN p_idmodelo INT
)
BEGIN
    UPDATE tbmodelo
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idmodelo = p_idmodelo;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_read
DELIMITER //
CREATE PROCEDURE `usp_modelo_read`()
BEGIN
    SELECT 
        m.idmodelo,
        m.idmarca,
        mar.nombremarca,
        m.nombremodelo,
        m.descripcion,
        m.estado,
        m.fecha_creacion,
        m.fecha_actualizacion
    FROM tbmodelo m
    INNER JOIN tbmarca mar ON m.idmarca = mar.idmarca
    WHERE m.estado = 1
    ORDER BY mar.nombremarca, m.nombremodelo;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_read_by_id
DELIMITER //
CREATE PROCEDURE `usp_modelo_read_by_id`(
    IN p_idmodelo INT
)
BEGIN
    SELECT 
        m.idmodelo,
        m.idmarca,
        mar.nombremarca,
        m.nombremodelo,
        m.descripcion,
        m.estado,
        m.fecha_creacion,
        m.fecha_actualizacion
    FROM tbmodelo m
    INNER JOIN tbmarca mar ON m.idmarca = mar.idmarca
    WHERE m.idmodelo = p_idmodelo;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_read_by_marca
DELIMITER //
CREATE PROCEDURE `usp_modelo_read_by_marca`(
    IN p_idmarca INT
)
BEGIN
    SELECT 
        m.idmodelo,
        m.idmarca,
        mar.nombremarca,
        m.nombremodelo,
        m.descripcion,
        m.estado,
        m.fecha_creacion,
        m.fecha_actualizacion
    FROM tbmodelo m
    INNER JOIN tbmarca mar ON m.idmarca = mar.idmarca
    WHERE m.idmarca = p_idmarca AND m.estado = 1
    ORDER BY m.nombremodelo;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_modelo_update
DELIMITER //
CREATE PROCEDURE `usp_modelo_update`(
    IN p_idmodelo INT,
    IN p_idmarca INT,
    IN p_nombremodelo VARCHAR(100),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    UPDATE tbmodelo
    SET idmarca = p_idmarca,
        nombremodelo = p_nombremodelo,
        descripcion = p_descripcion,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idmodelo = p_idmodelo;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_activate
DELIMITER //
CREATE PROCEDURE `usp_producto_activate`(
    IN p_idproducto INT
)
BEGIN
    UPDATE tbproducto
    SET estado = 1,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idproducto = p_idproducto;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_create
DELIMITER //
CREATE PROCEDURE `usp_producto_create`(
    IN p_idcategoria INT,
    IN p_idmodelo INT,
    IN p_idcolor INT,
    IN p_nombreproducto VARCHAR(150),
    IN p_descripcion TEXT,
    IN p_precio DECIMAL(10,2),
    IN p_stock INT
)
BEGIN
    INSERT INTO tbproducto (idcategoria, idmodelo, idcolor, nombreproducto, descripcion, precio, stock)
    VALUES (p_idcategoria, p_idmodelo, p_idcolor, p_nombreproducto, p_descripcion, p_precio, p_stock);
    
    SELECT LAST_INSERT_ID() as idproducto;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_delete
DELIMITER //
CREATE PROCEDURE `usp_producto_delete`(
    IN p_idproducto INT
)
BEGIN
    UPDATE tbproducto
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idproducto = p_idproducto;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_read
DELIMITER //
CREATE PROCEDURE `usp_producto_read`()
BEGIN
    SELECT 
        p.idproducto,
        p.idcategoria,
        c.nombrecategoria,
        p.idmodelo,
        m.nombremodelo,
        mar.nombremarca,
        p.idcolor,
        col.nombrecolor,
        col.codigo_hex,
        p.nombreproducto,
        p.descripcion,
        p.precio,
        p.stock,
        p.estado,
        p.fecha_creacion,
        p.fecha_actualizacion
    FROM tbproducto p
    LEFT JOIN tbcategoria c ON p.idcategoria = c.idcategoria
    LEFT JOIN tbmodelo m ON p.idmodelo = m.idmodelo
    LEFT JOIN tbmarca mar ON m.idmarca = mar.idmarca
    LEFT JOIN tbcolor col ON p.idcolor = col.idcolor
    WHERE p.estado = 1
    ORDER BY p.nombreproducto;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_read_by_id
DELIMITER //
CREATE PROCEDURE `usp_producto_read_by_id`(
    IN p_idproducto INT
)
BEGIN
    SELECT 
        p.idproducto,
        p.idcategoria,
        c.nombrecategoria,
        p.idmodelo,
        m.nombremodelo,
        mar.idmarca,
        mar.nombremarca,
        p.idcolor,
        col.nombrecolor,
        col.codigo_hex,
        p.nombreproducto,
        p.descripcion,
        p.precio,
        p.stock,
        p.estado,
        p.fecha_creacion,
        p.fecha_actualizacion
    FROM tbproducto p
    LEFT JOIN tbcategoria c ON p.idcategoria = c.idcategoria
    LEFT JOIN tbmodelo m ON p.idmodelo = m.idmodelo
    LEFT JOIN tbmarca mar ON m.idmarca = mar.idmarca
    LEFT JOIN tbcolor col ON p.idcolor = col.idcolor
    WHERE p.idproducto = p_idproducto;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_talla_create
DELIMITER //
CREATE PROCEDURE `usp_producto_talla_create`(
    IN p_idproducto INT,
    IN p_idtalla INT,
    IN p_stock INT
)
BEGIN
    INSERT INTO tbproducto_talla (idproducto, idtalla, stock)
    VALUES (p_idproducto, p_idtalla, p_stock)
    ON DUPLICATE KEY UPDATE
        stock = p_stock,
        fecha_actualizacion = CURRENT_TIMESTAMP;
    
    SELECT LAST_INSERT_ID() as idproducto_talla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_talla_delete
DELIMITER //
CREATE PROCEDURE `usp_producto_talla_delete`(
    IN p_idproducto_talla INT
)
BEGIN
    UPDATE tbproducto_talla
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idproducto_talla = p_idproducto_talla;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_talla_read
DELIMITER //
CREATE PROCEDURE `usp_producto_talla_read`(
    IN p_idproducto INT
)
BEGIN
    SELECT 
        pt.idproducto_talla,
        pt.idproducto,
        pt.idtalla,
        t.nombretalla,
        tt.nombretipotalla,
        pt.stock,
        pt.estado,
        pt.fecha_creacion,
        pt.fecha_actualizacion
    FROM tbproducto_talla pt
    INNER JOIN tbtalla t ON pt.idtalla = t.idtalla
    INNER JOIN tbtipotalla tt ON t.idtipotalla = tt.idtipotalla
    WHERE pt.idproducto = p_idproducto AND pt.estado = 1
    ORDER BY tt.nombretipotalla, t.nombretalla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_talla_update_stock
DELIMITER //
CREATE PROCEDURE `usp_producto_talla_update_stock`(
    IN p_idproducto_talla INT,
    IN p_stock INT
)
BEGIN
    UPDATE tbproducto_talla
    SET stock = p_stock,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idproducto_talla = p_idproducto_talla;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_producto_update
DELIMITER //
CREATE PROCEDURE `usp_producto_update`(
    IN p_idproducto INT,
    IN p_idcategoria INT,
    IN p_idmodelo INT,
    IN p_idcolor INT,
    IN p_nombreproducto VARCHAR(150),
    IN p_descripcion TEXT,
    IN p_precio DECIMAL(10,2),
    IN p_stock INT
)
BEGIN
    UPDATE tbproducto
    SET idcategoria = p_idcategoria,
        idmodelo = p_idmodelo,
        idcolor = p_idcolor,
        nombreproducto = p_nombreproducto,
        descripcion = p_descripcion,
        precio = p_precio,
        stock = p_stock,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idproducto = p_idproducto;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_talla_create
DELIMITER //
CREATE PROCEDURE `usp_talla_create`(
    IN p_idtipotalla INT,
    IN p_nombretalla VARCHAR(20),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO tbtalla (idtipotalla, nombretalla, descripcion)
    VALUES (p_idtipotalla, p_nombretalla, p_descripcion);
    
    SELECT LAST_INSERT_ID() as idtalla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_talla_delete
DELIMITER //
CREATE PROCEDURE `usp_talla_delete`(
    IN p_idtalla INT
)
BEGIN
    UPDATE tbtalla
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idtalla = p_idtalla;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_talla_read
DELIMITER //
CREATE PROCEDURE `usp_talla_read`()
BEGIN
    SELECT 
        t.idtalla,
        t.idtipotalla,
        tt.nombretipotalla,
        t.nombretalla,
        t.descripcion,
        t.estado,
        t.fecha_creacion,
        t.fecha_actualizacion
    FROM tbtalla t
    INNER JOIN tbtipotalla tt ON t.idtipotalla = tt.idtipotalla
    WHERE t.estado = 1
    ORDER BY tt.nombretipotalla, t.nombretalla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_talla_read_by_tipo
DELIMITER //
CREATE PROCEDURE `usp_talla_read_by_tipo`(
    IN p_idtipotalla INT
)
BEGIN
    SELECT 
        t.idtalla,
        t.idtipotalla,
        tt.nombretipotalla,
        t.nombretalla,
        t.descripcion,
        t.estado,
        t.fecha_creacion,
        t.fecha_actualizacion
    FROM tbtalla t
    INNER JOIN tbtipotalla tt ON t.idtipotalla = tt.idtipotalla
    WHERE t.idtipotalla = p_idtipotalla AND t.estado = 1
    ORDER BY t.nombretalla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_talla_update
DELIMITER //
CREATE PROCEDURE `usp_talla_update`(
    IN p_idtalla INT,
    IN p_idtipotalla INT,
    IN p_nombretalla VARCHAR(20),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    UPDATE tbtalla
    SET idtipotalla = p_idtipotalla,
        nombretalla = p_nombretalla,
        descripcion = p_descripcion,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idtalla = p_idtalla;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_tipotalla_create
DELIMITER //
CREATE PROCEDURE `usp_tipotalla_create`(
    IN p_nombretipotalla VARCHAR(50),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO tbtipotalla (nombretipotalla, descripcion)
    VALUES (p_nombretipotalla, p_descripcion);
    
    SELECT LAST_INSERT_ID() as idtipotalla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_tipotalla_delete
DELIMITER //
CREATE PROCEDURE `usp_tipotalla_delete`(
    IN p_idtipotalla INT
)
BEGIN
    UPDATE tbtipotalla
    SET estado = 0,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idtipotalla = p_idtipotalla;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_tipotalla_read
DELIMITER //
CREATE PROCEDURE `usp_tipotalla_read`()
BEGIN
    SELECT 
        idtipotalla,
        nombretipotalla,
        descripcion,
        estado,
        fecha_creacion,
        fecha_actualizacion
    FROM tbtipotalla
    WHERE estado = 1
    ORDER BY nombretipotalla;
END//
DELIMITER ;

-- Volcando estructura para procedimiento bdsistienda_geca.usp_tipotalla_update
DELIMITER //
CREATE PROCEDURE `usp_tipotalla_update`(
    IN p_idtipotalla INT,
    IN p_nombretipotalla VARCHAR(50),
    IN p_descripcion VARCHAR(255)
)
BEGIN
    UPDATE tbtipotalla
    SET nombretipotalla = p_nombretipotalla,
        descripcion = p_descripcion,
        fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE idtipotalla = p_idtipotalla;
    
    SELECT ROW_COUNT() as filas_afectadas;
END//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
