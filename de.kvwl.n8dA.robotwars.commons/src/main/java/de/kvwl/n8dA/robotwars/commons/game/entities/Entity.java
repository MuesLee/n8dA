package de.kvwl.n8dA.robotwars.commons.game.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable
{

	private static final long serialVersionUID = 1L;
	protected long id;
	protected String name; 
	protected int configurationPointCosts;
	
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getConfigurationPointCosts() {
		return configurationPointCosts;
	}

	public void setConfigurationPointCosts(int configurationPointCosts) {
		this.configurationPointCosts = configurationPointCosts;
	}
}
