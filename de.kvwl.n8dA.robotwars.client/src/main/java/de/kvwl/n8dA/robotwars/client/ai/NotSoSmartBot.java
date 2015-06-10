package de.kvwl.n8dA.robotwars.client.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
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

	private Robot ownRobot;
	private Robot enemyRobot;

	private static final int rating_stat_vulnerability = 5;
	private static final int rating_stat_resistance = -5;
	private static final int rating_energy_loss = 1;
	private static final int rating_hp_loss = 1;
	private static final int rating_direct_damage = 2;
	private static final int rating_kill_enemy = 100;
	private static final int rating_avoid_death = 50;
	private static final int comment_massive_dmg = 30;

	private GameStateType gameState = GameStateType.GAME_HASNT_BEGUN;
	private boolean playerActionDone = false;
	private boolean isSad = false;

	private RobotAction advicedAction = null;

	public NotSoSmartBot(Robot ownRobot) {
		this.ownRobot = ownRobot;
	}

	public String getSmartBotText() {

		if (isSad() && playerActionDone) {
			return "Das hätte ich ja nicht gemacht...";
		}
		if (isSad) {
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

			text = actionName + " " + comment;
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
		List<RatedAction> ratedActions = new ArrayList<>();

		return ratedActions;
	}

	private List<? extends RatedAction> rateAttackActions() {
		List<RatedAction> ratedActions = new ArrayList<>();

		int healthPointsEnemy = enemyRobot.getHealthPoints();

		List<Attack> possibleAttacks = ownRobot.getPossibleAttacks();
		for (Attack attack : possibleAttacks) {
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

			List<StatusEffect> appliedStatusEffects = attack.getStatusEffects();
			for (StatusEffect statusEffect : appliedStatusEffects) {
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

			if (possibleDmg >= comment_massive_dmg) {
				comment += attack.getName().toUpperCase()
						+ "!!11 BABABAAAMM!!11";
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
		if (roboAction.equals(advicedAction)) {
			isSad = false;
		} else {
			isSad = true;
		}
		playerActionDone = true;
		advicedAction = null;
	}
}
