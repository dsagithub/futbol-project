package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.FutbolError;

public class ComentarioNotFoundException extends WebApplicationException {
	private final static String MESSAGE = "Comentario no encontrado";

	public ComentarioNotFoundException() {
		super(Response.status(Response.Status.NOT_FOUND)
				.entity(new FutbolError(Response.Status.NOT_FOUND
						.getStatusCode(), MESSAGE))
				.type(MediaType.FUTBOL_API_ERROR).build());
	}
}
