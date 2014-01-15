package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ClubResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.NoticiasResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

public class NoticiasLinkBuilder {
	
	public final static Link buildURINoticiaId(UriInfo uriInfo, String rel,
			String idClub, int idNoticia) {
		URI uri = uriInfo.getBaseUriBuilder().path(NoticiasResource.class)
				.path(NoticiasResource.class, "getNoticia")
				.build(idClub, idNoticia);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Noticia " + idNoticia);
		link.setType(MediaType.FUTBOL_API_NOTICIA);
		return link;
	}
	
	public final static Link buildURIClubId(UriInfo uriInfo, String rel,
			String idClub) {
		URI uri = uriInfo.getBaseUriBuilder().path(ClubResource.class)
				.path(ClubResource.class, "getClub")
				.build(idClub);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Club " + idClub);
		link.setType(MediaType.FUTBOL_API_CLUB);
		return link;
	}
	
	public static final Link buildURINoticias(UriInfo uriInfo, String offset,
			String length, String rel, String idClub) {
		URI uri = null;
		uri = uriInfo.getBaseUriBuilder().path(NoticiasResource.class)
				.queryParam("offset", offset).queryParam("length", length)
				.build(idClub);

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setTitle("Noticias");
		self.setType(MediaType.FUTBOL_API_NOTICIAS_COLLECTION);
		return self;
	}

}
