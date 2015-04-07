package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Entity;

/**
 * Vorlage für Roboter-Items
 *
 */
public abstract class RoboItem extends Entity implements RoboModificator
{

	private static final long serialVersionUID = 1L;

	//Wenn das Item zur Grundausrüstung gehört solle dies zu false gesetzt werden
	private boolean removeable = true;

	public boolean isRemoveable()
	{
		return removeable;
	}

	public void setRemoveable(boolean removeable)
	{
		this.removeable = removeable;
	}

	@Override
	public abstract RoboItem clone();
}
