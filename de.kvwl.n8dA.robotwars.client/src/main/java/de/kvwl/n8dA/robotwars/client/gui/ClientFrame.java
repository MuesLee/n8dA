package de.kvwl.n8dA.robotwars.client.gui;

import javax.swing.JFrame;

import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;

//TODO Client disp
public class ClientFrame extends JFrame
{

	private static final long serialVersionUID = 1L;

	private RoboBattlePlayerClient client;

	public ClientFrame(RoboBattlePlayerClient client)
	{

		this.client = client;
	}
}
