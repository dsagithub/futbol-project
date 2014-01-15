package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;

public class ComentariosCollection {
	private List<Comentario> Comentarios;
	private List<Link> links = new ArrayList<Link>();
	
	public ComentariosCollection() {
		super();
		Comentarios = new ArrayList<Comentario>();
	}
	
	public List<Comentario> getComentarios() {
		return Comentarios;
	}
	
	public void setComentarios(List<Comentario> comentarios) {
		Comentarios = comentarios;
	}
	
	public void addComentario(Comentario Comentario) {
		Comentarios.add(Comentario);
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
