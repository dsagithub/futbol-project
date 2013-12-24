package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class Campeonatos {

	private int idcampeonatos;
	private String nombre;
	
	private List<Link> links = new ArrayList<Link>();


	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public int getIdcampeonatos() {
		return idcampeonatos;
	}
	public void setIdcampeonatos(int idcampeonatos) {
		this.idcampeonatos = idcampeonatos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
