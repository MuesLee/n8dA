package de.kvwl.n8dA.infrastructure.commons.entity;

public class HighscoreEntry {
	
	String name;
	int points;
	
	public HighscoreEntry(String name, int points) {
		super();
		this.name = name;
		this.points = points;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	

}
