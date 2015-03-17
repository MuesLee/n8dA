package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import de.kvwl.n8dA.robotwars.client.CreditClient;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;

//TODO Marvin: Client disp
public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private RoboBattlePlayerClient battleClient;
	private CreditClient creditClient;

	public ClientFrame(RoboBattlePlayerClient battleClient,
			CreditClient creditClient) {

		this.battleClient = battleClient;
		this.creditClient = creditClient;

		createGui();
		pack();
	}

	private void createGui() {

		setLayout(new BorderLayout());
	}
}
