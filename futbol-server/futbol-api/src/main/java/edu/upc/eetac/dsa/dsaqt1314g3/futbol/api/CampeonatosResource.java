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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Campeonatos;


public class CampeonatosResource {
	
	@Path("/{idclub}/{idequipo}/Calendario/Campeonato")
	public class ClubResource {
	        
	        
	        @Context
	        private UriInfo uriInfo;
	        private DataSource ds = DataSourceSPA.getInstance().getDataSource();	
	    	@GET
	    	@Path("/{idcampeonato}")
	    	@Produces(MediaType.FUTBOL_API_CAMPEONATOS)
	        public Campeonatos getCampeonatos(@PathParam("idcampeonato") String idcampeonato, @PathParam("idclub") String idclub, @PathParam("idequipo") String idequipo, @Context Request req){
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
	    			String sql = "select * from campeonatos where idcampeonatos='" + idcampeonato
	    					+ "' and idequipo = " + idequipo + "'";
	    			rs = stmt.executeQuery(sql);
	    			if (rs.next()) {
	    				campeonato.setIdcampeonatos(rs.getString("Idcampeonato"));
	    				campeonato.setNombre(rs.getString("Nombre"));
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
	            
	}
}

