package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.FutbolError;

public class BadRequestException extends WebApplicationException {
	public BadRequestException(String message) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity(new FutbolError(Response.Status.BAD_REQUEST
						.getStatusCode(), message))
				.type(MediaType.FUTBOL_API_ERROR).build());
	}
}
