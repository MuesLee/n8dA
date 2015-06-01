package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.client.BattleClientListener;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.ServerIsNotReadyForYouException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.ItemUtil;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

public class BattlePanel extends JPanel implements ActionListener,
		BattleClientListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(BattlePanel.class);

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";
	private static final long serialVersionUID = 1L;

	/**
	 * Zeit für die Auswahl einer Aktion. Wert ist in Sekunden.
	 */
	private static final int SELECTION_TIME = 50;

	private RoboBattlePlayerClient battleClient;
	private Robot robot;

	private String playerName;

	private SimpleProgressBar life;
	private SimpleProgressBar energy;
	private JLabel lblLife;
	private JLabel lblEnergy;
	private JPanel pnlTimer;
	private Countdown countdown;
	private JPanel pnlActionSelection;
	private JLabel lblInfo;

	private StatusEffectPanel ownStatusEffectPanel;
	private StatusEffectPanel enemyStatusEffectPanel;

	private JPanel sidePanel;

	private JPanel statusPanel;

	private JPanel actions;

	public BattlePanel(RoboBattlePlayerClient battleClient, Robot robot,
			String playerName) {

		this.battleClient = battleClient;
		this.robot = robot;
		this.playerName = playerName;

		createGui();
		setupConnection();
	}

	private void setupConnection() {

		try {
			battleClient.setClientListener(this);
			battleClient.registerClientWithRobotAtServer(robot, playerName);

			// Roboter mit angewendeten Modifikationen abfragen
			this.robot = battleClient.getUpdatedRobot();
			updateRobot();

			battleClient.sendPlayerIsReadyToBattleToServer();
		} catch (NoFreeSlotInBattleArenaException e) {

			int showConfirmDialog = JOptionPane
					.showConfirmDialog(
							this,
							"Zu zeit ist kein Platz für dich in der Arena. \nErneut versuchen?.",
							"Kein freier Platz", JOptionPane.OK_CANCEL_OPTION);

			if (showConfirmDialog == JOptionPane.OK_OPTION) {
				setupConnection();
			} else {
				System.exit(-1);
			}
		} catch (ServerIsNotReadyForYouException e) {
			int showConfirmDialog = JOptionPane.showConfirmDialog(this,
					"Bitte versuche es gleich nochmal. \nErneut versuchen?",
					"Der Server ist gerade beschäftigt..",
					JOptionPane.OK_CANCEL_OPTION);

			if (showConfirmDialog == JOptionPane.OK_OPTION) {
				setupConnection();
			} else {
				System.exit(-1);
			}
		}
	}

	private void createGui() {

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(createInfoSection(), BorderLayout.NORTH);

		pnlActionSelection = createActionSelection();
		add(pnlActionSelection, BorderLayout.CENTER);

		statusPanel = createStatusPanel();
		add(statusPanel, BorderLayout.SOUTH);

		sidePanel = createSidePanel();
		add(sidePanel, BorderLayout.EAST);

		updateStats(false);
	}

	private JPanel createStatusPanel() {

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

		ownStatusEffectPanel = createStatusEffectPanel(
				robot.getStatusEffects(), "Eigene Statuseffekte");
		statusPanel.add(ownStatusEffectPanel, BorderLayout.SOUTH);

		Robot updatedRobotOfEnemy = battleClient.getUpdatedRobotOfEnemy();
		List<StatusEffect> enemyStatusEffects;
		if (updatedRobotOfEnemy != null) {
			enemyStatusEffects = updatedRobotOfEnemy.getStatusEffects();
		} else {
			enemyStatusEffects = Collections.<StatusEffect> emptyList();
		}
		enemyStatusEffectPanel = createStatusEffectPanel(enemyStatusEffects,
				"Gegnerische Statuseffekte");
		statusPanel.add(enemyStatusEffectPanel, BorderLayout.SOUTH);

		return statusPanel;
	}

	private JPanel createSidePanel() {
		final JPanel sidePanel = new JPanel();
		BoxLayout layout = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
		sidePanel.setLayout(layout);

		JButton helpButton = new JButton();
		helpButton.setIcon(new ImageIcon(InternalImage.loadFromPath(IMAGE_PATH,
				"questionmark.png")));
		helpButton.setToolTipText("Wie geht das Spiel eigentlich?");
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HelpFrame helpFrame = new HelpFrame();
				helpFrame.setLocationRelativeTo(sidePanel);
			}
		});

		JButton adviceButton = new JButton();
		adviceButton.setToolTipText("SmartBot - Berater (DLC)");
		adviceButton.setIcon(new ImageIcon(InternalImage.loadFromPath(
				IMAGE_PATH, "smartbot.png")));
		adviceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Timo: AdviceBot programmieren
			}
		});

		helpButton.setBorderPainted(false);
		helpButton.setContentAreaFilled(false);
		adviceButton.setBorderPainted(false);
		adviceButton.setContentAreaFilled(false);

		sidePanel.add(Box.createVerticalStrut(6));
		sidePanel.add(helpButton);
		sidePanel.add(adviceButton);
		sidePanel.setVisible(true);
		return sidePanel;
	}

	private JPanel createInfoSection() {
		JPanel info = new JPanel();
		info.setLayout(new BorderLayout());

		info.add(createRoboStats(), BorderLayout.SOUTH);

		pnlTimer = createTimer();
		info.add(pnlTimer, BorderLayout.CENTER);

		return info;
	}

	private StatusEffectPanel createStatusEffectPanel(
			List<StatusEffect> statusEffects, String title) {
		return new StatusEffectPanel(statusEffects, title);
	}

	private JPanel createRoboStats() {
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));

		stats.add(Box.createVerticalStrut(30));

		// Lebensanzeige
		JPanel pnlLife = new JPanel();
		pnlLife.setLayout(new BoxLayout(pnlLife, BoxLayout.X_AXIS));
		stats.add(pnlLife);

		pnlLife.add(new JLabel(new ImageIcon(ImageUtils.getScaledInstance(
				InternalImage.loadFromPath(IMAGE_PATH, "life.png"), 20, 20,
				null))));

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
				InternalImage.loadFromPath(IMAGE_PATH, "energy.png"), 20, 20,
				null))));

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

	private JPanel createTimer() {

		JPanel timer = new JPanel();
		timer.setLayout(new BorderLayout());

		countdown = new Countdown();
		countdown.addActionListener(this);
		countdown.setVisible(false);
		timer.add(countdown, BorderLayout.CENTER);

		lblInfo = new JLabel();
		lblInfo.setHorizontalAlignment(JLabel.CENTER);
		timer.add(lblInfo, BorderLayout.SOUTH);

		return timer;
	}

	private JPanel createActionSelection() {
		JPanel actions = new JPanel();
		actions.setLayout(new BorderLayout());

		JPanel spActions = new JPanel();
		spActions.setLayout(new BorderLayout());
		actions.add(spActions, BorderLayout.CENTER);

		JPanel ownActions = new JPanel();
		spActions.add(ownActions, BorderLayout.CENTER);
		ownActions.setLayout(new BoxLayout(ownActions, BoxLayout.X_AXIS));

		ownActions.add(createAttackSelection());
		ownActions.add(Box.createHorizontalStrut(10));
		ownActions.add(createDefendSelection());

		JPanel otherActions = createOtherActionsPanel();
		spActions.add(otherActions, BorderLayout.SOUTH);

		return actions;
	}

	private JPanel createOtherActionsPanel() {

		JPanel otherActions = new JPanel();
		otherActions.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(
						BorderFactory.createLineBorder(Color.BLACK, 2, true),
						"Gegner Historie"), BorderFactory.createEmptyBorder(3,
				3, 3, 3)));
		otherActions.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {

			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {

				return new Dimension(0, super.getPreferredSize().height);
			}
		};
		otherActions.add(sp, BorderLayout.CENTER);

		if (actions == null) {
			actions = new JPanel();
			actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
		}
		sp.setViewportView(actions);

		return otherActions;
	}

	private JPanel createDefendSelection() {
		JPanel defends = new JPanel();
		defends.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(
						BorderFactory.createLineBorder(Color.BLACK, 2, true),
						"Verteidigung"), BorderFactory.createEmptyBorder(3, 3,
				3, 3)));
		defends.setLayout(new GridLayout(2, 2, 5, 5));

		List<Defense> defs = (robot != null) ? robot.getPossibleDefends()
				: new ArrayList<Defense>(0);
		for (int i = 0; i < 4; i++) {

			ActionButton def = new ActionButton();
			def.addActionListener(this);

			if (i < defs.size()) {

				Defense defense = defs.get(i);

				def.setRoboAction(defense);
				def.setText(String.format("%s - %.2f (%d E)",
						defense.getName(),
						(100 * defense.getBonusOnDefenseFactor()),
						defense.getEnergyCosts()));
				def.setIcon(defense.getRobotActionType());
				def.setToolTipText(ItemUtil
						.createToolTipTextForRobotActions(defense));
			} else {

				def.setText("<Leer>");
				def.setRoboAction(null);
			}

			defends.add(def);
		}

		return defends;
	}

	private JPanel createAttackSelection() {
		JPanel attacks = new JPanel();
		attacks.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(
						BorderFactory.createLineBorder(Color.BLACK, 2, true),
						"Angriff"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		attacks.setLayout(new GridLayout(2, 2, 5, 5));

		List<Attack> atks = (robot != null) ? robot.getPossibleAttacks()
				: new ArrayList<Attack>(0);
		for (int i = 0; i < 4; i++) {

			ActionButton atk = new ActionButton();
			atk.addActionListener(this);

			if (i < atks.size()) {

				Attack attack = atks.get(i);

				atk.setRoboAction(attack);
				atk.setText(String.format("%s - %d S (%d E)", attack.getName(),
						attack.getDamage(), attack.getEnergyCosts()));
				atk.setIcon(attack.getRobotActionType());
				atk.setToolTipText(ItemUtil
						.createToolTipTextForRobotActions(attack));
			} else {

				atk.setText("<Leer>");
				atk.setRoboAction(null);
			}

			attacks.add(atk);
		}

		return attacks;
	}

	private void updateStats(boolean interpretItems) {

		if (robot == null) {
			return;
		}

		Robot tmp = new Robot();

		tmp.setMaxHealthPoints(robot.getMaxHealthPoints());
		tmp.setHealthPoints(robot.getHealthPoints());

		tmp.setMaxEnergyPoints(robot.getMaxEnergyPoints());
		tmp.setEnergyPoints(robot.getEnergyPoints());

		if (interpretItems) {

			List<RoboItem> items = robot.getEquippedItems();

			for (RoboItem i : items) {

				i.performInitialRobotModification(tmp);
			}
		}

		LOG.debug("Stat update -> Health: {}({}), Energy: {}({})",
				tmp.getHealthPoints(), tmp.getMaxHealthPoints(),
				tmp.getEnergyPoints(), tmp.getMaxEnergyPoints());

		life.setMaximum(tmp.getMaxHealthPoints());
		life.setValue(tmp.getHealthPoints());
		lblLife.setText("" + tmp.getHealthPoints());

		energy.setMaximum(tmp.getMaxEnergyPoints());
		energy.setValue(tmp.getEnergyPoints());
		lblEnergy.setText("" + tmp.getEnergyPoints());

		ownStatusEffectPanel.update(robot.getStatusEffects());
		Robot updatedRobotOfEnemy = battleClient.getUpdatedRobotOfEnemy();
		LOG.debug("Enemy Robot: " + updatedRobotOfEnemy);

		if (updatedRobotOfEnemy != null) {
			enemyStatusEffectPanel.update(updatedRobotOfEnemy
					.getStatusEffects());
		}
	}

	private void actionSelection(RobotAction roboAction) {

		actionSelection(roboAction, false);
	}

	private void actionSelection(RobotAction roboAction, boolean force) {
		LOG.debug("Action Selected -> {}", roboAction);

		if (roboAction == null) {
			LOG.debug("Action selected -> <Leer>");
			return;
		}

		if (!force && countdown.getTime() <= 0) {
			LOG.debug("Countdown over -> no selection possible");
			return;
		}

		if (!force && roboAction.getEnergyCosts() > robot.getEnergyPoints()) {
			LOG.debug("Energy amount not suitable");
			JOptionPane
					.showMessageDialog(
							this,
							"Deine Energie reicht nicht aus, um diese Fähigkeit einzusetzen.",
							"Nicht genug Energie", JOptionPane.WARNING_MESSAGE);
			return;
		}

		battleClient.sendRobotActionToServer(roboAction);

		countdown.stopCountdown();
		countdown.setVisible(false);

		setInfo("Warte bis dein Gegener seine Aktion gewählt hat und die Runde beendet wurde.");
	}

	private void startCountdown() {

		setInfo("Bitte wähle deine nächste Aktion aus.");

		countdown.stopCountdown();
		countdown.setVisible(true);
		countdown.setTime(SELECTION_TIME);
		countdown.startCountdown();

		repack();
	}

	private void repack() {

		Container parent = this.getParent();

		while (!(parent instanceof JFrame) || parent == null) {

			parent = parent.getParent();
		}

		if (parent != null) {

			((JFrame) parent).pack();
		}
	}

	private void setInfo(String s) {

		lblInfo.setText(s);
	}

	public void updateRobot() {

		updateRobot(false);
	}

	public void updateRobot(boolean interpretItems) {

		remove(pnlActionSelection);

		pnlActionSelection = createActionSelection();
		add(pnlActionSelection);

		updateStats(interpretItems);

		revalidate();
		repaint();
	}

	private void putEnemyAction(RobotAction robotAction) {

		int actionCount = actions.getComponentCount();
		while (actionCount > 7) {

			actions.remove(actionCount - 1);
			actionCount = actions.getComponentCount();
		}

		ActionButton lblAction = new ActionButton();
		lblAction.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(3, 0, 3, 5),
				BorderFactory.createLineBorder(Color.BLACK, 2, true)));
		lblAction.setBorderPainted(true);
		lblAction.setContentAreaFilled(false);

		if (robotAction instanceof Attack) {

			Attack attack = (Attack) robotAction;

			lblAction.setRoboAction(attack);
			lblAction.setText(String.format("%s - %d S (%d E)",
					attack.getName(), attack.getDamage(),
					attack.getEnergyCosts()));
			lblAction.setIcon(attack.getRobotActionType());
			lblAction.setToolTipText(ItemUtil
					.createToolTipTextForRobotActions(attack));
		} else if (robotAction instanceof Defense) {

			Defense defense = (Defense) robotAction;

			lblAction.setRoboAction(defense);
			lblAction.setText(String.format("%s - %.2f (%d E)",
					defense.getName(),
					(100 * defense.getBonusOnDefenseFactor()),
					defense.getEnergyCosts()));
			lblAction.setIcon(defense.getRobotActionType());
			lblAction.setToolTipText(ItemUtil
					.createToolTipTextForRobotActions(defense));
		} else {

			lblAction.setText(String.format("%s (%s)", robotAction.getName(),
					robotAction.getDescription()));
		}

		actions.add(lblAction, 0);
		revalidate();
		repaint();
		repack();
	}

	private void countdownOver() {

		LOG.debug("Countdown over -> auto select action");
		countdown.setVisible(false);

		List<Attack> attacks = robot.getPossibleAttacks();
		List<Defense> defends = robot.getPossibleDefends();

		List<RobotAction> actions = new ArrayList<RobotAction>(attacks.size()
				+ defends.size());
		actions.addAll(attacks);
		actions.addAll(defends);

		Collections.shuffle(actions);

		for (RobotAction action : actions) {

			if (action.getEnergyCosts() <= robot.getEnergyPoints()) {

				actionSelection(action, true);
				break;
			}
		}
	}

	@Override
	public void startActionSelection() {

		robot = battleClient.getUpdatedRobot();
		updateRobot(false);
		startCountdown();
	}

	@Override
	public void gameOver(GameStateType result) {

		LOG.debug("Game Over -> Result: " + result);

		Robot robotTmp = battleClient.getUpdatedRobot();

		if (robotTmp != null) {

			robot = robotTmp;
			updateRobot();
		}

		countdown.stopCountdown();
		countdown.setVisible(false);

		RobotPosition ownPosition = battleClient.getPositionOfOwnRobot();

		String msg = "";

		switch (result) {

		case DRAW:
			msg = "Keiner von Euch hat gewonnen ;(";
			break;

		case VICTORY_LEFT:

			if (ownPosition == RobotPosition.LEFT) {

				msg = "Du hast gewonnen :)";
			} else {

				msg = "Du hast verloren :(";
			}
			break;

		case VICTORY_RIGHT:

			if (ownPosition == RobotPosition.RIGHT) {

				msg = "Du hast gewonnen :)";
			} else {

				msg = "Du hast verloren :(";
			}
			break;

		default:
			return;
		}

		JOptionPane.showMessageDialog(this, msg, "Game Over",
				JOptionPane.INFORMATION_MESSAGE);
		System.exit(-1);

	}

	@Override
	public void receiveEnemyRobotAction(final RobotAction robotAction) {
		LOG.debug("Received enemy robot action: {}", robotAction);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {

					putEnemyAction(robotAction);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			LOG.warn("Could not add enemy action", e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if (source == countdown) {
			if (countdown.getTime() == 0) {

				countdownOver();
			}
		} else if (source instanceof ActionButton) {

			actionSelection(((ActionButton) source).getRoboAction());
		}
	}

	public StatusEffectPanel getEnemyStatusEffectPanel() {
		return enemyStatusEffectPanel;
	}

	public void setEnemyStatusEffectPanel(
			StatusEffectPanel enemyStatusEffectPanel) {
		this.enemyStatusEffectPanel = enemyStatusEffectPanel;
	}

}
