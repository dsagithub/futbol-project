package edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api;

import java.util.ArrayList;
import java.util.List;


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

	public void setRetrans(List<Retransmision> Retransmisiones) {
		retransmisiones = Retransmisiones;
	}

	public void addRetrans(Retransmision retrans) {
		retransmisiones.add(retrans);
	}
}
