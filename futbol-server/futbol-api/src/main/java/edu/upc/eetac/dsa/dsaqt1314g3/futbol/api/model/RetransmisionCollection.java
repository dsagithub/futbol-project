package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

import java.util.ArrayList;
import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.links.Link;

public class RetransmisionCollection {

	private List<Retransmision> retransmisiones;
	private List<Link> links = new ArrayList<Link>();

	public RetransmisionCollection() {
		super();
		retransmisiones = new ArrayList<Retransmision>();
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

	public List<Retransmision> getRetrans() {
		return retransmisiones;
	}

	public void setRetrans(List<Retransmision> retransmisiones) {
		retransmisiones = retransmisiones;
	}

	public void addRetrans(Retransmision retrans) {
		retransmisiones.add(retrans);
	}
}
