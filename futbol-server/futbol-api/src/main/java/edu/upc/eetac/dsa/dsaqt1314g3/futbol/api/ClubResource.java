package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Club;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.ClubCollection;

@Path("/clubs")
public class ClubResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Context
	private UriInfo uriInfo;

	@GET
	@Produces(MediaType.FUTBOL_API_CLUB_COLLECTION)
	public ClubCollection getclubs(@QueryParam("nombre") String nombre,
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
			} else {
				slq = "select * from Club LIMIT" + offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				Club club = new Club();
				club.setIdClub(rs.getString("idClub"));
				club.setNombre(rs.getString("nombre"));
				// Faltan links!
				clubs.addClub(club);
				icount++;
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}

		// links!
		return clubs;
	}

	@GET
	@Path("/{idClub}")
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
			String sql = "select * from Club where id=" + idClub;
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				club.setIdClub("idClub");
				club.setNombre("nombre");
				//addlinks
			}
			else
			{
				throw new ClubNotFoundException("No existe ningún club con esa id")
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
		
		
		
		if (club.getNombre().length()>45){
			throw new BadRequestException("El nombre del club ha de ser menor de 45 carácteres);
		}
		
		Connection conn = null;
		try{
			Statement stmt = conn.createStatement();
			string sql = "insert into Club (nombre) values('"
			+ club.getNombre()
			+"')";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				
			club.setIdClub(rs.getInt(1));
			//If con links
			rs.close();
			stmt.close();
			conn.close();
			}
			else {
				throw new //;
			}
			
			
			}
		 catch (SQLException e) {
				throw new InternalServerException(e.getMessage());
			}
		return club;
		}
		
	}//final del resource
	


