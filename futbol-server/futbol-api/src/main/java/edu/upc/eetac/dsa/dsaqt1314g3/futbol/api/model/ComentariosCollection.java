package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

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
	

}
