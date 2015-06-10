package de.kvwl.n8dA.robotwars.client.ai;

import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffectModificationType;

public class NotSoSmartBot {

	private Robot ownRobot;
	private Robot enemyRobot;
	
	public NotSoSmartBot(Robot ownRobot) {
		this.ownRobot = ownRobot;
	}
	

	public String getSmartBotText() {
		String text = "Hallo, ich bin NotSoSmart-Bot! Ich mache gerade meinem Namen alle Ehre und kann dir nicht weiterhelfen...";
		
		List<StatusEffect> enemyStatusEffects = enemyRobot.getStatusEffects();
		
		for (StatusEffect statusEffect : enemyStatusEffects) {
			if(statusEffect instanceof TypeEffect)
			{
				TypeEffect typeEffect = (TypeEffect) statusEffect;
				RobotActionType typeEffectActionType = typeEffect.getActionType();
				if(typeEffect.getModificationType()==TypeEffectModificationType.VULNERABILITY)
				{
					List<Attack> possibleAttacks = getOwnRobot().getPossibleAttacks();
					for (Attack attack : possibleAttacks) {
						if(attack.getRobotActionType().equals(typeEffectActionType))
						{	
							if(getOwnRobot().getEnergyPoints()>=attack.getEnergyCosts())
							{
								text = "Gib ihm ne Kelle!\nBenutze " + attack.getName();
							}
						}
					}
				}
			}
		}
		return text;
	}


	public Robot getOwnRobot() {
		return ownRobot;
	}


	public void setOwnRobot(Robot ownRobot) {
		this.ownRobot = ownRobot;
	}


	public Robot getEnemyRobot() {
		return enemyRobot;
	}


	public void setEnemyRobot(Robot enemyRobot) {
		this.enemyRobot = enemyRobot;
	}
	
}
