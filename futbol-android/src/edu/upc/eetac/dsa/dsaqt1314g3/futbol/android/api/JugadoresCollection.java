package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;


public class JugadoresCollection {
	
	private List<Jugadores> Jugadores;
	private List<Link> links = new ArrayList<Link>();
	
	public void addJugadores(Jugadores Jugador) {
		Jugadores.add(Jugador);
	}	
	public List<Jugadores> getJugadores() {
		return Jugadores;
	}
	public void setJugadores(List<Jugadores> jugadores) {
		Jugadores = jugadores;
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
	
	public JugadoresCollection() {
		super();
		Jugadores = new ArrayList<Jugadores>();
	}

	


}
