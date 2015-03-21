package de.kvwl.n8dA.robotwars.commons.game.actions;

public enum StatusEffectType {
	
	VULNERABLE_ROCK,VULNERABLE_SCISSOR,VULNERABLE_PAPER,RESISTANT_ROCK,RESISTANT_SCISSOR,RESISTANT_PAPER;
	
	public double getVulnerabilityFactor()
	{
		return 1.5;
	}
	public double getResistanceFactor()
	{
		return 0.5;
	}

}
