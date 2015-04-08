package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;

public class ActionButton extends JButton
{

	private static final long serialVersionUID = 1L;

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private RobotAction roboAction;

	public RobotAction getRoboAction()
	{
		return roboAction;
	}

	public void setRoboAction(RobotAction roboAction)
	{
		this.roboAction = roboAction;
	}

	public void setIcon(RobotActionType robotActionType)
	{

		Image img;

		switch (robotActionType)
		{
			case PAPER:
				img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Paper.png");
			break;
			case ROCK:
				img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Rock.png");
			break;
			case SCISSOR:
				img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Scissor.png");
			break;
			default:
				return;
		}

		setIcon(new ImageIcon(img));
	}
}
