package de.kvwl.n8dA.robotwars.actions;

public class Attack extends RobotAction {
	
	private static final long serialVersionUID = 1L;
	protected int damage;

	public Attack(RobotActionType type, int damage) {
		super(type);
		this.damage = damage;
		
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	

}
