package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CalendarioResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.EquipoResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.JugadoresResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

public class EquiposLinkBuilder {

	public final static Link buildURIEquipoId(UriInfo uriInfo, String rel,
			String clubid, String equipoid) {
		URI uri = uriInfo.getBaseUriBuilder().path(EquipoResource.class)
				.path(EquipoResource.class, "getEquipo")
				.build(clubid, equipoid);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Equipo " + equipoid);
		link.setType(MediaType.FUTBOL_API_EQUIPO);
		return link;
	}

	public final static Link buildURIClubId(UriInfo uriInfo, String rel,
			String clubid) {
		URI uri = uriInfo.getBaseUriBuilder().path(EquipoResource.class)
				.build(clubid);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Club " + clubid);
		link.setType(MediaType.FUTBOL_API_CLUB);
		return link;
	}

	public final static Link buildURICalendarioId(UriInfo uriInfo, String rel,
			String idcampeonato, String offset, String length, String pattern) {
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

	public final static Link buildURIJugadores(UriInfo uriInfo, String rel,
			String offset, String length, String idequipo, String idclub) {
		URI uri = null;
		if (offset == null && length == null)
			uri = uriInfo.getBaseUriBuilder().path(JugadoresResource.class)
					.build(idclub, idequipo);
		else {
			uri = uriInfo.getBaseUriBuilder().path(JugadoresResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build(idclub, idequipo);
		}
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_JUGADORES_COLLECTION);
		return link;
	}

	public static final Link buildURIEquipos(UriInfo uriInfo, String offset,
			String length, String pattern, String rel, String idclub) {
		URI uri = null;
		if (pattern == null)
			uri = uriInfo.getBaseUriBuilder().path(EquipoResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build(idclub);
		else
			uri = uriInfo.getBaseUriBuilder().path(EquipoResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.queryParam("pattern", pattern).build(idclub);

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOL_API_EQUIPO_COLLECTION);
		return self;
	}
}
