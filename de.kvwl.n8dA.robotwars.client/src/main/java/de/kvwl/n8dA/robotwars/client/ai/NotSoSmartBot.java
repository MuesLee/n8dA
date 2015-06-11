package de.kvwl.n8dA.robotwars.client.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.EnergyConsumingEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.EnergyLossEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.HealthConsumingEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffectModificationType;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;

public class NotSoSmartBot {

	private static final double neutral_defense_factor = 0.75;
	private Robot ownRobot;
	private Robot enemyRobot;

	private static final double rating_stat_vulnerability = 5;
	private static final double rating_stat_resistance = -5;
	private static final double rating_energy_loss = 1;
	private static final double rating_hp_loss = 1;
	private static final double rating_direct_damage = 2;
	private static final double rating_avoided_direct_damage = rating_direct_damage*0.66;
	private static final double rating_kill_enemy = 200;
	private static final double rating_avoid_death = 100;
	private static final double comment_massive_dmg = 30;

	private GameStateType gameState = GameStateType.GAME_HASNT_BEGUN;
	private boolean playerActionDone = false;
	private boolean isSad = false;

	private RobotAction advicedAction = null;

	public NotSoSmartBot(Robot ownRobot) {
		this.ownRobot = ownRobot;
	}

	public String getSmartBotText() {

		if (isSad() && playerActionDone && advicedAction == null) {
			return "Das hätte ich ja nicht gemacht...";
		}
		if (isSad && !playerActionDone && advicedAction == null) {
			return "Du hörst ja eh nicht auf mich...";
		}
		if (playerActionDone) {
			return "Daumen sind gedrückt!";
		}

		String text = "Hallo, ich bin "
				+ toString()
				+ " und kann dir gerade nicht weiterhelfen...klick mich später nochmal an!";

		switch (getGameState()) {
		case BATTLE_IS_ACTIVE:
			text = "Woaaahh! Epische Animationen!";
			break;
		case DRAW:
			break;
		case GAME_HASNT_BEGUN:
			break;
		case GAME_HAS_BEGUN:
			text = "HF! GL!";
			break;
		case GAME_IS_ENDING:
			break;
		case GAME_OVER:
			text = "GG! WP!";
			break;
		case SERVER_BUSY:
			break;
		case VICTORY_LEFT:
			break;
		case VICTORY_RIGHT:
			break;
		case WAITING_FOR_PLAYER_INPUT:
			text = getAdviceText(text);
			break;
		default:
			break;
		}

		return text;
	}

	private String getAdviceText(String text) {
		List<RatedAction> ratedRobotActions = getRatedRobotActions();
		if (ratedRobotActions != null && !ratedRobotActions.isEmpty()) {
			RatedAction ratedAction = ratedRobotActions.get(ratedRobotActions
					.size() - 1);

			RobotAction action = ratedAction.getAction();
			advicedAction = action;
			String actionName = action.getName();
			String comment = ratedAction.getComment();

			text = actionName + comment;
		}
		return text;
	}

	private List<RatedAction> getRatedRobotActions() {
		if (enemyRobot == null || ownRobot == null) {
			return Collections.emptyList();
		}

		List<RatedAction> ratedActions = new ArrayList<>();

		ratedActions.addAll(rateAttackActions());
		ratedActions.addAll(rateDefenseActions());

		Collections.sort(ratedActions);

		return ratedActions;
	}

	private List<? extends RatedAction> rateDefenseActions() {
		
		//TODO Timo: implement  Defendrating
		List<RatedAction> ratedActions = new ArrayList<>();

		int ownHealthPoints = ownRobot.getHealthPoints();

		List<Defense> possibleDefends = ownRobot.getPossibleDefends();
		for (Defense defense : possibleDefends) {
			int rating =0;
			String comment = "";
			int ownEnergyPoints = ownRobot.getEnergyPoints();
			if (ownEnergyPoints < defense.getEnergyCosts()) {
				continue;
			}
			double bonusOnDefenseFactor = defense.getBonusOnDefenseFactor();
			double defenseFactorCombined = bonusOnDefenseFactor + neutral_defense_factor;
			RobotActionType defenseType = defense.getRobotActionType();
			RobotActionType counteredType = defenseType.getDominatedRobotActionType();
			
			int maxDmgDefenseType = getEnemyMaxDamageForRobotActionType(defenseType);
			int maxDmgCounteredType = getEnemyMaxDamageForRobotActionType(counteredType);
			
			
			List<StatusEffect> statusEffects = ownRobot.getStatusEffects();
			for (StatusEffect statusEffect : statusEffects) {
				double damageModDefenseType = statusEffect.getDamageModificatorForRoboActionType(defenseType);
				double damageModCounteredType = statusEffect.getDamageModificatorForRoboActionType(counteredType);
				
				maxDmgCounteredType*=damageModCounteredType;
				maxDmgDefenseType*=damageModDefenseType;
			}
			
			comment="! DEFENSE!";
			if(maxDmgCounteredType>=ownHealthPoints || maxDmgDefenseType >=ownHealthPoints)
			{
				rating += rating_avoid_death;
				comment =" sonst gehste drauf!!!11 OMFG!!11";
			}
			
			rating+=getRatingForAppliedStatusEffects(defense.getStatusEffects(), false);
			rating += (maxDmgCounteredType*rating_avoided_direct_damage)+(maxDmgCounteredType*bonusOnDefenseFactor*rating_direct_damage);
			rating += (maxDmgDefenseType*rating_avoided_direct_damage)*(1-defenseFactorCombined);
			
		RatedAction ratedAction = new RatedAction(rating, defense);
		ratedAction.setComment(comment);
		ratedActions.add(ratedAction);
		}

		return ratedActions;
	}

