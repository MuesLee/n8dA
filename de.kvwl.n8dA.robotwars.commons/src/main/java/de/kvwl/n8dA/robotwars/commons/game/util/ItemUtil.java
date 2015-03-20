package de.kvwl.n8dA.robotwars.commons.game.util;

import java.util.ArrayList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.items.EPBoostItem;
import de.kvwl.n8dA.robotwars.commons.game.items.EPRegItem;
import de.kvwl.n8dA.robotwars.commons.game.items.HPBoostItem;
import de.kvwl.n8dA.robotwars.commons.game.items.HPRegItem;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;

public class ItemUtil
{

	public static List<RoboItem> getAllRoboItems()
	{
		List<RoboItem> items = new ArrayList<>();
		items.add(new HPBoostItem());
		items.add(new HPRegItem());
		items.add(new EPBoostItem());
		items.add(new EPRegItem());
		return items;
	}

	public static RoboItem cloneItemById(Long itemId)
	{

		List<RoboItem> roboItems = getAllRoboItems();

		for (RoboItem item : roboItems)
		{

			if (item.getId() == itemId)
			{

				return clone(item);
			}
		}

		return null;
	}

	private static RoboItem clone(RoboItem item)
	{

		Class<? extends RoboItem> clone = item.getClass();

		try
		{
			RoboItem clonedItem = clone.newInstance();

			return clonedItem;
		}
		catch (InstantiationException | IllegalAccessException e)
		{
		}

		return null;
	}
}
