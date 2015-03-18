package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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

	private JLabel lblCredits;

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
		add(createShop(), BorderLayout.CENTER);
	}

	private JPanel createShop()
	{
		JPanel shop = new JPanel();
		shop.setLayout(new BorderLayout());

		JTabbedPane tbPane = new JTabbedPane();
		shop.add(tbPane, BorderLayout.CENTER);

		tbPane.addTab("Angriff", createAttackShop());
		tbPane.addTab("Verteidigung", createDefenseShop());
		tbPane.addTab("Ausrüstung", createItemShop());

		return shop;
	}

	private JPanel createItemShop()
	{
		JPanel items = new JPanel();

		return items;
	}

	private JPanel createDefenseShop()
	{

		JPanel defense = new JPanel();

		return defense;
	}

	private JPanel createAttackShop()
	{
		JPanel attack = new JPanel();

		return attack;
	}

	private JPanel createCreditBar()
	{
		JPanel creditBar = new JPanel();
		creditBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		lblCredits = new JLabel();
		updateCreditLabel();
		creditBar.add(lblCredits);

		return creditBar;
	}

	private void updateCreditLabel()
	{

		lblCredits.setText(String.format("Credits: %d", (maxCredits - usedCredits)));
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
