package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Equipo;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.EquipoCollection;

@Path("/clubs/{idClub}")
public class EquipoResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	@Context
	private UriInfo uriInfo;

	@GET
	@Produces(MediaType.FUTBOL_API_EQUIPO_COLLECTION)
	public EquipoCollection getEquipos(@PathParam("idClub") String clubid,
			@QueryParam("pattern") String pattern,
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
						+ "%' and idClub=" + clubid + ")";
			} else {
				sql = "select * from equipo where idClub=" + clubid + " LIMIT "
						+ offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Equipo equipo = new Equipo();
				equipo.setIdClub(clubid);
				equipo.setIdEquipo(rs.getString("idEquipo"));
				equipo.setNombre(rs.getString("nombre"));
				// equipo.addlink
				equipos.addEquipo(equipo);
				icount++;
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		if (ioffset != 0) {
			String prevoffset = "" + (ioffset - ilength);
			// stings.addLink(BeeterAPILinkBuilder.buildURIStings(uriInfo,prevoffset,
			// length, username, "prev"));
		}
		// stings.addLink(BeeterAPILinkBuilder.buildURIStings(uriInfo, offset,
		// length, username, "self"));
		String nextoffset = "" + (ioffset + ilength);
		if (ilength <= icount) {
			// stings.addLink(BeeterAPILinkBuilder.buildURIStings(uriInfo,
			// nextoffset, length, username, "next"));
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
			sql = "select * from equipo where idClub=" + clubid
					+ " and idEquipo='" + idequipo + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				equipo.setIdClub(clubid);
				equipo.setIdEquipo(idequipo);
				equipo.setNombre(rs.getString("nombre"));
				// equipo.addlink
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return equipo;
	}

}
