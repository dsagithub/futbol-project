package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.ComentariosResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;

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