	private List<? extends RatedAction> rateAttackActions() {
		List<RatedAction> ratedActions = new ArrayList<>();

		int healthPointsEnemy = enemyRobot.getHealthPoints();

		List<Attack> possibleAttacks = ownRobot.getPossibleAttacks();
		for (Attack attack : possibleAttacks) {

			int ownEnergyPoints = ownRobot.getEnergyPoints();
			if (ownEnergyPoints < attack.getEnergyCosts()) {
				continue;
			}

			int rating = 0;
			String comment = "";
			int possibleDmg = attack.getDamage();
			RobotActionType attackActionType = attack.getRobotActionType();
			List<StatusEffect> enemyStatusEffects = enemyRobot
					.getStatusEffects();

			for (StatusEffect statusEffect : enemyStatusEffects) {
				if (statusEffect instanceof TypeEffect) {
					TypeEffect typeEffect = (TypeEffect) statusEffect;
					if (typeEffect.getActionType().equals(attackActionType)) {
						possibleDmg *= typeEffect
								.getDamageModificatorForRoboActionType(attackActionType);
					}
				}
			}

			rating += getRatingForAppliedStatusEffects(attack.getStatusEffects(), true);

			if (possibleDmg >= comment_massive_dmg) {
				comment += "! "+attack.getName().toUpperCase()
						+ "!!11 BABABAAAMM!!11";
			} else {
				comment += " wäre solide!";
			}
			if (healthPointsEnemy <= possibleDmg) {
				rating += rating_kill_enemy;
				comment += "...GG.";
			}

			rating = rating += (possibleDmg * rating_direct_damage);
			RatedAction ratedAction = new RatedAction(rating, attack);
			ratedAction.setComment(comment);
			ratedActions.add(ratedAction);
		}
		return ratedActions;
	}

	private int getRatingForAppliedStatusEffects(
			List<StatusEffect> statusEffects, boolean isAttack) {
		
		double rating_stat_vulnerability = NotSoSmartBot.rating_stat_vulnerability;
		double rating_stat_resistance = NotSoSmartBot.rating_stat_resistance;
		if(!isAttack)
		{
			rating_stat_resistance*=-1;
			rating_stat_vulnerability*=-1;
		}
		
		int rating = 0;
		for (StatusEffect statusEffect : statusEffects) {
			if (statusEffect instanceof TypeEffect) {
				TypeEffect typeEffect = (TypeEffect) statusEffect;
				if (typeEffect.getModificationType() == TypeEffectModificationType.VULNERABILITY) {
					rating += rating_stat_vulnerability;
				} else {
					rating -= rating_stat_resistance;
				}
			} else if (statusEffect instanceof EnergyConsumingEffect) {
				EnergyConsumingEffect energyConsumingEffect = (EnergyConsumingEffect) statusEffect;
				int energyLost = EnergyConsumingEffect.getEnergyLoss()
						* energyConsumingEffect.getStartDuration();
				rating += (energyLost * rating_energy_loss);
			} else if (statusEffect instanceof EnergyLossEffect) {
				rating += (EnergyLossEffect.getEnergyLoss() * rating_energy_loss);
			} else if (statusEffect instanceof HealthConsumingEffect) {
				HealthConsumingEffect healthConsumingEffect = (HealthConsumingEffect) statusEffect;
				int hpLost = HealthConsumingEffect.getHpLoss()
						* healthConsumingEffect.getStartDuration();
				rating += (hpLost * rating_hp_loss);
			}
		}
		
		return rating;
	}

	private int getEnemyMaxDamageForRobotActionType(RobotActionType actionType) {
		int maxDmg = 0;
		int energyPoints = enemyRobot.getEnergyPoints();
		List<Attack> possibleAttacks = enemyRobot.getPossibleAttacks();
		for (Attack attack : possibleAttacks) {
			RobotActionType attackActionType = attack.getRobotActionType();
			if (attackActionType != actionType
					|| energyPoints < attack.getEnergyCosts()) {
				continue;
			}
			maxDmg = Math.max(maxDmg, attack.getDamage());
		}

		return maxDmg;
	}

	@SuppressWarnings("unused")
	private int getEnemyMaxDamage() {
		int maxDmg = 0;
		int energyPoints = enemyRobot.getEnergyPoints();
		List<Attack> possibleAttacks = enemyRobot.getPossibleAttacks();
		for (Attack attack : possibleAttacks) {
			if (energyPoints < attack.getEnergyCosts()) {
				continue;
			}
			maxDmg = Math.max(maxDmg, attack.getDamage());
		}

		return maxDmg;
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

	@Override
	public String toString() {
		return "NotSoSmart-Bot";
	}

	public GameStateType getGameState() {
		return gameState;
	}

	public void setGameState(GameStateType gameState) {
		this.gameState = gameState;
	}

	public boolean isPlayerActionDone() {
		return playerActionDone;
	}

	public void setPlayerActionDone(boolean playerActionDone) {
		this.playerActionDone = playerActionDone;
	}

	public boolean isSad() {
		return isSad;
	}

	public void setSad(boolean isSad) {
		this.isSad = isSad;
	}

	public void checkIfPlayerActionWasAdviced(RobotAction roboAction) {
		if (advicedAction == null) {
			isSad = false;
		} else {
			if (roboAction.equals(advicedAction)) {
				isSad = false;
			} else {
				isSad = true;
			}
		}
		playerActionDone = true;
		advicedAction = null;
	}
}
