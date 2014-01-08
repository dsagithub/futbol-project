package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CalendarioResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.CampeonatosResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ComentariosResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.UserResource;

public class ComentariosLinkBuilder {
	
	public final static Link buildURIComentarioId(UriInfo uriInfo, String rel,
			String idCampeonato, String idPartido, int idComentario) {
		URI uri = uriInfo.getBaseUriBuilder().path(ComentariosResource.class)
				.path(ComentariosResource.class, "getComentario")
				.build(idCampeonato, idPartido, idComentario);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Comentario " + idComentario);
		link.setType(MediaType.FUTBOL_API_COMENTARIO);
		return link;
	}
	
	public final static Link buildURICalendarioId(UriInfo uriInfo, String rel,
			String idCampeonato, String idPartido) {
		URI uri = uriInfo.getBaseUriBuilder().path(CalendarioResource.class)
				.path(CalendarioResource.class, "getCalendario")
				.build(idCampeonato, idPartido);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_CALENDARIO);
		return link;
	}
	
	public final static Link buildURIUsuarioId(UriInfo uriInfo, String rel,
			int idUser) {
		URI uri = uriInfo.getBaseUriBuilder().path(UserResource.class)
				.path(UserResource.class, "getUser")
				.build(idUser);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_USER);
		return link;
	}
	
	public final static Link buildURICampeonatoId(UriInfo uriInfo, String rel,
			String idCampeonato) {
		URI uri = uriInfo.getBaseUriBuilder().path(CampeonatosResource.class)
				.path(CampeonatosResource.class, "getCampeonatos")
				.build(idCampeonato);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setType(MediaType.FUTBOL_API_CAMPEONATOS);
		return link;
	}
	
	public static final Link buildURIComentarios(UriInfo uriInfo, String offset,
			String length, String idComentario, String rel, String idPartido, String idCampeonato) {
		URI uri = null;
		if (idComentario == null)
			uri = uriInfo.getBaseUriBuilder().path(ComentariosResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build(idCampeonato, idPartido);
		else
			uri = uriInfo.getBaseUriBuilder().path(ComentariosResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.queryParam("idComentario", idComentario).build(idCampeonato, idPartido);

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOL_API_COMENTARIOS_COLLECTION);
		return self;
	}

}
