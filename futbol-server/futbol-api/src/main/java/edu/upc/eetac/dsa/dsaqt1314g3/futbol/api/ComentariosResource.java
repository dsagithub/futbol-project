package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.ComentariosLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Comentario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.ComentariosCollection;

@Path("/campeonato/{idCampeonato}/calendario/{idPartido}/comentarios")
public class ComentariosResource {
private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext security;
	
	@GET
	@Path("/{idcomentario}")
	@Produces(MediaType.FUTBOL_API_COMENTARIO)
	public Comentario getComentario(@PathParam("idcomentario") String idComentario, 
			@PathParam("idPartido") String idPartido,
			@PathParam("idCampeonato") String idCampeonato,
			@Context Request req) {
		Comentario comentario = new Comentario();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			String sql = "select * from Comentarios where idComentarios=" + idComentario;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				comentario.setIdComentario(rs.getInt("idComentarios"));
				comentario.setTiempo(rs.getString("tiempo"));
				comentario.setMedia(rs.getString("media"));
				comentario.setTexto(rs.getString("texto"));
				comentario.setIdPartido(rs.getString("idPartido"));
				comentario.setIdUsuario(rs.getInt("idUsuario"));
				//links
				comentario.addLink(ComentariosLinkBuilder.buildURIComentarioId(uriInfo,
						"self",idCampeonato, idPartido, comentario.getIdComentario()));
				comentario.addLink(ComentariosLinkBuilder.buildURICalendarioId(uriInfo,
						"Calendario",idCampeonato, idPartido));
				comentario.addLink(ComentariosLinkBuilder.buildURICampeonatoId(uriInfo,
						"Campeonato",idCampeonato));
			} else {
				throw new ComentarioNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
		}

