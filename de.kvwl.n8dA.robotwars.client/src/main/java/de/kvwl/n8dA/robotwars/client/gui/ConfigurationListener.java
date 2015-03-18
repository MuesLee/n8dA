package de.kvwl.n8dA.robotwars.client.gui;

import java.util.EventListener;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public interface ConfigurationListener extends EventListener {

	public void configurationCompleted(Robot configuredRobot);

}
