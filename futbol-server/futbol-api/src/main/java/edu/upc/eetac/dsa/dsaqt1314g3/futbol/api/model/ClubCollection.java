package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class ClubCollection {
	
	
	private List<Club> Clubs;
	private List<Link> links = new ArrayList<Link>();
	
	public ClubCollection() {
		super();
		Clubs = new ArrayList<Club>();
	}

	public List<Club> getClubs() {
		return Clubs;
	}

	public void setClubs(List<Club> clubs) {
		Clubs = clubs;
	}
	public void addClub(Club Club) {
		Clubs.add(Club);
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