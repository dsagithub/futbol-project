package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CalendarioResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.EquipoResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.RetransResource;

public class RetransLinkBuilder {
	public final static Link buildURIPartidoId(UriInfo uriInfo, String rel,
			String campeonatoid, String idpartido) {

		URI uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
				.path(CalendarioResource.class, "getCalendario")
				.build(campeonatoid, idpartido);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Partido " + idpartido);
		link.setType(MediaType.FUTBOL_API_CALENDARIO);
		return link;
	}

	public final static Link buildURIRetraID(UriInfo uriInfo, String rel,
			String campeonatoid, String idpartido, String idretra) {
		URI uri = uriInfo.getBaseUriBuilder().path(RetransResource.class)
				.path(RetransResource.class, "getRetra")
				.build(campeonatoid, idpartido, idretra);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Retransmision " + idretra);
		link.setType(MediaType.FUTBOL_API_RETRA);
		return link;
	}

	public final static Link buildURIRetrans(UriInfo uriInfo, String rel,
			String idcampeonato, String idpartido, String offset,
			String length, String pattern) {
		URI uri = null;
		if (offset == null && length == null)
			uri = uriInfo.getBaseUriBuilder().path(RetransResource.class)
					.build(idcampeonato, idpartido);
		else {
			if (pattern == null)
				uri = uriInfo.getBaseUriBuilder().path(RetransResource.class)
						.queryParam("offset", offset)
						.queryParam("length", length)
						.build(idcampeonato, idpartido);
			else
				uri = uriInfo.getBaseUriBuilder().path(RetransResource.class)
						.queryParam("offset", offset)
						.queryParam("length", length)
						.queryParam("pattern", pattern)
						.build(idcampeonato, idpartido);
		}
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Retransmisiones del partido " + idpartido);
		link.setType(MediaType.FUTBOL_API_RETRA_COLLECTION);
		return link;
	}
}
