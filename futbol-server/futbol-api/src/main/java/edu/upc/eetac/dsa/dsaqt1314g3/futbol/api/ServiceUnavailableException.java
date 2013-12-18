package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.FutbolError;



public class ServiceUnavailableException extends WebApplicationException {
	public ServiceUnavailableException(String message) {
		super(Response
				.status(Response.Status.SERVICE_UNAVAILABLE)
				.entity(new FutbolError(Response.Status.SERVICE_UNAVAILABLE
						.getStatusCode(), message))
				.type(MediaType.FUTBOL_API_ERROR).build());
	}
}
