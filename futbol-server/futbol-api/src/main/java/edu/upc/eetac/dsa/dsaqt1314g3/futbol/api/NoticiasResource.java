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
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.DataSourceSPA;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Noticia;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.NoticiasCollection;


@Path("/club/{idClub}/noticias")
public class NoticiasResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/{idnoticia}")
	@Produces(MediaType.FUTBOL_API_NOTICIA)
	public Noticia getNoticia(@PathParam("idnoticia") String idNoticia, @Context Request req) {
		CacheControl cc = new CacheControl();
		Noticia noticia = new Noticia();
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
			String sql = "select * from Noticias where idNoticias=" + idNoticia;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				noticia.setIdNoticia(rs.getInt("idNoticias"));
				noticia.setIdClub(rs.getInt("idClub"));
				noticia.setTitulo(rs.getString("titulo"));
				noticia.setContent(rs.getString("content"));
				noticia.setMedia(rs.getString("media"));
				noticia.setLastModified(rs.getTimestamp("lastModified"));
				//----------
				noticia.addLink(null);
				noticia.addLink(null);
			} else {
				throw new NoticiaNotFoundException();
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
		/*EntityTag eTag = new EntityTag(Integer.toString(noticia.getLastModified()
				.hashCode()));
		Response.ResponseBuilder rb = req.evaluatePreconditions(eTag);
		
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}
		
		rb = Response.ok(noticia).cacheControl(cc).tag(eTag);*/

		return noticia;
		
	}
	
	@DELETE
	@Path("/{idnoticia}")
	public void deleteNoticia(@PathParam("idnoticia") int idNoticia) {
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
			sql = "delete from Noticias where idNoticias=" + idNoticia;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new NoticiaNotFoundException();

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
	@Consumes(MediaType.FUTBOL_API_NOTICIA)
	@Produces(MediaType.FUTBOL_API_NOTICIA)
	public Noticia createNoticia(Noticia noticia) {
		if (noticia.getTitulo().length() > 100) {
			throw new BadRequestException(
					"title length must be less or equal than 100 characters");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into Noticias (idNoticias, idClub, titulo, content, media, lastModified) values ('"
					+ noticia.getIdNoticia() 	
					+ "', '"
					+ noticia.getIdClub()
					+ "', '" 
					+ noticia.getTitulo()
					+ "', '" 
					+ noticia.getContent()
					+ "', '" 
					+ noticia.getMedia()
					+ "', '" 
					+ noticia.getLastModified() + "')";
					
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int noticiaid = rs.getInt(1);
				noticia.setIdNoticia(noticiaid);
				
				String sql2 = "select Noticias.lastModified from Noticias where Noticias.idNoticia = "
						+ noticiaid;
				rs = stmt.executeQuery(sql2);
				if (rs.next()) {
					noticia.setLastModified(rs.getTimestamp(1));
					// links
				}
				rs.close();
				stmt.close();
				conn.close();

			} else {
				throw new NoticiaNotFoundException();
			}
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return noticia;
	}
	
	@PUT
	@Path("/{idnoticia}")
	@Consumes(MediaType.FUTBOL_API_NOTICIA)
	@Produces(MediaType.FUTBOL_API_NOTICIA)
	public Noticia updateNoticia(@PathParam("idnoticia") int idNoticia, Noticia noticia) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		try {
			Statement stmt = conn.createStatement();
			String sql = "update Noticias set Noticias.idClub='" + noticia.getIdClub()
					+ "',Noticias.titulo='" + noticia.getTitulo() + "',Noticias.content='"
					+ noticia.getContent() + "',Noticias.media='"
					+ noticia.getMedia() + "' where Noticias.idNoticias=" + idNoticia;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new NoticiaNotFoundException();
			sql = "select Noticias.lastModified from Noticias where Noticias.idNoticia = " + idNoticia;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				noticia.setLastModified(rs.getTimestamp("lastModified"));
				noticia.setIdNoticia(idNoticia);
				// links
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return noticia;
	}
	
	@GET
	@Produces(MediaType.FUTBOL_API_NOTICIAS_COLLECTION)
	public NoticiasCollection getNoticias(@QueryParam("titulo") String titulo,
			@QueryParam("idClub") int idClub,
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
		NoticiasCollection noticias = new NoticiasCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			Statement stmt = conn.createStatement();
			String sql = null;
			if (idClub != 0 && titulo != null) {
				sql = "select * from Noticias where (titulo like '%" + titulo
						+ "%' AND idClub like '%" + idClub
						+ "%') ORDER BY lastModified desc LIMIT " + offset + ","
						+ length;
			} else if (idClub == 0 && titulo != null) {
				sql = "select * from Noticias where titulo like '%" + titulo
						+ "%' ORDER BY lastModified desc LIMIT " + offset + ","
						+ length;
			} else if (titulo == null && idClub != 0) {
				sql = "select * from Noticias where idClub like '%" + idClub
						+ "%' ORDER BY lastModified desc LIMIT " + offset + ","
						+ length;
			} else {
				sql = "select * from Noticias ORDER BY lastModified LIMIT "
						+ offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Noticia noticia = new Noticia();
				noticia.setIdNoticia(rs.getInt("idNoticias"));
				noticia.setIdClub(rs.getInt("idClub"));
				noticia.setTitulo(rs.getString("titulo"));
				noticia.setContent(rs.getString("content"));
				noticia.setMedia(rs.getString("media"));
				noticia.setLastModified(rs.getTimestamp("lastModified"));
				//links
				
				noticias.addNoticia(noticia);
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
		return noticias;
	}
	

}
