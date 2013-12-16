package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/{clubid}/{equipoid}")
public class EquipoResource {	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	@Context
	private UriInfo uriInfo;
	

}
