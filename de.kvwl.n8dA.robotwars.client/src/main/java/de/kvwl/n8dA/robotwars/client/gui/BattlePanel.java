package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.kvwl.n8dA.robotwars.client.BattleClientListener;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

public class BattlePanel extends JPanel implements ActionListener, BattleClientListener
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/client/images/";
	private static final long serialVersionUID = 1L;

	private static final int SELECTION_TIME = 50;

	private RoboBattlePlayerClient battleClient;
	private Robot robot;

	private SimpleProgressBar life;
	private SimpleProgressBar energy;
	private JLabel lblLife;
	private JLabel lblEnergy;
	private JPanel pnlTimer;
	private Countdown countdown;
	private JPanel pnlActionSelection;

	public BattlePanel(RoboBattlePlayerClient battleClient, Robot robot)
	{

		this.battleClient = battleClient;
		this.robot = robot;

		createGui();
		setupConnection();
	}

	private void setupConnection()
	{

		try
		{
			battleClient.setClientListener(this);
			battleClient.registerClientWithRobotAtServer(robot);
			battleClient.sendPlayerIsReadyToBattleToServer();
		}
		catch (NoFreeSlotInBattleArenaException e)
		{

			JOptionPane.showMessageDialog(this,
				"Zu zeit ist kein Platz für dich in der Arena. \nBitte warte, bis du dran bist.", "Kein freier Platz",
				JOptionPane.ERROR_MESSAGE);

			System.exit(-1);
		}
	}

	private void createGui()
	{

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(createInfoSection(), BorderLayout.NORTH);

		pnlActionSelection = createActionSelection();
		add(pnlActionSelection, BorderLayout.CENTER);

		updateStats();
	}

	private JPanel createInfoSection()
	{
		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());

		info.add(createRoboStats(), BorderLayout.SOUTH);

		pnlTimer = createTimer();
		info.add(pnlTimer, BorderLayout.CENTER);

		return info;
	}

	private JPanel createRoboStats()
	{
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

		stats.add(Box.createVerticalStrut(30));

		// Lebensanzeige
		JPanel pnlLife = new JPanel();
		pnlLife.setLayout(new BoxLayout(pnlLife, BoxLayout.X_AXIS));
		stats.add(pnlLife);

		pnlLife.add(new JLabel(new ImageIcon(ImageUtils.getScaledInstance(
			InternalImage.loadFromPath(IMAGE_PATH, "life.png"), 20, 20, null))));

		life = new SimpleProgressBar();
		life.setMinimum(0);
		life.setMaximum(robot.getMaxHealthPoints());
		life.setValue(life.getMaximum());
		life.setBackground(Color.GRAY);
		life.setForeground(Color.RED);
		pnlLife.add(life);

		pnlLife.add(Box.createHorizontalStrut(5));

		lblLife = new JLabel("" + life.getValue());
		pnlLife.add(lblLife);

		// Energieanzeige
		JPanel pnlEnergy = new JPanel();
		pnlEnergy.setLayout(new BoxLayout(pnlEnergy, BoxLayout.X_AXIS));
		stats.add(pnlEnergy);

		pnlEnergy.add(new JLabel(new ImageIcon(ImageUtils.getScaledInstance(
			InternalImage.loadFromPath(IMAGE_PATH, "energy.png"), 20, 20, null))));

		energy = new SimpleProgressBar();
		energy.setMinimum(0);
		energy.setMaximum(robot.getMaxEnergyPoints());
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
		timer.setVisible(false);
		timer.setLayout(new BorderLayout());

		countdown = new Countdown();
		countdown.addActionListener(this);
		timer.add(countdown, BorderLayout.CENTER);

		JLabel lblTimerInfo = new JLabel("Wähle deine nächste Aktion");
		lblTimerInfo.setHorizontalAlignment(JLabel.CENTER);
		timer.add(lblTimerInfo, BorderLayout.SOUTH);

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
			def.addActionListener(this);

			if (i < defs.size())
			{

				Defense defense = defs.get(i);

				def.setRoboAction(defense);
				def.setText(String.format("%s - %.2f (%d E)", defense.getName(),
					(100 * defense.getBonusOnDefenseFactor()), defense.getEnergyCosts()));
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
			atk.addActionListener(this);

			if (i < atks.size())
			{

				Attack attack = atks.get(i);

				atk.setRoboAction(attack);
				atk.setText(String.format("%s - %d S (%d E)", attack.getName(), attack.getDamage(),
					attack.getEnergyCosts()));
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

	private void actionSlection(RobotAction roboAction)
	{

		if (roboAction == null)
		{
			System.out.println("Action selected -> <Leer>");
			return;
		}

		if (countdown.getTime() <= 0)
		{
			System.out.println("Countdown over -> no selection possible");
			return;
		}

		if (roboAction.getEnergyCosts() > robot.getEnergyPoints())
		{
			System.out.println("Nicht genug Energie");
			JOptionPane.showMessageDialog(this, "Deine Energie reicht nicht aus, um diese Fähigkeit einzusetzen.",
				"Nicht genug Energie", JOptionPane.WARNING_MESSAGE);
			return;
		}

		System.out.println("Action selected -> " + roboAction.getName());

		battleClient.sendRobotActionToServer(roboAction);

		countdown.stopCountdown();
		pnlTimer.setVisible(false);
	}

	private void startCountdown()
	{

		countdown.stopCountdown();
		pnlTimer.setVisible(true);
		countdown.setTime(SELECTION_TIME);
		countdown.startCountdown();

		repack();
	}

	private void repack()
	{

		Container parent = this.getParent();

		while (!(parent instanceof JFrame) || parent == null)
		{

			parent = parent.getParent();
		}

		if (parent != null)
		{

			((JFrame) parent).pack();
		}
	}

	private void updateRobot()
	{

		remove(pnlActionSelection);

		pnlActionSelection = createActionSelection();
		add(pnlActionSelection);

		updateStats();

		revalidate();
		repaint();
	}

	private void countdownOver()
	{

		System.out.println("Countdown over");
		pnlTimer.setVisible(false);

		List<Attack> attacks = robot.getPossibleAttacks();

		for (Attack atk : attacks)
		{

			if (atk.getEnergyCosts() <= robot.getEnergyPoints())
			{

				battleClient.sendRobotActionToServer(atk);
			}
		}
	}

	@Override
	public void startActionSelection()
	{

		robot = battleClient.getUpdatedRobot();
		updateRobot();
		startCountdown();
	}

	@Override
	public void gameOver(GameStateType result)
	{

		RobotPosition ownPosition = battleClient.getPositionOfOwnRobot();

		String msg = "";

		switch (result)
		{

			case DRAW:
				msg = "Keiner von Euch hat gewonnen ;(";
			break;

			case VICTORY_LEFT:

				if (ownPosition == RobotPosition.LEFT)
				{

					msg = "Du hast gewonnen :)";
				}
				else
				{

					msg = "Du hast verloren :(";
				}
			break;

			case VICTORY_RIGHT:

				if (ownPosition == RobotPosition.RIGHT)
				{

					msg = "Du hast gewonnen :)";
				}
				else
				{

					msg = "Du hast verloren :(";
				}
			break;

			default:
				return;
		}

		JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		System.exit(-1);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		Object source = e.getSource();

		if (source == countdown)
		{
			if (countdown.getTime() == 0)
			{

				countdownOver();
			}
		}
		else if (source instanceof ActionButton)
		{

			actionSlection(((ActionButton) source).getRoboAction());
		}
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
		r.setMaxHealthPoints(500);
		r.setHealthPoints(150);

		r.setMaxEnergyPoints(100);
		r.setEnergyPoints(80);

		Attack atk = new Attack(RobotActionType.PAPER, 10);
		atk.setName("Rakete");
		atk.setEnergyCosts(10);
		r.getPossibleAttacks().add(atk);

		BattlePanel comp = new BattlePanel(null, r);
		comp.startCountdown();
		disp.add(comp);

		disp.pack();
		disp.setVisible(true);
	}
}
