package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

//TODO Marvin: BattlePanel
public class BattlePanel extends JPanel
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/client/images/";
	private static final long serialVersionUID = 1L;

	private RoboBattlePlayerClient battleClient;
	private Robot robot;
	private SimpleProgressBar life;
	private SimpleProgressBar energy;
	private JLabel lblLife;
	private JLabel lblEnergy;

	public BattlePanel(RoboBattlePlayerClient battleClient, Robot robot)
	{

		this.battleClient = battleClient;
		this.robot = robot;

		createGui();
	}

	private void createGui()
	{

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(createInfoSection(), BorderLayout.NORTH);
		add(createActionSelection(), BorderLayout.CENTER);

		updateStats();
	}

	private JPanel createInfoSection()
	{
		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());

		info.add(createRoboStats(), BorderLayout.SOUTH);
		info.add(createTimer(), BorderLayout.CENTER);

		return info;
	}

	private JPanel createRoboStats()
	{
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

		//Lebensanzeige
		JPanel pnlLife = new JPanel();
		pnlLife.setLayout(new BoxLayout(pnlLife, BoxLayout.X_AXIS));
		stats.add(pnlLife);

		pnlLife.add(new JLabel(new ImageIcon(ImageUtils.getScaledInstance(
			InternalImage.loadFromPath(IMAGE_PATH, "life.png"), 20, 20, null))));

		life = new SimpleProgressBar();
		life.setMinimum(0);
		life.setMaximum(robot.getHealthPoints());
		life.setValue(life.getMaximum());
		life.setBackground(Color.GRAY);
		life.setForeground(Color.RED);
		pnlLife.add(life);

		pnlLife.add(Box.createHorizontalStrut(5));

		lblLife = new JLabel("" + life.getValue());
		pnlLife.add(lblLife);

		//Energieanzeige
		JPanel pnlEnergy = new JPanel();
		pnlEnergy.setLayout(new BoxLayout(pnlEnergy, BoxLayout.X_AXIS));
		stats.add(pnlEnergy);

		pnlEnergy.add(new JLabel(new ImageIcon(ImageUtils.getScaledInstance(
			InternalImage.loadFromPath(IMAGE_PATH, "energy.png"), 20, 20, null))));

		energy = new SimpleProgressBar();
		energy.setMinimum(0);
		energy.setMaximum(robot.getEnergyPoints());
		energy.setValue(energy.getMaximum());
		energy.setBackground(Color.GRAY);
		energy.setForeground(Color.BLUE);
		pnlEnergy.add(energy);

		pnlEnergy.add(Box.createHorizontalStrut(5));

		lblEnergy = new JLabel("" + energy.getValue());
		pnlEnergy.add(lblEnergy);

		stats.add(Box.createVerticalStrut(30));

		return stats;
	}

	private JPanel createTimer()
	{
		JPanel timer = new JPanel();

		return timer;
	}

	private JPanel createActionSelection()
	{
		JPanel actions = new JPanel();
		actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));

		actions.add(createAttackSelection());
		actions.add(Box.createHorizontalStrut(10));
		actions.add(createDefendSelection());

		return actions;
	}

	private JPanel createDefendSelection()
	{
		JPanel defends = new JPanel();
		defends.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true), "Verteidigung"),
			BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		defends.setLayout(new GridLayout(2, 2, 5, 5));

		List<Defense> defs = robot.getPossibleDefends();
		for (int i = 0; i < 4; i++)
		{

			ActionButton def = new ActionButton();

			if (i < defs.size())
			{

				Defense defense = defs.get(i);

				def.setRoboAction(defense);
				def.setText(String.format("%s (%d E)", defense.getName(), defense.getEnergyCosts()));
			}
			else
			{

				def.setText("<Leer>");
				def.setRoboAction(null);
			}

			defends.add(def);
		}

		return defends;
	}

	private JPanel createAttackSelection()
	{
		JPanel attacks = new JPanel();
		attacks.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true), "Angriff"),
			BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		attacks.setLayout(new GridLayout(2, 2, 5, 5));

		List<Attack> atks = robot.getPossibleAttacks();
		for (int i = 0; i < 4; i++)
		{

			ActionButton atk = new ActionButton();

			if (i < atks.size())
			{

				Attack attack = atks.get(i);

				atk.setRoboAction(attack);
				atk.setText(String.format("%s (%d E)", attack.getName(), attack.getEnergyCosts()));
			}
			else
			{

				atk.setText("<Leer>");
				atk.setRoboAction(null);
			}

			attacks.add(atk);
		}

		return attacks;
	}

	private void updateStats()
	{

		life.setValue(robot.getHealthPoints());
		lblLife.setText("" + robot.getHealthPoints());

		energy.setValue(robot.getEnergyPoints());
		lblEnergy.setText("" + robot.getEnergyPoints());
	}

	// XXX Marvin: Testmain -> entfernen
	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JFrame disp = new JFrame();
		disp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		disp.setSize(800, 600);
		disp.setLocationRelativeTo(null);

		Robot r = new Robot();
		r.setHealthPoints(150);
		r.setEnergyPoints(80);

		Attack atk = new Attack(RobotActionType.PAPER, 10);
		atk.setName("Rakete");
		atk.setEnergyCosts(10);
		r.getPossibleAttacks().add(atk);

		BattlePanel comp = new BattlePanel(null, r);
		disp.add(comp);

		disp.pack();
		disp.setVisible(true);
	}
}
