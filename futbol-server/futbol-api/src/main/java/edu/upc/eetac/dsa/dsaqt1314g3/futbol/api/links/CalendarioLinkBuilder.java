package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CalendarioResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CampeonatosResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ClubResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ComentariosResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.EquipoResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.JugadoresResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.RetransResource;

public class CalendarioLinkBuilder {

	
	public final static Link buildURICalendarioId(UriInfo uriInfo, String rel,String idCampeonato,
			 String idPartido) {
		URI uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
				.path(CalendarioResource.class, "getCalendario")
				.build(idCampeonato,idPartido);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Calendario " + idPartido);
		link.setType(MediaType.FUTBOL_API_CALENDARIO);
		return link;
	}

	public static final Link buildURICalendarios(UriInfo uriInfo, String offset,
			String length, String pattern, String rel, String idCampeonato) {
		URI uri = null;
		if (pattern == null)
			uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build(idCampeonato);
		else
			uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.queryParam("pattern", pattern).build(idCampeonato);

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOL_API_CALENDARIO_COLLECTION);
		return self;
	}
	public final static Link buildURIRetransmision(UriInfo uriInfo, String rel,
			String offset, String length, String idCampeonato, String idPartido) {
		URI uri = null;
		if (offset == null && length == null)
			uri = uriInfo.getBaseUriBuilder().path(RetransResource.class)
					.build(idCampeonato, idPartido);
		else {
			uri = uriInfo.getBaseUriBuilder().path(RetransResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build(idCampeonato, idPartido);
		}
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_RETRA_COLLECTION);
		return link;
	}
	public final static Link buildURIComentarios(UriInfo uriInfo, String rel,
			String offset, String length, String idCampeonato, String idPartido) {
		URI uri = null;
		if (offset == null && length == null)
			uri = uriInfo.getBaseUriBuilder().path(ComentariosResource.class)
					.build(idCampeonato, idPartido);
		else {
			uri = uriInfo.getBaseUriBuilder().path(ComentariosResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build(idCampeonato, idPartido);
		}
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_COMENTARIOS_COLLECTION);
		return link;
	}
	
	public final static Link buildURICampeonatos(UriInfo uriInfo, String rel,
			 String idCampeonato) {
		URI uri = null;
		
			uri = uriInfo.getBaseUriBuilder().path(CampeonatosResource.class).path(CampeonatosResource.class,"getCampeonatos").build(idCampeonato);
		
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_COMENTARIOS_COLLECTION);
		return link;
	}
}
