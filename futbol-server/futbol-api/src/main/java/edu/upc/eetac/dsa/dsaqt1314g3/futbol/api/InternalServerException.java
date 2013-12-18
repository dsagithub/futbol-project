package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.FutbolError;


public class InternalServerException extends WebApplicationException {
	public InternalServerException(String message) {
	super(Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new FutbolError(Response.Status.INTERNAL_SERVER_ERROR
						.getStatusCode(), message))
				.type(MediaType.FUTBOL_API_ERROR).build());
	}

}
