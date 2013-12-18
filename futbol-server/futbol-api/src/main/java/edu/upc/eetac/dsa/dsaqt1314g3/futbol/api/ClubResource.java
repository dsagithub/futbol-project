package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.DataSourceSPA;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Club;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.CalendarioCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.ClubCollection;


@Path("/")
public class ClubResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces(MediaType.FUTBOL_API_CLUB_COLLECTION)
	public ClubCollection getclubs(@QueryParam("nombre") String nombre,
			@QueryParam("offset") String offset,
			@QueryParam("length") String length)
	{
		if ((offset == null) || (length == null))
			throw new BadRequestException(
					"Indica un offset y un length ");
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
		
		try{
			Statement stmt = conn.createStatement();
			String sql = null;
			
			if(nombre != null)
			{
				sql = "select * from clubs where nombre like '%" + nombre
						+ "%' LIMIT " + offset
						+ "," + length;
			}
			else 
			{
				slq= "select * from clubs LIMIT" + offset + "," + length;
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				
				Club club = new Club();
				club.setIdClub(rs.getString("idClub"));
				club.setNombre(rs.getString("nombre"));
				//Faltan links!
				clubs.addClub(club);
				icount++;
			}
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (SQLException e) {
			throw new InternalServerException(e.getMessage());
		}
		
		//links!
		return clubs;
	}
	
	@GET
	@Path("/{idClub}")
	@Produces(MediaType.FUTBOL_API_CLUB)
	
	
	
	
	
	
	
}
