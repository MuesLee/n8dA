package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import java.awt.Color;

public class EnergyPoints extends HealthPoints {

	public EnergyPoints() {

		super();

		setMaxColor(new Color(0, 0, 255));
		setMinColor(new Color(204, 102, 255));
	}

}
