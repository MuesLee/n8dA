package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;

public class ConfigShop extends JDialog
{

	private static final long serialVersionUID = 1L;

	private Robot config;

	private long maxCredits;
	private long usedCredits;

	private ConfigShop(Robot startConfig, long maxCredits)
	{

		this.config = startConfig;
		this.maxCredits = maxCredits;
		this.usedCredits = calculateUsedCredits(config);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		createGui();
		pack();
	}

	private long calculateUsedCredits(Robot config)
	{

		long costs = 0;

		costs += config.getConfigurationPointCosts();

		List<RoboItem> equippedItems = config.getEquippedItems();
		for (RoboItem item : equippedItems)
		{

			costs += item.getConfigurationPointCosts();
		}

		List<Attack> possibleAttacks = config.getPossibleAttacks();
		for (Attack atks : possibleAttacks)
		{

			costs += atks.getConfigurationPointCosts();
		}

		List<Defense> possibleDefends = config.getPossibleDefends();
		for (Defense defs : possibleDefends)
		{

			costs += defs.getConfigurationPointCosts();
		}

		return costs;
	}

	private void createGui()
	{

		setLayout(new BorderLayout());
		setTitle("Shop");

		add(createCreditBar(), BorderLayout.NORTH);
	}

	private JPanel createCreditBar()
	{
		JPanel creditBar = new JPanel();

		return creditBar;
	}

	public Robot getConfiguration()
	{

		return config;
	}

	public static Robot getConfiguration(Robot startConfig, long maxCredits)
	{

		ConfigShop shop = new ConfigShop(startConfig, maxCredits);
		shop.setLocationRelativeTo(null);
		shop.setVisible(true);

		return shop.getConfiguration();
	}

	public static void main(String[] args)
	{

		Robot startConfig = new Robot();

		getConfiguration(startConfig, 500);
	}
}
