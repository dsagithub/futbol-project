package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CalendarioResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

public class CalendarioLinkBuilder {

	
	public final static Link buildURICalendarioId(UriInfo uriInfo, String rel,
			 String idPartido) {
		URI uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
				.path(CalendarioResource.class, "getCalendario")
				.build(idPartido);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Calendario " + idPartido);
		link.setType(MediaType.FUTBOL_API_CALENDARIO);
		return link;
	}
}
