package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;


public class Calendario {
	
	private String idCampeonato;
	private String idPartido;
	private String idEquipoA;
	private String equipoA;
	private String idEquipoB;
	private String equipoB;
	private String jornada;
	private String fecha;
	private String hora;
	private List<Link> links = new ArrayList<Link>();
	public String getIdCampeonato() {
		return idCampeonato;
	}
	public void setIdCampeonato(String idCampeonato) {
		this.idCampeonato = idCampeonato;
	}
	public String getIdPartido() {
		return idPartido;
	}
	public void setIdPartido(String idPartido) {
		this.idPartido = idPartido;
	}
	public String getIdEquipoA() {
		return idEquipoA;
	}
	public void setIdEquipoA(String idEquipoA) {
		this.idEquipoA = idEquipoA;
	}
	public String getIdEquipoB() {
		return idEquipoB;
	}
	public void setIdEquipoB(String idEquipoB) {
		this.idEquipoB = idEquipoB;
	}
	public String getJornada() {
		return jornada;
	}
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
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
	public String getEquipoA() {
		return equipoA;
	}
	public void setEquipoA(String equipoA) {
		this.equipoA = equipoA;
	}
	public String getEquipoB() {
		return equipoB;
	}
	public void setEquipoB(String equipoB) {
		this.equipoB = equipoB;
	}

}
