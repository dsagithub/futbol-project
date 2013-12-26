package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class CampeonatosCollection {

	private List<Campeonatos> Campeonatos;
	private List<Link> links = new ArrayList<Link>();
	
	public CampeonatosCollection() {
		super();
		Campeonatos = new ArrayList<Campeonatos>();
	}

	public List<Campeonatos> getCampeonatos() {
		return Campeonatos;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setCampeonatos(List<Campeonatos> campeonatos) {
		Campeonatos = campeonatos;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(Link link){
		links.add(link);
	}
	
	//public void addCampeonatos(Campeonatos campeonato) {
		//Campeonatos.add(Campeonatos);
	//}
	
	
	
	
	
}
