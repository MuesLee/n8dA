package de.kvwl.n8dA.robotwars.commons.game.actions;

import de.kvwl.n8dA.robotwars.commons.game.entities.Entity;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

//TODO Timo: Beschreibung der Actions anpassen. -> Reaktion auf Tooltipwunsch
public abstract class RobotAction extends Entity {

	private static final long serialVersionUID = 3093807349848858832L;

	public RobotAction(RobotActionType robotActionType) {
		this.robotActionType = robotActionType;
		this.description = String.format("Typ: %s",
				robotActionType.getHumanReadableString());
	}

	protected RobotActionType robotActionType;
	protected Animation animation;
	protected int energyCosts;
	private String description;

	public boolean beats(RobotAction otherAction) {
		return this.robotActionType.beats(otherAction.getRobotActionType());
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public RobotActionType getRobotActionType() {
		return robotActionType;
	}

	public void setRobotActionType(RobotActionType robotActionType) {
		this.robotActionType = robotActionType;
	}

	@Override
	public String toString() {

		return name + " Typ: " + robotActionType;
	}

	public int getEnergyCosts() {
		return energyCosts;
	}

	public void setEnergyCosts(int energyCosts) {
		this.energyCosts = energyCosts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
