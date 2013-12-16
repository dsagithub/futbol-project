package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class NoticiasCollection {
	private List<Noticia> Noticias;
	private List<Link> links = new ArrayList<Link>();
	
	public NoticiasCollection() {
		super();
		Noticias = new ArrayList<Noticia>();
	}

	public List<Noticia> getNoticias() {
		return Noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		Noticias = noticias;
	}
	
	public void addNoticia(Noticia Noticia) {
		Noticias.add(Noticia);
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addLink(Link link){
		links.add(link);
	}
	

}
