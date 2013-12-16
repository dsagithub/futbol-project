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
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Noticia;


@Path("/noticias")
public class NoticiasResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/{idnoticia}")
	@Produces(MediaType.FUTBOL_API_NOTICIA)
	public Response getNoticia(@PathParam("idnoticia") int idNoticia, @Context Request req) {
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
				noticia.setIdNoticia(rs.getInt("idNoticia"));
				noticia.setIdClub(rs.getInt("idClub"));
				noticia.setTitulo(rs.getString("titulo"));
				noticia.setContent(rs.getString("content"));
				noticia.setMedia(rs.getString("media"));
				noticia.setLastModified(rs.getTimestamp("lastModified"));
				//----------
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
		EntityTag eTag = new EntityTag(Integer.toString(noticia.getLastModified().hashCode()));
		Response.ResponseBuilder rb = req.evaluatePreconditions(eTag);
		
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}
		
		rb = Response.ok(noticia).cacheControl(cc).tag(eTag);

		return rb.build();
		
	}

}
