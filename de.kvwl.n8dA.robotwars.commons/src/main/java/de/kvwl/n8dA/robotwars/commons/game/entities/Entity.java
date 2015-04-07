package de.kvwl.n8dA.robotwars.commons.game.entities;

import java.io.Serializable;

/**
 * Verteilung der IDs: <br>
 * <ul>
 * <li>Negative long Werte für Benutzerdefinierte objects</li>
 * <li>Positive long Werte für die vordefinierten objects</li>
 * </ul>
 *
 */
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getConfigurationPointCosts()
	{
		return configurationPointCosts;
	}

	public void setConfigurationPointCosts(int configurationPointCosts)
	{
		this.configurationPointCosts = configurationPointCosts;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + configurationPointCosts;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (configurationPointCosts != other.configurationPointCosts)
			return false;
		if (id != other.id)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

}
