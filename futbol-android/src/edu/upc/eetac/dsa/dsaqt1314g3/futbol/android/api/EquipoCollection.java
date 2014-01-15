package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;


public class EquipoCollection {
	
	private List<Equipo> Equipos;
	private List<Link> links = new ArrayList<Link>();
	
	public EquipoCollection() {
		super();
		Equipos = new ArrayList<Equipo>();
	}

	public List<Equipo> getEquipos() {
		return Equipos;
	}
	public void setEquipos(List<Equipo> equipos) {
		Equipos = equipos;
	}
	public void addEquipo(Equipo Equipo) {
		Equipos.add(Equipo);
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
