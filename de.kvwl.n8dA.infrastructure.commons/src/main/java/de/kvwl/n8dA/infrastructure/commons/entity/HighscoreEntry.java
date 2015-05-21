package de.kvwl.n8dA.infrastructure.commons.entity;

public class HighscoreEntry implements Comparable<HighscoreEntry>{
	
	String name;
	int points;
	
	private boolean highlighted = false;
	
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
	public boolean isHighlighted() {
		return highlighted;
	}
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	@Override
	public int compareTo(HighscoreEntry o) {
		
		if(o.equals(this))
		{
			return 0;
		}
		else if(o.getPoints()>this.points)
		{
			return 1;
		}
		else {
			return -1;
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (highlighted ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + points;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HighscoreEntry other = (HighscoreEntry) obj;
		if (highlighted != other.highlighted)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (points != other.points)
			return false;
		return true;
	}
	
	
	

}
