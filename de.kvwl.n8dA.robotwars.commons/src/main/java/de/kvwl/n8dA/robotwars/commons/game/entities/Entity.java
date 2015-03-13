package de.kvwl.n8dA.robotwars.commons.game.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
