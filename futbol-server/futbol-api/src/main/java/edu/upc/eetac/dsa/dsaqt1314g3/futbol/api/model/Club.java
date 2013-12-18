package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class Club {

	private String idClub;
	private String nombre;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	private List<Link> links = new ArrayList<Link>();
	
	
	public String getIdClub() {
		return idClub;
	}
	public void setIdClub(String idClub) {
		this.idClub = idClub;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public void addLink(Link link) {
		links.add(link);
	}
	
	
}
