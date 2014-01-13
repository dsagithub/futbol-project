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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.DataSourceSPA;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.ClubLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.EquiposLinkBuilder;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Club;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.CalendarioCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.ClubCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.BadRequestException;






@Path("/club/")
public class ClubResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext security;

	@GET
	@Produces(MediaType.FUTBOL_API_CLUB_COLLECTION)
	public ClubCollection getclubs(@QueryParam("pattern") String nombre,
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

		ClubCollection clubs = new ClubCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}

		try {
			Statement stmt = conn.createStatement();
			String sql = null;

			if (nombre != null) {
				sql = "select * from Club where nombre like '%" + nombre
						+ "%' LIMIT " + offset + "," + length;
			} 
			else  {
				sql = "select * from Club LIMIT " 
			+ offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				Club club = new Club();
				club.setIdClub(rs.getString("idClub"));
				club.setNombre(rs.getString("nombre"));
				club.addLink(ClubLinkBuilder.buildURIEquipos2(uriInfo, "0", "15", null, "Lista equipos", club.getIdClub()));
				clubs.addClub(club);
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
		clubs.addLink(ClubLinkBuilder.buildURIClubs(uriInfo, prevoffset, length, nombre, "prev"));
		
		}
		clubs.addLink(ClubLinkBuilder.buildURIClubs(uriInfo, offset, length, nombre, "self"));
		String nextoffset = "" + (ioffset + ilength);
		if (ilength <= icount) {
		clubs.addLink(ClubLinkBuilder.buildURIClubs(uriInfo, nextoffset, length, nombre, "next"));
	}
		
		return clubs;
	}

	@GET
	@Path("{idClub}")
	@Produces(MediaType.FUTBOL_API_CLUB)
	public Club getClub(@PathParam("idClub") String idClub){
		
		Club club = new Club();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.createStatement();
			String sql = "select * from Club where idClub=" + idClub;
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				club.setIdClub(idClub);
				club.setNombre(rs.getString("nombre"));
				club.addLink(ClubLinkBuilder.buildURIClubId(uriInfo, "self", club.getIdClub()));
				
				
			}
			else
			{
				throw new ClubNotFoundException("No existe ningún club con esa id");
			}
		}
		catch (SQLException e) 
		{
			throw new InternalServerException(e.getMessage());
		}
		 finally 
		 {
			try 
			{
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					throw new InternalServerException(e.getMessage());
				}
			}
		
		return club;
		
		}
	
	@POST
	@Consumes(MediaType.FUTBOL_API_CLUB)
	@Produces(MediaType.FUTBOL_API_CLUB)
	public Club createClub(Club club)
	{
		if (!security.isUserInRole("administrator"))
		{
			throw new ForbiddenException("Solo administrador puede realizar esta acción");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		
		if (club.getNombre().length()>45){
			throw new BadRequestException("El nombre del club ha de ser menor de 45 carácteres");
		}
		
		if (club.getNombre().length()==0)
		{
			throw new BadRequestException("El nombre del club no puede estar vacío");
		}
	
		try{
			Statement stmt = conn.createStatement();
			String sql = "insert into Club (nombre) values ('"
			+ club.getNombre()
			+"')";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
		// Si no hay lasModified las 2 siguientes lineas sobran
		int  clubid = rs.getInt(1);
		club.setIdClub(Integer.toString(clubid));
		club.addLink(ClubLinkBuilder.buildURIClubId(uriInfo, "self", club.getIdClub()));
			
			rs.close();
			stmt.close();
			conn.close();
			}
			else {
				throw new ClubNotFoundException("Error");
			}
			
			
			}
		 catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
		return club;
		}
	
	@DELETE
	@Path("{idClub}")
	public void deleteSting(@PathParam("idClub") String id) {
		Connection conn = null;
		
		if (!security.isUserInRole("administrator"))
		{
			throw new ForbiddenException("Solo administrador puede realizar esta acción");
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
			sql = "delete from Club where idClub=" + id;
			int rs2 = stmt.executeUpdate(sql);
			if (rs2 == 0)
				throw new ClubNotFoundException("No existe ningún club con esa id");

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
	@PUT
	@Path("{idClub}")
	@Consumes(MediaType.FUTBOL_API_CLUB)
	@Produces(MediaType.FUTBOL_API_CLUB)
	public Club updateClub(@PathParam("idClub") String id, Club club) {
	
		if (!security.isUserInRole("administrator"))
		{
			throw new ForbiddenException("Solo administrador puede realizar esta acción");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		if (club.getNombre().length()>45){
			throw new BadRequestException("El nombre del club ha de ser menor de 45 carácteres");
		}
		
		if (club.getNombre().length()==0)
		{
			throw new BadRequestException("El nombre del club no puede estar vacío");
		}
		
	
		try {
			Statement stmt = conn.createStatement();
			String sql = "update Club set Club.nombre='" + club.getNombre()
					  + "' where Club.idClub=" + id;
			int rs2 = stmt.executeUpdate(sql);
		
			if (rs2 == 0)
				throw new ClubNotFoundException("No existe equipo para actualizar");
			club.setIdClub(id);
			
			club.addLink(ClubLinkBuilder.buildURIClubId(uriInfo, "self", club.getIdClub()));
		
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		return club;
	} 
	
	}

	


