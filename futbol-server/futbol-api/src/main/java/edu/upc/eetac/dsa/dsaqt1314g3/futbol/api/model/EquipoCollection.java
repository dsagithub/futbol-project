package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model.Equipo;



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
