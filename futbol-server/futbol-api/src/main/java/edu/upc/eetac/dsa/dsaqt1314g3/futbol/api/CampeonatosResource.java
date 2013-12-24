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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Campeonatos;

public class CampeonatosResource {

	@Path("/campeonato")
	public class ClubResource {

		@Context
		private UriInfo uriInfo;
		private DataSource ds = DataSourceSPA.getInstance().getDataSource();

		@GET
		@Path("/{idcampeonato}")
		@Produces(MediaType.FUTBOL_API_CAMPEONATOS)
		public Campeonatos getCampeonatos(
				@PathParam("idcampeonato") int idcampeonato) {
			CacheControl cc = new CacheControl();
			Campeonatos campeonato = new Campeonatos();
			Connection conn = null;
			Statement stmt = null;

			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServiceUnavailableException(e.getMessage());
			}
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				String sql = "select * from Campeonatos where idcampeonatos='"
						+ idcampeonato + "'";
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					campeonato.setIdcampeonatos(rs.getInt("idCampeonato"));
					campeonato.setNombre(rs.getString("nombre"));
				} else {
					throw new CampeonatoNotFoundException();
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
			return campeonato;

		}

		
		@DELETE
		@Path("/{idcampeonato}")
		public void borrarcampeonato(
				@PathParam("idcampeonato") int idcampeonato ) {
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
				sql = "delete from Campeonatos where idCampeonatos='"
						+ idcampeonato + "'";

				int rs2 = stmt.executeUpdate(sql);
				if (rs2 == 0)
					throw new BadRequestException("No se puede realizar tal acción");

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
		
		
		@POST
		@Produces(MediaType.FUTBOL_API_CAMPEONATOS)
		@Consumes(MediaType.FUTBOL_API_CAMPEONATOS)
		public Campeonatos crearCampeonato( Campeonatos campeonato) {
			Connection conn = null;


			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServiceUnavailableException(e.getMessage());
			}
			if (campeonato.getNombre().length() > 45) {
				throw new BadRequestException(
						"Name length must be less or equal than 45 characters");
			}
			try {
				Statement stmt = conn.createStatement();
				String sql = "insert into Campeonatos (idCampeonato,nombre) values ('"
						+ campeonato.getIdcampeonatos() + "', '" + campeonato.getNombre() + "')";
				stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int idCampeonato = rs.getInt(1);
					campeonato.setIdcampeonatos(idCampeonato);
					rs.close();
					stmt.close();
					conn.close();
				} else {
					throw new CampeonatoNotFoundException();
				}
			} catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
			
			return campeonato;
		}
		
		
		@PUT
		@Path("/{idcampeonato}")
		@Produces(MediaType.FUTBOL_API_CAMPEONATOS)
		@Consumes(MediaType.FUTBOL_API_CAMPEONATOS)
		public Campeonatos actualizarCampeonato(
				

				@PathParam("idcampeonato") int idcampeonato) {

			Connection conn = null;
			Campeonatos campeonato = new Campeonatos();

			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServiceUnavailableException(e.getMessage());
			}
			try {
				Statement stmt = conn.createStatement();
				
				String sql = "update Campeonatos set Campeonatos.idCampeonatos ="
						+ campeonato.getNombre() + "' where Campeonato.idCampeonato="
						+ campeonato.getIdcampeonatos() +  "')";

				int rs2 = stmt.executeUpdate(sql);
				if (rs2 == 0)
					throw new CampeonatoNotFoundException();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
			return campeonato;
		}
		
		
	}
}
