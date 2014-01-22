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
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.EquiposLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Campeonatos;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.CampeonatosCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Equipo;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.EquipoCollection;


@Path("/campeonato")
public class CampeonatosResource {
	
		@Context
		private SecurityContext security;
		private DataSource ds = DataSourceSPA.getInstance().getDataSource();
		@Context
		private UriInfo uriInfo;
		
		@GET
		@Path("/{idcampeonato}/equipos")
		@Produces(MediaType.FUTBOL_API_EQUIPO_COLLECTION)
		public EquipoCollection getEquipos(@PathParam("idcampeonato") String idcamp,
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
							+ "%' and idcampeonatos=" + idcamp + ")";
				} else {
						sql = "select * from equipo where idcampeonatos=" + idcamp
								+ " LIMIT " + offset + "," + length;
					
				}
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					Equipo equipo2 = new Equipo();
					equipo2.setIdClub(rs.getString("idclub"));
					equipo2.setIdEquipo(rs.getString("idequipo"));
					equipo2.setNombre(rs.getString("nombre"));
					equipo2.setCampeonato(rs.getString("idcampeonatos"));
					equipo2.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo,
							"self", equipo2.getIdClub(), equipo2.getIdEquipo()));

					equipo2.addLink(EquiposLinkBuilder.buildURICalendarioId(
							uriInfo, "Calendario", equipo2.getCampeonato(), offset,
							length, pattern));
					equipos.addEquipo(equipo2);
					icount++;
					while (rs.next()) {
						Equipo equipo = new Equipo();
						equipo.setIdClub(rs.getString("idclub"));
						equipo.setIdEquipo(rs.getString("idequipo"));
						equipo.setNombre(rs.getString("nombre"));
						equipo.setCampeonato(rs.getString("idcampeonatos"));
						equipo.addLink(EquiposLinkBuilder.buildURIEquipoId(uriInfo,
								"self", equipo.getIdClub(), equipo.getIdEquipo()));
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
				equipos.addLink(EquiposLinkBuilder.buildURIEquipoCampeonato(uriInfo,
						prevoffset, length, pattern, "prev", idcamp));
			}
			equipos.addLink(EquiposLinkBuilder.buildURIEquipoCampeonato(uriInfo, offset,
					length, pattern, "self", idcamp));
			String nextoffset = "" + (ioffset + ilength);
			if (ilength <= icount) {
				equipos.addLink(EquiposLinkBuilder.buildURIEquipoCampeonato(uriInfo,
						nextoffset, length, pattern, "next", idcamp));
			}
			return equipos;
		}
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
				String sql = "select * from campeonatos where idcampeonatos='"
						+ idcampeonato + "'";
				rs = stmt.executeQuery(sql);
				String offset = "0";
				String length = "15";
				String pattern = null;
				if (rs.next()) {
					campeonato.setIdcampeonatos(rs.getInt("idcampeonatos"));
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
				sql = "delete from campeonatos where idcampeonatos='"
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
				String sql = "insert into campeonatos (idcampeonatos,nombre) values ('"
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
				
				String sql = "update campeonatos set campeonatos.nombre='" + campeonato.getNombre()						
						  + "' where campeonatos.idcampeonatos=" + idcampeonato;

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

				
				sql = "select * from campeonatos LIMIT " 
				+ offset + "," + length;
				
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {

					Campeonatos campeonato = new Campeonatos();
					campeonato.setIdcampeonatos(rs.getInt("idcampeonatos"));
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
