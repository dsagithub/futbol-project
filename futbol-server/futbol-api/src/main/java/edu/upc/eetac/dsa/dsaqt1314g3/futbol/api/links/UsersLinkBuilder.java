package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.EquipoResource;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.UserResource;

public class UsersLinkBuilder {
	public final static Link buildURIUser(UriInfo uriInfo, String rel,
			String username) {
		URI uri = uriInfo.getBaseUriBuilder().path(UserResource.class)
				.path(UserResource.class, "getUser").build(username);
		Link link = new Link();
		link.setUri(uri.toString());
		link.setRel(rel);
		link.setTitle("Usuario " + username);
		link.setType(MediaType.FUTBOL_API_USER);
		return link;
	}

	public static final Link buildURIUserList(UriInfo uriInfo, String offset,
			String length, String pattern, String rel) {
		URI uri = null;
		if (pattern == null)
			uri = uriInfo.getBaseUriBuilder().path(UserResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.build();
		else
			uri = uriInfo.getBaseUriBuilder().path(UserResource.class)
					.queryParam("offset", offset).queryParam("length", length)
					.queryParam("pattern", pattern).build();

		Link self = new Link();
		self.setUri(uri.toString());
		self.setRel(rel);
		self.setType(MediaType.FUTBOl_API_USER_COLLECTION);
		return self;
	}
}
