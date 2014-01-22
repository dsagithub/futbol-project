SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `futboldb` ;
CREATE SCHEMA IF NOT EXISTS `futboldb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `futboldb` ;

-- -----------------------------------------------------
-- Table `futboldb`.`club`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`club` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`club` (
  `idclub` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idclub`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `futboldb`.`campeonatos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`campeonatos` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`campeonatos` (
  `idcampeonatos` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idcampeonatos`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `futboldb`.`equipo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`equipo` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`equipo` (
  `idequipo` INT NOT NULL AUTO_INCREMENT,
  `idclub` INT NOT NULL,
  `idcampeonatos` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idequipo`),
  CONSTRAINT `fk_Equipos_Clubs1`
    FOREIGN KEY (`idclub`)
    REFERENCES `futboldb`.`club` (`idclub`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Equipo_Campeonatos1`
    FOREIGN KEY (`idcampeonatos`)
    REFERENCES `futboldb`.`campeonatos` (`idcampeonatos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Equipos_Clubs1_idx` ON `futboldb`.`equipo` (`idclub` ASC);

CREATE INDEX `fk_Equipo_Campeonatos1_idx` ON `futboldb`.`equipo` (`idcampeonatos` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`calendario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`calendario` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`calendario` (
  `idpartido` INT NOT NULL AUTO_INCREMENT,
  `idcampeonato` INT NOT NULL,
  `idequipoa` INT NOT NULL,
  `idequipob` INT NOT NULL,
  `jornada` INT NULL,
  `fecha` VARCHAR(45) NULL,
  `hora` VARCHAR(45) NULL,
  PRIMARY KEY (`idpartido`),
  CONSTRAINT `fk_Calendario_Equipo1`
    FOREIGN KEY (`idequipoa`)
    REFERENCES `futboldb`.`equipo` (`idequipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Calendario_Equipo2`
    FOREIGN KEY (`idequipob`)
    REFERENCES `futboldb`.`equipo` (`idequipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Calendario_Campeonatos1`
    FOREIGN KEY (`idcampeonato`)
    REFERENCES `futboldb`.`campeonatos` (`idcampeonatos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Calendario_Equipo1_idx` ON `futboldb`.`calendario` (`idequipoa` ASC);

CREATE INDEX `fk_Calendario_Equipo2_idx` ON `futboldb`.`calendario` (`idequipob` ASC);

CREATE INDEX `fk_Calendario_Campeonatos1_idx` ON `futboldb`.`calendario` (`idcampeonato` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`retransmision`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`retransmision` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`retransmision` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idpartido` INT NOT NULL,
  `tiempo` VARCHAR(45) NULL,
  `texto` VARCHAR(500) NULL,
  `media` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Partidos_Calendario1`
    FOREIGN KEY (`idpartido`)
    REFERENCES `futboldb`.`calendario` (`idpartido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Partidos_Calendario1_idx` ON `futboldb`.`retransmision` (`idpartido` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`usuarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`usuarios` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`usuarios` (
  `idusuario` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL,
  `nombre` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `role` VARCHAR(45) NULL,
  PRIMARY KEY (`idusuario`, `username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `futboldb`.`jugadores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`jugadores` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`jugadores` (
  `dni` VARCHAR(50) NOT NULL,
  `nombre` VARCHAR(50) NULL,
  `apellidos` VARCHAR(45) NULL,
  `idequipo` INT NOT NULL,
  PRIMARY KEY (`dni`),
  CONSTRAINT `fk_Jugadores_Equipos`
    FOREIGN KEY (`idequipo`)
    REFERENCES `futboldb`.`equipo` (`idequipo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Jugadores_Equipos_idx` ON `futboldb`.`jugadores` (`idequipo` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`comentarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`comentarios` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`comentarios` (
  `idcomentarios` INT NOT NULL AUTO_INCREMENT,
  `tiempo` VARCHAR(45) NULL,
  `texto` VARCHAR(45) NULL,
  `media` VARCHAR(45) NULL,
  `idpartido` INT NOT NULL,
  `idusuario` INT NOT NULL,
  PRIMARY KEY (`idcomentarios`),
  CONSTRAINT `fk_Comentarios_Calendario1`
    FOREIGN KEY (`idpartido`)
    REFERENCES `futboldb`.`calendario` (`idpartido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comentarios_Usuarios1`
    FOREIGN KEY (`idusuario`)
    REFERENCES `futboldb`.`usuarios` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Comentarios_Calendario1_idx` ON `futboldb`.`comentarios` (`idpartido` ASC);

CREATE INDEX `fk_Comentarios_Usuarios1_idx` ON `futboldb`.`comentarios` (`idusuario` ASC);


-- -----------------------------------------------------
-- Table `futboldb`.`noticias`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `futboldb`.`noticias` ;

CREATE TABLE IF NOT EXISTS `futboldb`.`noticias` (
  `idnoticias` INT NOT NULL AUTO_INCREMENT,
  `idclub` INT NOT NULL,
  `titulo` VARCHAR(100) NULL,
  `content` VARCHAR(500) NULL,
  `media` VARCHAR(45) NULL,
  `lastmodified` TIMESTAMP NULL,
  PRIMARY KEY (`idnoticias`),
  CONSTRAINT `fk_Noticias_Club1`
    FOREIGN KEY (`idclub`)
    REFERENCES `futboldb`.`club` (`idclub`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Noticias_Club1_idx` ON `futboldb`.`noticias` (`idclub` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
