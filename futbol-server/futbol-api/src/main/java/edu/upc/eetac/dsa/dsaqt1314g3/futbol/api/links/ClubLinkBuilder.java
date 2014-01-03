package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ClubResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.EquipoResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

public class ClubLinkBuilder {
	
	
	public final static Link buildURIClubId(UriInfo uriInfo, String rel,
			String clubid) {
		URI uri = uriInfo.getBaseUriBuilder().path(ClubResource.class)
				.path(ClubResource.class, "getClub")
				.build(clubid);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Club " + clubid);
		link.setType(MediaType.FUTBOL_API_CLUB);
		return link;
	}
	
	
	
	public static final Link buildURIClubs(UriInfo uriInfo, String offset,
			String length, String pattern, String rel) {
		URI uri = null;
		if (pattern == null)
			uri = uriInfo.getBaseUriBuilder().path(ClubResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build();
		else
			uri = uriInfo.getBaseUriBuilder().path(ClubResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.queryParam("pattern", pattern).build();

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOL_API_CLUB_COLLECTION);
		return self;
	}
	//lista de equipos
	
	
	public static final Link buildURIEquipos2(UriInfo uriInfo, String offset,
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
