package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;


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
	

	
	public void addCampeonatos(Campeonatos campeonato) {
		Campeonatos.add(campeonato);
	}
	
	
	
	
	
}
