package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.FutbolError;

public class ForbiddenException extends WebApplicationException {
	private final static String MESSAGE = "No tienes permisos para esta acción";

	public ForbiddenException(String message) {
		super(Response
				.status(Response.Status.FORBIDDEN)
				.entity(new FutbolError(Response.Status.FORBIDDEN.getStatusCode(), message))
				.type(MediaType.FUTBOL_API_ERROR).build());
	}

}
