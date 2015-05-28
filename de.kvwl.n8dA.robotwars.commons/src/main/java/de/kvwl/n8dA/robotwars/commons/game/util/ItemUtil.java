package de.kvwl.n8dA.robotwars.commons.game.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.items.EPBoostItem;
import de.kvwl.n8dA.robotwars.commons.game.items.EPRegItem;
import de.kvwl.n8dA.robotwars.commons.game.items.HPBoostItem;
import de.kvwl.n8dA.robotwars.commons.game.items.HPRegItem;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;

public class ItemUtil
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

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

				return item.clone();
			}
		}

		return null;
	}

	public static String createToolTipTextForRobotActions(RobotAction robotAction)
	{

		RobotActionType robotActionType = robotAction.getRobotActionType();
		List<StatusEffect> statusEffects = robotAction.getStatusEffects();

		String iconName = robotActionType.getIconName();
		URL iconURL = RobotAction.class.getResource(IMAGE_PATH + iconName);
		String actionsStrengthValue ="";		
		String additionalText = "";
		
		
		if(robotAction instanceof Attack)
		{
			additionalText= "<p><hr>Diese Statuseffekte bekommt der <b>gegnerische Roboter.</b></p>";
			actionsStrengthValue = "Schaden: " + ((Attack) robotAction).getDamage();
		}
		else if(robotAction instanceof Defense)
		{
			additionalText= "<hr><p>Diese Statuseffekte bekommt <b>dein Roboter.</b></p>";
			actionsStrengthValue = "Verteidigungswert: " + ((Defense) robotAction).getBonusOnDefenseFactor()*100 
					+" <br><br><b>Starke Verteidigung gegen: " ;
			for (StatusEffect statusEffect : statusEffects)
			{
				String iconNameDef = statusEffect.getIconName();
				actionsStrengthValue += "<img src='" + RobotAction.class.getResource(IMAGE_PATH + iconNameDef) + "'>";
			}
			
			actionsStrengthValue += "</b><br><br>";
		}
		
		String text = "<html><p>Typ:&nbsp<img src=\"" +
		iconURL + "\"></p>" + "<p>Kosten: " + robotAction.getEnergyCosts() + "</p><p>"+actionsStrengthValue+"</p><p><b><u>Verursacht:</u></b></p>";
		
		for (StatusEffect statusEffect : statusEffects)
		{
			
			iconName = statusEffect.getIconName();
			text += "<p>" + statusEffect.getModifierText() + "&nbsp";
			
			text += "<img src='" + RobotAction.class.getResource(IMAGE_PATH + iconName) + "'>";
			text += "&nbsp Runden:&nbsp" + "<b>" + statusEffect.getStartDuration() + "</b>";
			text += "</p>";
		}
		
		text += additionalText;
		text += "</html>";

		return text;
	}
}
