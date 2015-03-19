package de.kvwl.n8dA.robotwars.client.gui;

import javax.swing.JButton;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;

public class ActionButton extends JButton
{

	private static final long serialVersionUID = 1L;

	private RobotAction roboAction;

	public RobotAction getRoboAction()
	{
		return roboAction;
	}

	public void setRoboAction(RobotAction roboAction)
	{
		this.roboAction = roboAction;
	}
}
