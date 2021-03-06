package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;

public class ClientFrame extends JFrame implements ConfigurationListener
{

	private static final Logger LOG = LoggerFactory.getLogger(ClientFrame.class);

	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private RoboBattlePlayerClient battleClient;
	private long maxCreditPoints;

	private JPanel container;
	private RoboConfigurationPanel roboConfigurationPanel;

	private String playerName;

	public ClientFrame(RoboBattlePlayerClient battleClient, long maxCreditPoints, String playerName)
	{

		this.battleClient = battleClient;
		this.maxCreditPoints = maxCreditPoints;
		this.playerName = playerName;

		LOG.debug("Credit points available for configuration: {}", maxCreditPoints);

		createGui();
		openConfiguration();
		addWindowListener();
	}

	private void addWindowListener()
	{

		addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{

				System.exit(0);
			}
		});
	}

	private void closeAnimations()
	{
		if (roboConfigurationPanel != null)
		{

			roboConfigurationPanel.dispose();
			roboConfigurationPanel = null;
		}
	}

	private void openConfiguration()
	{

		closeAnimations();

		try
		{
			Attack[] attacks = battleClient.getAllPossibleAttacksFromServer().toArray(new Attack[0]);

			Robot[] robots = battleClient.getAllPossibleRobotsFromServer(playerName).toArray(new Robot[0]);

			Defense[] defends = battleClient.getAllPossibleDefendsFromServer().toArray(new Defense[0]);

			RoboItem[] items = battleClient.getAllPossibleItemsFromServer().toArray(new RoboItem[0]);

			long maxCredit = maxCreditPoints;

			LOG.info("Open configuration view with {} credit points", maxCredit);

			roboConfigurationPanel = new RoboConfigurationPanel(robots, attacks, defends, items, maxCredit);

			roboConfigurationPanel.addConfigurationListener(this);

			show(roboConfigurationPanel);
		}
		catch (IOException e)
		{

			JOptionPane.showMessageDialog(this, e.getMessage(), "Ein Fehler ist aufgetaucht.",
				JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void openBattle(Robot robot, String playerName)
	{
		closeAnimations();
		robot.setRobotOwner(playerName);
		BattlePanel battlePanel = new BattlePanel(battleClient, robot, playerName);
		show(battlePanel);
	}

	@Override
	public void configurationCompleted(Robot configuredRobot)
	{

		LOG.info("Configuration finished -> Open battle view");

		openBattle(configuredRobot, playerName);
	}

	private void createGui()
	{

		setIconImage(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
		setTitle("RoboBattle - Client");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		container = new JPanel();
		container.setLayout(new BorderLayout());
		add(container, BorderLayout.CENTER);
	}

	private void show(JPanel panel)
	{
		container.removeAll();
		container.add(panel);

		container.revalidate();
		container.repaint();

		pack();
	}
}