		return comentario;
		
	}
	
	@DELETE
	@Path("/{idcomentario}")
	public void deleteComentario(@PathParam("idcomentario") int idComentario) {
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
			sql = "delete from Comentarios where idComentarios=" + idComentario;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new ComentarioNotFoundException();

		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@POST
	@Consumes(MediaType.FUTBOL_API_COMENTARIO)
	@Produces(MediaType.FUTBOL_API_COMENTARIO)
	public Comentario createComentario(@PathParam("idCampeonato") String idCampeonato, Comentario comentario) {
		if (comentario.getTexto().length() > 100) {
			throw new BadRequestException(
					"texto length must be less or equal than 100 characters");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into Comentarios (idComentarios, tiempo, media, texto, idPartido, idUsuario) values ('"
					+ comentario.getIdComentario() 	
					+ "', '"
					+ comentario.getTiempo()
					+ "', '" 
					+ comentario.getMedia()
					+ "', '" 
					+ comentario.getTexto()
					+ "', '" 
					+ comentario.getIdPartido()
					+ "', '" 
					+ comentario.getIdUsuario() + "')";
					
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int comentarioid = rs.getInt(1);
				comentario.setIdComentario(comentarioid);
				
				String sql2 = "select Comentarios.tiempo from Comentarios where Comentarios.idComentarios = "
						+ comentarioid;
				rs = stmt.executeQuery(sql2);
				if (rs.next()) {
					// links
					comentario.addLink(ComentariosLinkBuilder.buildURIComentarioId(uriInfo,
							"self",idCampeonato, comentario.getIdPartido(), comentario.getIdComentario()));
					comentario.addLink(ComentariosLinkBuilder.buildURICalendarioId(uriInfo,
							"Calendario",idCampeonato, comentario.getIdPartido()));
					comentario.addLink(ComentariosLinkBuilder.buildURICampeonatoId(uriInfo,
							"Campeonato",idCampeonato));
				}
				rs.close();
				stmt.close();
				conn.close();

			} else {
				throw new ComentarioNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return comentario;
	}
	
	@PUT
	@Path("/{idcomentario}")
	@Consumes(MediaType.FUTBOL_API_COMENTARIO)
	@Produces(MediaType.FUTBOL_API_COMENTARIO)
	public Comentario updateComentario(@PathParam("idCampeonato") String idCampeonato,
			@PathParam("idcomentario") int idComentario, Comentario comentario) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "update Comentarios set Comentarios.tiempo='" + comentario.getTiempo()
					+ "',Comentarios.media='" + comentario.getMedia() 
					+ "',Comentarios.texto='" + comentario.getTexto() 
					+ "',Comentarios.media='" + comentario.getMedia()
					+ "',Comentarios.idPartido='" + comentario.getIdPartido() 
					+ "',Comentarios.idUsuario='" + comentario.getIdUsuario() 
					+ "' where Comentarios.idComentarios=" + idComentario;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new ComentarioNotFoundException();
			sql = "select Comentarios.tiempo from Comentarios where Comentarios.idComentarios = " + idComentario;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				comentario.setIdComentario(idComentario);
				// links
				comentario.addLink(ComentariosLinkBuilder.buildURIComentarioId(uriInfo,
						"self",idCampeonato, comentario.getIdPartido(), comentario.getIdComentario()));
				comentario.addLink(ComentariosLinkBuilder.buildURICalendarioId(uriInfo,
						"Calendario",idCampeonato, comentario.getIdPartido()));
				comentario.addLink(ComentariosLinkBuilder.buildURICampeonatoId(uriInfo,
						"Campeonato",idCampeonato));
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return comentario;
	}
	
	@GET
	@Produces(MediaType.FUTBOL_API_COMENTARIOS_COLLECTION)
	public ComentariosCollection getComentarios(@PathParam("idPartido") String idPartido,
			@PathParam("idCampeonato") String idCampeonato,
			@QueryParam("idComentario") String idComentario,
			@QueryParam("offset") String offset,
			@QueryParam("length") String length) {
		if ((offset == null) || (length == null))
			throw new BadRequestException(
					"offset and length are mandatory parameters");
		int ioffset, ilength, icount = 0;
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
		ComentariosCollection comentarios = new ComentariosCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			Statement stmt = conn.createStatement();
			String sql = null;
			if (idComentario != null && idPartido != null) {
				sql = "select * from Comentarios where (idPartido like '%" + idPartido
						+ "%' AND idComentarios like '%" + idComentario
						+ "%') ORDER BY tiempo desc LIMIT " + offset + ","
						+ length;
			} else if (idComentario == null && idPartido != null) {
				sql = "select * from Comentarios where idPartido like '%" + idPartido
						+ "%' ORDER BY tiempo desc LIMIT " + offset + ","
						+ length;
			} else if (idPartido == null && idComentario != null) {
				sql = "select * from Comentarios where idComentarios like '%" + idComentario
						+ "%' ORDER BY tiempo desc LIMIT " + offset + ","
						+ length;
			} else {
				sql = "select * from Comentarios ORDER BY tiempo LIMIT "
						+ offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Comentario comentario = new Comentario();
				comentario.setIdComentario(rs.getInt("idComentarios"));
				comentario.setTiempo(rs.getString("tiempo"));
				comentario.setMedia(rs.getString("media"));
				comentario.setTexto(rs.getString("texto"));
				comentario.setIdPartido(rs.getString("idPartido"));
				comentario.setIdUsuario(rs.getInt("idUsuario"));
				//links
				comentario.addLink(ComentariosLinkBuilder.buildURIComentarioId(uriInfo,
						"self",idCampeonato, idPartido, comentario.getIdComentario()));
				comentario.addLink(ComentariosLinkBuilder.buildURICalendarioId(uriInfo,
						"Calendario",idCampeonato, idPartido));
				comentario.addLink(ComentariosLinkBuilder.buildURICampeonatoId(uriInfo,
						"Campeonato",idCampeonato));			
				comentarios.addComentario(comentario);
				icount++;
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		if (ioffset != 0) {
			String prevoffset = null;
			if ((ioffset - ilength) < 0)
				prevoffset = "" + (0);
			else
				prevoffset = "" + (ioffset - ilength);
			comentarios.addLink(ComentariosLinkBuilder.buildURIComentarios(uriInfo,
					prevoffset, length, idComentario, "prev", idPartido, idCampeonato));
		}
		comentarios.addLink(ComentariosLinkBuilder.buildURIComentarios(uriInfo, offset,
				length, idComentario, "self", idPartido, idCampeonato));
		String nextoffset = "" + (ioffset + ilength);
		if (ilength <= icount) {
			comentarios.addLink(ComentariosLinkBuilder.buildURIComentarios(uriInfo,
					nextoffset, length, idComentario, "next", idPartido, idCampeonato));
		}
		return comentarios;
	}
	

}
