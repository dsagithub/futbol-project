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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.CampeonatosLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Campeonatos;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.CampeonatosCollection;


@Path("/campeonato")
public class CampeonatosResource {
	
		@Context
		private SecurityContext security;
		private DataSource ds = DataSourceSPA.getInstance().getDataSource();
		@Context
		private UriInfo uriInfo;
		@GET
		@Path("/{idcampeonato}")
		@Produces(MediaType.FUTBOL_API_CAMPEONATOS)
		public Campeonatos getCampeonatos(
				@PathParam("idcampeonato") int idcampeonato) {
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
				String sql = "select * from Campeonatos where idCampeonatos='"
						+ idcampeonato + "'";
				rs = stmt.executeQuery(sql);
				String offset = "0";
				String length = "15";
				String pattern = null;
				if (rs.next()) {
					campeonato.setIdcampeonatos(rs.getInt("idCampeonatos"));
					campeonato.setNombre(rs.getString("nombre"));
					campeonato.addLink(CampeonatosLinkBuilder.buildURICampeonatoId(uriInfo, "self", campeonato.getIdcampeonatos()));
					campeonato.addLink(CampeonatosLinkBuilder.buildURICalendarioId(uriInfo,
							"Calendario", campeonato.getIdcampeonatos() , offset, length,
							pattern));
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
			
			if (!security.isUserInRole("administrator"))
			{
				throw new ForbiddenException("Solo el administrador puede borrar un campeonato");
			}
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
					throw new ClubNotFoundException("No existe ningún campeonato con esa id");

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
			
			if (!security.isUserInRole("administrator"))
		{
				throw new ForbiddenException("Solo el administrador puede realizar un post");
			}	
			
			if (campeonato.getNombre() == null) {
				throw new BadRequestException("Nombre no puede ser null");
			}
			
			if (campeonato.getNombre().length()>45){
				throw new BadRequestException("El nombre del campeonato ha de ser menor de 45 carácteres");
			}
			
			if (campeonato.getNombre().length()==0)
			{
				throw new BadRequestException("El nombre del campeonato no puede estar vacío");
			}


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
				String sql = "insert into Campeonatos (idCampeonatos,nombre) values ('"
						+ campeonato.getIdcampeonatos() + "', '" + campeonato.getNombre() + "')";
				stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int idCampeonato = rs.getInt(1);
					campeonato.setIdcampeonatos(idCampeonato);
					campeonato.addLink(CampeonatosLinkBuilder.buildURICampeonatoId(uriInfo, "self", campeonato.getIdcampeonatos()));
					campeonato.addLink(CampeonatosLinkBuilder.buildURICalendarioId(uriInfo,
							"Calendario", campeonato.getIdcampeonatos() , "0", "5",
							null));
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
				

				@PathParam("idcampeonato") int idcampeonato , Campeonatos campeonato) {

			if (!security.isUserInRole("administrator"))
				{
					throw new ForbiddenException("Solo el administrador puede realizar una actualización a los campeonatos");
				}
				
			if (campeonato.getNombre().length()>45){
				throw new BadRequestException("El nombre del campeonato ha de ser menor de 45 carácteres");
			}
			
			if (campeonato.getNombre().length()==0)
			{
				throw new BadRequestException("El nombre del campeonato no puede estar vacío");
			}
			Connection conn = null;
			if (campeonato.getNombre() == null) {
				throw new BadRequestException("Nombre no puede ser null");
			}
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServiceUnavailableException(e.getMessage());
			}
			try {
				Statement stmt = conn.createStatement();
				campeonato.setIdcampeonatos(idcampeonato);
				
				String sql = "update Campeonatos set Campeonatos.nombre='" + campeonato.getNombre()						
						  + "' where Campeonatos.idCampeonatos=" + idcampeonato;

				int rs2 = stmt.executeUpdate(sql);
				if (rs2 == 0)
					throw new CampeonatoNotFoundException();
				
				campeonato.addLink(CampeonatosLinkBuilder.buildURICampeonatoId(uriInfo, "self", campeonato.getIdcampeonatos()));
				campeonato.addLink(CampeonatosLinkBuilder.buildURICalendarioId(uriInfo,
						"Calendario", campeonato.getIdcampeonatos() , "0", "5",
						null));
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
			return campeonato;
		}
		
		@GET
		@Produces(MediaType.FUTBOL_API_CAMPEONATOS_COLLECTION)
		public CampeonatosCollection getcampeonatos(
				@QueryParam("pattern") String pattern,
				@QueryParam("offset") String offset,
				@QueryParam("length") String length) {
			if ((offset == null) || (length == null))
				throw new BadRequestException("Indica un offset y un length ");
			int ioffset, ilength, icount = 0;
			try {
				ioffset = Integer.parseInt(offset);
				if (ioffset < 0)
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				throw new BadRequestException(
						"Offset es un entero igual o mayor de 0.");
			}
			try {
				ilength = Integer.parseInt(length);
				if (ilength < 1)
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				throw new BadRequestException(
						"Lenght ha de ser entero igual o mayor a 1.");
			}

			CampeonatosCollection ccol = new CampeonatosCollection();

			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServiceUnavailableException(e.getMessage());
			}

			try {
				Statement stmt = conn.createStatement();
				String sql = null;

				
				sql = "select * from Campeonatos LIMIT " 
				+ offset + "," + length;
				
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {

					Campeonatos campeonato = new Campeonatos();
					campeonato.setIdcampeonatos(rs.getInt("idCampeonatos"));
					campeonato.setNombre(rs.getString("nombre"));
					campeonato.addLink(CampeonatosLinkBuilder.buildURICalendarioId(uriInfo,
							"Calendario", campeonato.getIdcampeonatos() , offset, length,
							pattern));
					campeonato.addLink(CampeonatosLinkBuilder.buildURICampeonatoId(uriInfo, "self", campeonato.getIdcampeonatos()));
					ccol.addCampeonatos(campeonato);
					
					
					
					icount++;
					String nextoffset = "" + (ioffset + ilength);

					if (ilength <= icount) {
						
						ccol.addLink(CampeonatosLinkBuilder.buildURICampeonatos(uriInfo, nextoffset, length, pattern, "next"));

					}
					}
				
				String prevoffset = "" + (ioffset - ilength);
				int cuenta = ioffset - ilength;
				if (ioffset == 0)
				{
				ccol.addLink(CampeonatosLinkBuilder.buildURICampeonatos(uriInfo, offset, length, pattern, "self"));
				}
				else
				{

					if (cuenta >=  0) {
					ccol.addLink(CampeonatosLinkBuilder.buildURICampeonatos(uriInfo, prevoffset, length, pattern, "prev"));
					}
					else {
						
						ccol.addLink(CampeonatosLinkBuilder.buildURICampeonatos(uriInfo, "0", length, pattern, "prev"));
						
					}
					ccol.addLink(CampeonatosLinkBuilder.buildURICampeonatos(uriInfo, offset, length, pattern, "self"));
					
				}
				
				
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}

			return ccol;
		}

		
	}
