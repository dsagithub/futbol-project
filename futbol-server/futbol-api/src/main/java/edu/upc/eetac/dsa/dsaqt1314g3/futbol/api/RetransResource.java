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

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Retransmision;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.RetransmisionCollection;

@Path("/campeonato/{idcampeonato}/{idpartido}/retra")
public class RetransResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext security;

	@GET
	@Produces(MediaType.FUTBOL_API_RETRA)
	public RetransmisionCollection getRetrans(
			@PathParam("idpartido") String idpart,
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

		RetransmisionCollection retrans = new RetransmisionCollection();
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
				sql = "select * from retransmision where (nombre like '%"
						+ pattern + "%' and idPartido=" + idpart + ")"
						+ " LIMIT " + offset + "," + length;
				;
			} else {
				sql = "select * from retransmision where idPartido=" + idpart
						+ " LIMIT " + offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Retransmision retra = new Retransmision();
				retra.setTiempo(rs.getString("tiempo"));
				retra.setTexto(rs.getString("texto"));
				// addlink
				retrans.addRetrans(retra);
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
		return retrans;
	}

	@GET
	@Path("/{idretra}")
	@Produces(MediaType.FUTBOL_API_RETRA)
	public Retransmision getRetra(@PathParam("idretra") String idretra) {
		Retransmision retra = new Retransmision();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = null;
			sql = "select * from retransmision where id=" + idretra;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// retra.setId(idretra);
				retra.setIdPartido(rs.getString("idPartido"));
				retra.setTiempo(rs.getString("tiempo"));
				retra.setTexto(rs.getString("texto"));
				retra.setMedia(rs.getString("media"));
				// addlink
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return retra;
	}

	@POST
	@Produces(MediaType.FUTBOL_API_RETRA)
	@Consumes(MediaType.FUTBOL_API_RETRA)
	public Retransmision crearRetra(@PathParam("idpartido") String idpartido,
			Retransmision retra) {
		if (!security.isUserInRole("administrator")) {
			throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into retransmision (idPartido,tiempo,texto,media) values ('"
					+ idpartido
					+ "', '"
					+ retra.getTiempo()
					+ "', '"
					+ retra.getTexto() + "', '" + retra.getMedia() + "')";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				retra.setId(Integer.toString(id));
				retra.setIdPartido(idpartido);
				rs.close();
				stmt.close();
				conn.close();
			} else {
				throw new RetraNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return retra;
	}

	@PUT
	@Path("/{idretra}")
	@Produces(MediaType.FUTBOL_API_RETRA)
	@Consumes(MediaType.FUTBOL_API_RETRA)
	public Retransmision actualizaRetra(@PathParam("idretra") String id,
			@PathParam("idpartido") String idpartido, Retransmision retra) {
		if (!security.isUserInRole("administrator")) {
			throw new ForbiddenException("DENEGADO: FALTA PERMISOS");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "update retransmision set retransmision.tiempo='"
					+ retra.getTiempo() + "', retransmision.texto='"
					+ retra.getTexto() + "',retransmision.media='"
					+ retra.getMedia() + "' where (retransmision.id=" + id
					+ " AND retransmision.idPartido='" + idpartido + "')";

			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new RetraNotFoundException();
			retra.setId(id);
			retra.setIdPartido(idpartido);
			// /setlinks
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return retra;
	}

	@DELETE
	@Path("/{idretra}")
	public void borrarRetra(@PathParam("idpartido") String idpartido,
			@PathParam("idretra") String id) {
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
			sql = "delete from retransmision where (idPartido=" + idpartido
					+ " AND id='" + id + "')";

			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new BadRequestException("NO EXISTE");

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
