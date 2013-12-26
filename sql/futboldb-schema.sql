DROP SCHEMA IF EXISTS `futboldb` ;
CREATE SCHEMA IF NOT EXISTS `futboldb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `futboldb` ;

-- -----------------------------------------------------
-- Table `futboldb`.`Club`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Club` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Club` (
  `idClub` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idClub`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `futboldb`.`Equipo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Equipo` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Equipo` (
  `idEquipo` INT NOT NULL AUTO_INCREMENT,
  `idClub` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idEquipo`),
  CONSTRAINT `fk_Equipos_Clubs1`
    FOREIGN KEY (`idClub`)
    REFERENCES `futboldb`.`Club` (`idClub`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Equipos_Clubs1_idx` ON `futboldb`.`Equipo` (`idClub` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`Campeonatos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Campeonatos` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Campeonatos` (
  `idCampeonatos` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idCampeonatos`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `futboldb`.`Calendario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Calendario` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Calendario` (
  `idCampeonato` INT NOT NULL,
  `idPartido` VARCHAR(45) NOT NULL,
  `idEquipoA` INT NOT NULL,
  `idEquipoB` INT NOT NULL,
  `jornada` INT NULL,
  `fecha` VARCHAR(45) NULL,
  `hora` VARCHAR(45) NULL,
  PRIMARY KEY (`idPartido`),
  CONSTRAINT `fk_Calendario_Equipo1`
    FOREIGN KEY (`idEquipoA`)
    REFERENCES `futboldb`.`Equipo` (`idEquipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Calendario_Equipo2`
    FOREIGN KEY (`idEquipoB`)
    REFERENCES `futboldb`.`Equipo` (`idEquipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Calendario_Campeonatos1`
    FOREIGN KEY (`idCampeonato`)
    REFERENCES `futboldb`.`Campeonatos` (`idCampeonatos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Calendario_Equipo1_idx` ON `futboldb`.`Calendario` (`idEquipoA` ASC);

CREATE INDEX `fk_Calendario_Equipo2_idx` ON `futboldb`.`Calendario` (`idEquipoB` ASC);

CREATE INDEX `fk_Calendario_Campeonatos1_idx` ON `futboldb`.`Calendario` (`idCampeonato` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`Retransmision`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Retransmision` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Retransmision` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idPartido` VARCHAR(45) NOT NULL,
  `tiempo` VARCHAR(45) NULL,
  `texto` VARCHAR(500) NULL,
  `media` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Partidos_Calendario1`
    FOREIGN KEY (`idPartido`)
    REFERENCES `futboldb`.`Calendario` (`idPartido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Partidos_Calendario1_idx` ON `futboldb`.`Retransmision` (`idPartido` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`Usuarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Usuarios` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `nombre` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `role` VARCHAR(45) NULL,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `futboldb`.`Jugadores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Jugadores` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Jugadores` (
  `dni` VARCHAR(50) NOT NULL,
  `nombre` VARCHAR(50) NULL,
  `apellidos` VARCHAR(45) NULL,
  `idequipo` INT NOT NULL,
  PRIMARY KEY (`dni`),
  CONSTRAINT `fk_Jugadores_Equipos`
    FOREIGN KEY (`idequipo`)
    REFERENCES `futboldb`.`Equipo` (`idEquipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Jugadores_Equipos_idx` ON `futboldb`.`Jugadores` (`idequipo` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`Comentarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Comentarios` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Comentarios` (
  `idComentarios` INT NOT NULL AUTO_INCREMENT,
  `tiempo` VARCHAR(45) NULL,
  `texto` VARCHAR(45) NULL,
  `media` VARCHAR(45) NULL,
  `idPartido` VARCHAR(45) NOT NULL,
  `idUsuario` INT NOT NULL,
  PRIMARY KEY (`idComentarios`),
  CONSTRAINT `fk_Comentarios_Calendario1`
    FOREIGN KEY (`idPartido`)
    REFERENCES `futboldb`.`Calendario` (`idPartido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comentarios_Usuarios1`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `futboldb`.`Usuarios` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Comentarios_Calendario1_idx` ON `futboldb`.`Comentarios` (`idPartido` ASC);

CREATE INDEX `fk_Comentarios_Usuarios1_idx` ON `futboldb`.`Comentarios` (`idUsuario` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`Noticias`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`Noticias` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`Noticias` (
  `idNoticias` INT NOT NULL AUTO_INCREMENT,
  `idClub` INT NOT NULL,
  `titulo` VARCHAR(100) NULL,
  `content` VARCHAR(500) NULL,
  `media` VARCHAR(45) NULL,
  `lastModified` TIMESTAMP NULL,
  PRIMARY KEY (`idNoticias`),
  CONSTRAINT `fk_Noticias_Club1`
    FOREIGN KEY (`idClub`)
    REFERENCES `futboldb`.`Club` (`idClub`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Noticias_Club1_idx` ON `futboldb`.`Noticias` (`idClub` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
