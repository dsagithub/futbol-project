package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.EquiposLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Equipo;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.EquipoCollection;

@Path("/club/{idClub}/e")
public class EquipoResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext security;

	@GET
	@Produces(MediaType.FUTBOL_API_EQUIPO_COLLECTION)
	public EquipoCollection getEquipos(@PathParam("idClub") String clubid,
			@QueryParam("pattern") String pattern,
			@QueryParam("searchcampe") String seacampe,
			@QueryParam("offset") String offset,
			@QueryParam("length") String length) {

		if ((offset == null) || (length == null))
			throw new BadRequestException(
					"offset and length are mandatory parameters");
		int ioffset, ilength;
		int icount = 0;
		try {
			ioffset = Integer.parseInt(offset);
			if (ioffset < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new BadRequestException(
					"offset must be an integer greater or equal than 0.");
		}
		try {
			ilength = Integer.parseInt(length);
			if (ilength < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			throw new BadRequestException(
					"length must be an integer greater or equal than 1.");
		}
		EquipoCollection equipos = new EquipoCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = null;
			if (pattern != null) {
				sql = "select * from equipo where (nombre like '%" + pattern
						+ "%' and idclub=" + clubid + ")";
			} else {
				if (seacampe != null) {
					sql = "select * from equipo where idcampeonatos=" + seacampe
							+ " LIMIT " + offset + "," + length;
				} else {
					sql = "select * from equipo where idclub=" + clubid
							+ " LIMIT " + offset + "," + length;
					//sql = "select campeonatos.nombre, equipo.* from equipo, campeonatos where equipo.idcampeonatos=campeonatos.idcampeonatos and equipo.idclub ="
						//	+ clubid + " LIMIT " + offset + "," + length;
				}
			}
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				Equipo equipo2 = new Equipo();
				//equipo2.setCampnombre(rs.getString("nombre"));
				equipo2.setIdClub(clubid);
				equipo2.setIdEquipo(rs.getString("idequipo"));
				equipo2.setNombre(rs.getString("nombre"));
				equipo2.setCampeonato(rs.getString("idcampeonatos"));
				equipo2.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo,
						"self", clubid, equipo2.getIdEquipo()));

				equipo2.addLink(EquiposLinkBuilder.buildURICalendarioId(
						uriInfo, "Calendario", equipo2.getCampeonato(), offset,
						length, pattern));
				equipos.addEquipo(equipo2);
				icount++;
				while (rs.next()) {
					Equipo equipo = new Equipo();
					//equipo.setCampnombre(rs.getString("nombre"));
					equipo.setIdClub(clubid);
					equipo.setIdEquipo(rs.getString("idequipo"));
					equipo.setNombre(rs.getString("nombre"));
					equipo.setCampeonato(rs.getString("idcampeonatos"));
					equipo.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo,
							"self", clubid, equipo.getIdEquipo()));
					equipo.addLink(EquiposLinkBuilder.buildURICalendarioId(
							uriInfo, "Calendario", equipo.getCampeonato(),
							offset, length, pattern));
					equipos.addEquipo(equipo);
					icount++;
				}
			} else {
				throw new EquipoNotFoundException();
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		if (ioffset != 0) {
			String prevoffset = "" + (ioffset - ilength);
			equipos.addLink(EquiposLinkBuilder.buildURIEquipos(uriInfo,
					prevoffset, length, pattern, "prev", clubid));
		}
		equipos.addLink(EquiposLinkBuilder.buildURIEquipos(uriInfo, offset,
				length, pattern, "self", clubid));
		String nextoffset = "" + (ioffset + ilength);
		if (ilength <= icount) {
			equipos.addLink(EquiposLinkBuilder.buildURIEquipos(uriInfo,
					nextoffset, length, pattern, "next", clubid));
		}
		return equipos;
	}

	@GET
	@Path("/{idequipo}")
	@Produces(MediaType.FUTBOL_API_EQUIPO)
	public Equipo getEquipo(@PathParam("idClub") String clubid,
			@PathParam("idequipo") String idequipo) {
		Equipo equipo = new Equipo();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = null;
			sql = "select * from equipo where idclub=" + clubid
					+ " and idequipo='" + idequipo + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				equipo.setIdClub(clubid);
				equipo.setIdEquipo(idequipo);
				equipo.setNombre(rs.getString("nombre"));
				equipo.setCampeonato(rs.getString("idcampeonatos"));
				equipo.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo,
						"self", clubid, equipo.getIdEquipo()));
				equipo.addLink(EquiposLinkBuilder.buildURIClubId(uriInfo,
						"Club", clubid));
				equipo.addLink(EquiposLinkBuilder.buildURIJugadores(uriInfo,
						"Jugadores", "0", "15", idequipo, clubid));
				equipo.addLink(EquiposLinkBuilder.buildURICalendarioId(uriInfo,
						"Calendario", equipo.getCampeonato(), "0", "15", null));
			} else {
				throw new EquipoNotFoundException();
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return equipo;
	}

	@POST
	@Produces(MediaType.FUTBOL_API_EQUIPO)
	@Consumes(MediaType.FUTBOL_API_EQUIPO)
	public Equipo crearEquipo(@PathParam("idClub") String idclub, Equipo equipo) {

		if (!security.isUserInRole("administrator")) {
			throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
		}

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		if (equipo == null) {
			throw new BadRequestException("Insert name of team");
		} else {
			if (equipo.getNombre().length() > 50) {
				throw new BadRequestException(
						"Name length must be less or equal than 50 characters");
			}
			if (equipo.getCampeonato().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
			if (equipo.getNombre().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into equipo (idclub,idcampeonatos,nombre) values ('"
					+ idclub
					+ "', '"
					+ equipo.getCampeonato()
					+ "', '"
					+ equipo.getNombre() + "')";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int idEquipo = rs.getInt(1);
				equipo.setIdEquipo(Integer.toString(idEquipo));
				equipo.setIdClub(idclub);
				equipo.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo,
						"self", idclub, equipo.getIdEquipo()));
				equipo.addLink(EquiposLinkBuilder.buildURIClubId(uriInfo,
						"Club", idclub));
				equipo.addLink(EquiposLinkBuilder.buildURIJugadores(uriInfo,
						"Jugadores", "0", "15", equipo.getIdEquipo(), idclub));
				rs.close();
				stmt.close();
				conn.close();
			} else {
				throw new EquipoNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return equipo;
	}

	@PUT
	@Path("/{idequipo}")
	@Produces(MediaType.FUTBOL_API_EQUIPO)
	@Consumes(MediaType.FUTBOL_API_EQUIPO)
	public Equipo actualizarEquipo(@PathParam("idClub") String idclub,
			@PathParam("idequipo") String idequipo, Equipo equipo) {

		if (!security.isUserInRole("administrator")) {
			throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
		}
		if (equipo == null) {
			throw new BadRequestException("Insert name of team");
		} else {
			if (equipo.getNombre().length() > 50) {
				throw new BadRequestException(
						"Name length must be less or equal than 50 characters");
			}
			if (equipo.getCampeonato().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
			if (equipo.getNombre().length() == 0) {
				throw new BadRequestException("faltan parametros");
			}
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "update equipo set equipo.nombre='"
					+ equipo.getNombre() + "', equipo.idcampeonatos="
					+ equipo.getCampeonato() + " where (equipo.idclub="
					+ idclub + " AND equipo.idequipo='" + idequipo + "')";

			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new EquipoNotFoundException();
			equipo.setIdClub(idclub);
			equipo.setIdEquipo(idequipo);
			equipo.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo, "self",
					idclub, equipo.getIdEquipo()));
			equipo.addLink(EquiposLinkBuilder.buildURIClubId(uriInfo, "Club",
					idclub));
			equipo.addLink(EquiposLinkBuilder.buildURIJugadores(uriInfo,
					"Jugadores", "0", "15", equipo.getIdEquipo(), idclub));
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return equipo;
	}

	@DELETE
	@Path("/{idequipo}")
	public void borrarEquipo(@PathParam("idequipo") String idequipo,
			@PathParam("idClub") String idclub) {

		if (!security.isUserInRole("administrator")) {
			throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
		}

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		String sql;
		try {
			stmt = conn.createStatement();
			sql = "delete from equipo where (idequipo=" + idequipo
					+ " AND idclub='" + idclub + "')";

			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new BadRequestException("no permitido");

		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
