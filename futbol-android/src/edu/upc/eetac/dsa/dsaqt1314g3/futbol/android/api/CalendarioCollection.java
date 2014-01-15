package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;


public class CalendarioCollection {

	
	private List<Calendario> Calendarios;
	private List<Link> links = new ArrayList<Link>();
	
	public CalendarioCollection() {
		super();
		Calendarios = new ArrayList<Calendario>();
	}

	public List<Calendario> getCalendarios() {
		return Calendarios;
	}

	public void setCalendarios(List<Calendario> calendarios) {
		Calendarios = calendarios;
	}
	public void addCalendario(Calendario Calendario) {
		Calendarios.add(Calendario);
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
