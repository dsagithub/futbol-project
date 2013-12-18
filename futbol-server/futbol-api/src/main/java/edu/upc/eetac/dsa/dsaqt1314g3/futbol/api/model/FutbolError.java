package edu.upc.eetac.dsa.dsaqt1314g3.futbol.api.model;

public class FutbolError {
	private int status;
	private String message;

	public FutbolError() {
		super();
	}

	public FutbolError(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}

