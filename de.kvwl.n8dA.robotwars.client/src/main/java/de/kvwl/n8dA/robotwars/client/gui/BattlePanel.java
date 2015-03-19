package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

//TODO Marvin: BattlePanel
public class BattlePanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private RoboBattlePlayerClient battleClient;
	private Robot robot;

	public BattlePanel(RoboBattlePlayerClient battleClient, Robot robot)
	{

		this.battleClient = battleClient;
		this.robot = robot;

		createGui();
	}

	private void createGui()
	{

		setLayout(new BorderLayout());
	}
}
