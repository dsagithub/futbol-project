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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Comentario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.ComentariosCollection;

//hacer funciones segun el nuevo path. Hay cosas que tienes como queryparam que ahora
//seran path param (ejemplo id partido) Lo mismo para Noticias!!!!
@Path("/campeonato/{idCampeonato}/calendario/{idPartido}/comentarios")
public class ComentariosResource {
private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/{idcomentario}")
	@Produces(MediaType.FUTBOL_API_COMENTARIO)
	public Comentario getComentario(@PathParam("idcomentario") String idComentario, @Context Request req) {
		CacheControl cc = new CacheControl();
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
			String sql = "select * from Comentarios where idComentario=" + idComentario;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				comentario.setIdComentario(rs.getInt("idComentario"));
				comentario.setTiempo(rs.getString("tiempo"));
				comentario.setMedia(rs.getString("media"));
				comentario.setTexto(rs.getString("texto"));
				comentario.setIdPartido(rs.getString("idPartido"));
				comentario.setIdUsuario(rs.getInt("idUsuario"));
				//----------
				comentario.addLink(null);
				comentario.addLink(null);
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
			sql = "delete from Comentarios where idComentario=" + idComentario;
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
	public Comentario createComentario(Comentario comentario) {
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
			String sql = "insert into Comentarios (idComentario, tiempo, media, texto, idPartido, idUsuario) values ('"
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
	public Comentario updateComentario(@PathParam("idcomentario") int idComentario, Comentario comentario) {
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
					+ "' where Comentarios.idComentario=" + idComentario;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new ComentarioNotFoundException();
			//----------
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return comentario;
	}
	
	@GET
	@Produces(MediaType.FUTBOL_API_COMENTARIOS_COLLECTION)
	public ComentariosCollection getNoticias(@QueryParam("idComentario") String idComentario,
			@QueryParam("idPartido") String idPartido,
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
						+ "%' AND idComentario like '%" + idComentario
						+ "%') ORDER BY tiempo desc LIMIT " + offset + ","
						+ length;
			} else if (idComentario == null && idPartido != null) {
				sql = "select * from Comentarios where idPartido like '%" + idPartido
						+ "%' ORDER BY tiempo desc LIMIT " + offset + ","
						+ length;
			} else if (idPartido == null && idComentario != null) {
				sql = "select * from Comentarios where idComentario like '%" + idComentario
						+ "%' ORDER BY tiempo desc LIMIT " + offset + ","
						+ length;
			} else {
				sql = "select * from Comentarios ORDER BY tiempo LIMIT "
						+ offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Comentario comentario = new Comentario();
				comentario.setIdComentario(rs.getInt("idComentario"));
				comentario.setTiempo(rs.getString("tiempo"));
				comentario.setMedia(rs.getString("media"));
				comentario.setTexto(rs.getString("texto"));
				comentario.setIdPartido(rs.getString("idPartido"));
				comentario.setIdUsuario(rs.getInt("idUsuario"));
				//links
				
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
			String prevoffset = "" + (ioffset - ilength);
			//links
		}
		//links
		String nextoffset = "" + (ioffset + ilength);
		if (ilength <= icount) {
			//links
		}
		return comentarios;
	}
	

}
