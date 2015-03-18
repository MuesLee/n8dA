package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;

public class ClientFrame extends JFrame implements ConfigurationListener {

	private static final long serialVersionUID = 1L;

	private RoboBattlePlayerClient battleClient;
	private long maxCreditPoints;

	private JPanel container;
	private RoboConfigurationPanel roboConfigurationPanel;

	public ClientFrame(RoboBattlePlayerClient battleClient, long maxCreditPoints) {

		this.battleClient = battleClient;
		this.maxCreditPoints = maxCreditPoints;

		createGui();
		createShutdownHook();

		openConfiguration();
	}

	private void openConfiguration() {

		if (roboConfigurationPanel != null) {

			roboConfigurationPanel.dispose();
			roboConfigurationPanel = null;
		}

		try {

			Robot[] robots = battleClient.getAllPossibleRobotsFromServer()
					.toArray(new Robot[0]);

			Attack[] attacks = battleClient.getAllPossibleAttacksFromServer()
					.toArray(new Attack[0]);

			Defense[] defends = battleClient.getAllPossibleDefendsFromServer()
					.toArray(new Defense[0]);

			RoboItem[] items = battleClient.getAllPossibleItemsFromServer()
					.toArray(new RoboItem[0]);

			long maxCredit = maxCreditPoints;

			roboConfigurationPanel = new RoboConfigurationPanel(robots,
					attacks, defends, items, maxCredit);

			roboConfigurationPanel.addConfigurationListener(this);

			show(roboConfigurationPanel);
		} catch (IOException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Ein Fehler ist aufgetaucht.", JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void configurationCompleted(Robot configuredRobot) {

		// TODO Marvin: configurationCompleted
	}

	private void createGui() {

		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		container = new JPanel();
		container.setLayout(new BorderLayout());
	}

	private void show(JPanel panel) {

		container.removeAll();
		container.add(panel);

		container.revalidate();
		container.repaint();

		pack();
	}

	private void createShutdownHook() {

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {

				battleClient.disconnectFromServer();
			}
		}));
	}
}
