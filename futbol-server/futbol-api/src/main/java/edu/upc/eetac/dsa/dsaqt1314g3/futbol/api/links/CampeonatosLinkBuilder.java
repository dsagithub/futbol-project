package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CalendarioResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CampeonatosResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

public class CampeonatosLinkBuilder {

	public final static Link buildURICalendarioId(UriInfo uriInfo, String rel,
			int idcampeonato, String offset, String length, String pattern) {
		URI uri = null;
		if (offset == null && length == null)
			uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
					.build(idcampeonato);
		else {
			if (pattern == null)
				uri = uriInfo.getBaseUriBuilder()
						.path(CalendarioResource.class)
						.queryParam("offset", offset)
						.queryParam("length", length).build(idcampeonato);
			else
				uri = uriInfo.getBaseUriBuilder()
						.path(CalendarioResource.class)
						.queryParam("offset", offset)
						.queryParam("length", length)
						.queryParam("pattern", pattern).build(idcampeonato);
		}
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Campeonato " + idcampeonato);
		link.setType(MediaType.FUTBOL_API_CAMPEONATOS_COLLECTION);
		return link;
	}

	
	public final static Link buildURICampeonatoId(UriInfo uriInfo, String rel,
			int campeonatoid) {
		URI uri = uriInfo.getBaseUriBuilder().path(CampeonatosResource.class)
				.path(CampeonatosResource.class, "getCampeonatos")
				.build(campeonatoid);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Campeonato " + campeonatoid);
		link.setType(MediaType.FUTBOL_API_CAMPEONATOS);
		return link;
	}
	
	public static final Link buildURICampeonatos(UriInfo uriInfo, String offset,
			String length, String pattern, String rel) {
		URI uri = null;
		if (pattern == null)
			uri = uriInfo.getBaseUriBuilder().path(CampeonatosResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build();
		else
			uri = uriInfo.getBaseUriBuilder().path(CampeonatosResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.queryParam("pattern", pattern).build();

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOL_API_CAMPEONATOS_COLLECTION);
		return self;
	}

	
}
