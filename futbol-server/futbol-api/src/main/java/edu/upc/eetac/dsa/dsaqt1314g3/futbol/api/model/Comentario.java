package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class Comentario {
	private int idComentario;
	private String tiempo;
	private String media;
	private String texto;
	private String idPartido;
	private int idUsuario;
	private List<Link> links = new ArrayList<Link>();
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public int getIdComentario() {
		return idComentario;
	}
	public void setIdComentario(int idComentario) {
		this.idComentario = idComentario;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getIdPartido() {
		return idPartido;
	}
	public void setIdPartido(String idPartido) {
		this.idPartido = idPartido;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public void addLink(Link link) {
		links.add(link);
	}
	
	

}
