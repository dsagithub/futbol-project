package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ClubResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.EquipoResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.JugadoresResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

public class JugadoresLinkBuilder {

	public final static Link buildURIJugadorId(UriInfo uriInfo, String rel,
			String idequipo, String dni, String idclub) {
		URI uri = uriInfo.getBaseUriBuilder().path(JugadoresResource.class)
				.path(JugadoresResource.class, "getJugador")
				.build(idclub, idequipo, dni);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Jugador " + dni);
		link.setType(MediaType.FUTBOL_API_EQUIPO);
		return link;
	}

	public final static Link buildURIEquipoId(UriInfo uriInfo, String rel,
			String clubid, String idequipo) {
		URI uri = uriInfo.getBaseUriBuilder().path(EquipoResource.class)
				.path(EquipoResource.class, "getEquipo")
				.build(clubid, idequipo);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Equipo " + idequipo);
		link.setType(MediaType.FUTBOL_API_EQUIPO);
		return link;
	}

	public final static Link buildURIClubId(UriInfo uriInfo, String rel,
			String clubid) {
		URI uri = uriInfo.getBaseUriBuilder().path(ClubResource.class)
				.path(ClubResource.class, "getClub").build(clubid);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Club " + clubid);
		link.setType(MediaType.FUTBOL_API_CLUB);
		return link;
	}

	public static final Link buildURIJugadores(UriInfo uriInfo, String offset,
			String length, String rel, String idequipo, String idclub) {
		URI uri = null;
		uri = uriInfo.getBaseUriBuilder().path(JugadoresResource.class)
				.queryParam("offset", offset).queryParam("length", length)
				.build(idclub, idequipo);

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOL_API_EQUIPO_COLLECTION);
		return self;
	}

}
