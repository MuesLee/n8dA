package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;

public class ActionButton extends JButton {

	private static final long serialVersionUID = 1L;

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private RobotAction roboAction;

	public ActionButton() {
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	public RobotAction getRoboAction() {
		return roboAction;
	}

	public void setRoboAction(RobotAction roboAction) {
		this.roboAction = roboAction;
	}

	public void setIcon(RobotActionType robotActionType) {

		Image img;

		switch (robotActionType) {
		case WATER:
			img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Paper.png");
			break;
		case FIRE:
			img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Rock.png");
			break;
		case LIGHTNING:
			img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Scissor.png");
			break;
		default:
			return;
		}

		setIcon(new ImageIcon(img));
	}
}
