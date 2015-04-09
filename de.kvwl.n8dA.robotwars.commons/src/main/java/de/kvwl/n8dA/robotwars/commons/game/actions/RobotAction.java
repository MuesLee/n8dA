package de.kvwl.n8dA.robotwars.commons.game.actions;

import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.entities.Entity;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

//TODO Timo: Beschreibung der Actions anpassen. -> Reaktion auf Tooltipwunsch
public abstract class RobotAction extends Entity {

	private static final long serialVersionUID = 3093807349848858832L;
	
	public RobotAction(RobotActionType robotActionType) {
		this.robotActionType = robotActionType;
	}

	protected RobotActionType robotActionType;
	protected RobotActionPowerType	robotActionPowerType;
	
	protected Animation animation;
	protected int energyCosts;
	protected String description;
	
	
	
	protected List<StatusEffect> statusEffects;
	
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

	public List<StatusEffect> getStatusEffects() {
		
		return statusEffects;
	}

	public void setStatusEffects(List<StatusEffect> statusEffects) {
		this.statusEffects = statusEffects;
	}

	public RobotActionPowerType getRobotActionPowerType() {
		return robotActionPowerType;
	}

	public void setRobotActionPowerType(RobotActionPowerType robotActionPowerType) {
		this.robotActionPowerType = robotActionPowerType;
	}
	
	@Override
	public int compareTo(Entity o) {
		int configCostResult = super.compareTo(o);
		
		RobotAction other;
		if(o instanceof RobotAction)
		{
			other =  (RobotAction) o;
		}
		else {
			return configCostResult;
		}
		
		int result = 0;
		int thisIndex = this.getRobotActionType().getIndex();
		int otherIndex = other.getRobotActionType().getIndex();
		
		if(configCostResult == 0)
		{
			if(thisIndex == otherIndex)
				result = 0;
			else if(thisIndex > otherIndex)
			{
				result = 1;
			}
			else {
				result =-1;
			}
		}
		else {
			result = configCostResult;
		}
		
		return result;
	}
}
