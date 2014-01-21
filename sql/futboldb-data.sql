
insert into Club (idClub,nombre) values (1,'Barcelona');
insert into Club (idClub,nombre) values (2,'Sant Boi');
insert into Campeonatos (idCampeonatos, nombre) values (1, 'Alevin');
insert into Campeonatos (idCampeonatos, nombre) values (2, 'Femenino B');
insert into Campeonatos (idCampeonatos, nombre) values (3, 'Senior');
insert into Equipo (idequipo,idClub,nombre,idCampeonatos) values (5,'1', 'A',3);
insert into Equipo (idequipo,idClub,nombre,idCampeonatos) values (2,'1', 'Femenino',2);
insert into Equipo (idequipo,idClub,nombre,idCampeonatos) values (3,'2', 'Femenino',2);
insert into Equipo (idequipo,idClub,nombre,idCampeonatos) values (4,'2', 'Infantil',1);
insert into Equipo (idequipo,idClub,nombre,idCampeonatos) values (1,'1', 'Infantil',1);
insert into Jugadores (dni,nombre, apellidos, idequipo) values ('43435323H','Jaime', 'Martinez', '1');
insert into Jugadores (dni,nombre, apellidos, idequipo) values ('111111H','Jose', 'Garcia', '1');
insert into Jugadores (dni,nombre, apellidos, idequipo) values ('123456','Javier', 'Sanchez', '1');
insert into Jugadores (dni,nombre, apellidos, idequipo) values ('2323','Marta', 'Sanchez', '2');
insert into Jugadores (dni,nombre, apellidos, idequipo) values ('23234','Sandra', 'Gall', '2');
insert into Usuarios (username,email,nombre,password,role) values ('marin','marin@marin.com','Marin',MD5('marin'),'registered');
insert into Usuarios (username,email,nombre,password,role) values ('judit','judit@judit.com','Judit',MD5('judit'),'registered');
insert into Usuarios (username,email,nombre,password,role) values ('isaac','isaac@isaac.com','Isaac',MD5('isaac'),'registered');
insert into Usuarios (username,email,nombre,password,role) values ('kilian','kilian@kilian.com','Kilian',MD5('kilian'),'registered');
insert into Usuarios (username,email,nombre,password,role) values ('admin','admin@admin.com','Admin',MD5('admin'),'administrator');
insert into Calendario values (1,2,2,3,7,'28-12-2013','15:00');
insert into Calendario values (2,2,2,4,7,'29-12-2013','19:00');
insert into Calendario values (3,2,2,3,7,'28-12-2013','18:00');
insert into Calendario values (4,2,3,4,7,'28-12-2013','18:00');
insert into Retransmision (idPartido,tiempo,texto,media) values (1,'5','El jugador Fulanito acaba de dar al palo!!!',null);
insert into Retransmision (idPartido,tiempo,texto,media) values (1,'15','GOL GOL GOL GOL',null);
insert into Retransmision (idPartido,tiempo,texto,media) values (2,'45','Partido muy aburrido',null);
insert into Retransmision (idPartido,tiempo,texto,media) values (2,'50','PALO PALO PALO',null);