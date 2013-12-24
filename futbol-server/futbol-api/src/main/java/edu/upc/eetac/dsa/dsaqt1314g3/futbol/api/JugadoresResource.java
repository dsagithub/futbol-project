package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Jugadores;


@Path("/{idclub}/{idequipo}/jugadores")

public class JugadoresResource {

	@Context
    private UriInfo uriInfo;
    private DataSource ds = DataSourceSPA.getInstance().getDataSource();	
	@GET
	@Path("/{dni}")
	@Produces(MediaType.FUTBOL_API_JUGADORES)
    public Jugadores getJugador(@PathParam("dni") String dni, @PathParam("idclub") String idclub, @PathParam("idequipo") String idequipo, @Context Request req){
		CacheControl cc = new CacheControl();
		Jugadores jugador = new Jugadores();
		Connection conn = null;
        Statement stmt = null;
         try {
                 conn = ds.getConnection();
         } catch (SQLException e) {
                 throw new ServiceUnavailableException(e.getMessage());
         }
         
         try {
                 stmt = conn.createStatement();
                 String query = "SELECT * FROM Jugadores WHERE dni=" + dni + " and idequipo ="+idequipo+";";
                 ResultSet rs = stmt.executeQuery(query);
                 if (rs.next()) {
                         jugador.setDni(rs.getString("dni"));
                         jugador.setNombre(rs.getString("nombre"));
                         jugador.setApellidos(rs.getString("apellidos"));
                         jugador.setIdequipo(rs.getInt("IdEquipo"));
                 } else
                       throw new JugadorNotFoundException();
         } catch (SQLException e) {
                 throw new InternalServerException(e.getMessage());
         }
         finally {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
		}
	}
	return jugador;
}
	
	@POST
    @Consumes(MediaType.FUTBOL_API_JUGADORES)
    @Produces(MediaType.FUTBOL_API_JUGADORES)
    public Jugadores createJugador(@PathParam("idclub") String idclub, @PathParam("idequipo") String idequipo, Jugadores jugador) {
        Statement stmt = null;
        Connection conn = null;
        try {
                conn = ds.getConnection();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServiceUnavailableException(e.getMessage());
        }

        
        try {
            stmt = conn.createStatement();
            String sql = "select * from jugadores where dni='"
                            + jugador.getDni() + "' and nombre='"
                            + jugador.getNombre() + "'and apellidos='"
                            + jugador.getApellidos() + "'and idequipo='"
                            + jugador.getIdequipo() + "';";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
                throw new JugadorNotFoundException();
        sql = "insert into jugadores (dni, nombre, apellidos, idequipo) ";
        sql += "values (" + jugador.getDni() + ", '"
                         + jugador.getNombre() + "', '"
                         + jugador.getApellidos() + "', '"
                         + jugador.getIdequipo()
                        + "');";
		
        stmt.executeUpdate(sql);
		stmt.close();
		conn.close();
	} catch (SQLException e) {
		throw new InternalServerException(e.getMessage());
	}
	return jugador;
}

 }

		



