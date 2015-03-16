package de.kvwl.n8dA.robotwars.commons.game.util;

public enum RobotPosition {
LEFT("Links"), RIGHT("Rechts");

private String description;

private RobotPosition(String description) {
	this.setDescription(description);
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}




}
