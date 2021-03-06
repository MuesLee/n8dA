package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;
import game.engine.image.sprite.DefaultSprite;
import game.engine.image.sprite.Sprite;
import game.engine.stage.SwingStage;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.time.Clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bno.swing2.widget.BTextField;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public class RoboConfigurationPanel extends JPanel implements ActionListener
{

	private static final Logger LOG = LoggerFactory.getLogger(RoboConfigurationPanel.class);

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";
	private static final long serialVersionUID = 1L;

	private Map<String, BufferedImage> animations = new WeakHashMap<String, BufferedImage>();

	private SwingStage roboStage;
	private AnimationScene roboScene;

	private JLabel lblRoboName;
	private BTextField txtRoboName;

	private JButton nextRobo;
	private JButton prevRobo;

	private Clock clk;

	private int selectedRobot;
	private Robot[] robots;

	private List<JButton> btnAtks;
	private ArrayList<JButton> btnDefs;
	private JList<RoboItem> itemList;
	private JButton buyItems;

	private long maxCredit;

	private Attack[] attacks;
	private Defense[] defends;
	private RoboItem[] items;
	private JButton btnStart;
	private JLabel lblHealth;
	private JLabel lblEnergy;

	private JLabel lblCosts;

	public RoboConfigurationPanel(Robot[] robots, Attack[] attacks, Defense[] defends, RoboItem[] items, long maxCredit)
		throws IOException
	{

		this();

		LOG.debug("# of robots available for configuration: {}", robots.length);

		this.attacks = attacks;
		this.defends = defends;
		this.items = items;
		this.maxCredit = maxCredit;

		setRobots(robots);
	}

	private RoboConfigurationPanel()
	{
		createGui();
	}

	private void createGui()
	{

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(createRoboSelection(), BorderLayout.NORTH);
		add(createObjectSelection(), BorderLayout.CENTER);
		add(createButtonsRow(), BorderLayout.SOUTH);
	}

	private JPanel createButtonsRow()
	{
		JPanel border = new JPanel();
		border.setLayout(new BorderLayout());
		border.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		border.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

		final JPanel btnRow = new JPanel();
		btnRow.setLayout(new FlowLayout(FlowLayout.RIGHT));
		border.add(btnRow, BorderLayout.CENTER);

		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		btnStart.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		btnStart.setContentAreaFilled(false);

		JButton btnRules = new JButton("Regeln");
		btnRules.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				HelpFrame helpFrame = new HelpFrame();
				helpFrame.setLocationRelativeTo(btnRow);
			}
		});
		btnRules.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		btnRules.setContentAreaFilled(false);

		btnRow.add(btnRules);
		btnRow.add(btnStart);

		return border;
	}

	private JPanel createObjectSelection()
	{

		JPanel selBorder = new JPanel();
		selBorder.setLayout(new BorderLayout());
		selBorder.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

		JPanel selection = new JPanel();
		selection.setLayout(new GridLayout(1, 3, 5, 5));
		selBorder.add(selection, BorderLayout.CENTER);

		selection.add(createAtkSelection());
		selection.add(createItemSelection());
		selection.add(createDefSelection());

		return selBorder;
	}

	private JPanel createDefSelection()
	{
		JPanel defBorder = new JPanel();
		defBorder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
			"Verteidigung"));
		defBorder.setLayout(new BorderLayout());

		JPanel def = new JPanel();
		def.setLayout(new GridLayout(0, 2, 5, 5));
		defBorder.add(def, BorderLayout.CENTER);

		btnDefs = new ArrayList<JButton>(4);

		for (int i = 0; i < 4; i++)
		{

			JButton df = new JButton("<Leer>");
			df.setBorder(BorderFactory.createLineBorder(Color.RED, 1, true));
			df.setContentAreaFilled(false);
			df.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{

					selectDefends();
				}
			});

			def.add(df);
			btnDefs.add(df);
		}

		return defBorder;
	}

	private JPanel createItemSelection()
	{
		JPanel itemBorder = new JPanel();
		itemBorder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
			"Item"));
		itemBorder.setLayout(new BorderLayout());

		JPanel item = new JPanel();
		item.setLayout(new BorderLayout());
		itemBorder.add(item, BorderLayout.CENTER);

		JScrollPane spItems = new JScrollPane();
		item.add(spItems, BorderLayout.CENTER);

		itemList = new JList<RoboItem>();
		itemList.setSelectionModel(new DisabledItemSelectionModel());
		itemList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		spItems.setViewportView(itemList);

		buyItems = new JButton("Kaufen");
		buyItems.addActionListener(this);
		buyItems.setContentAreaFilled(false);
		buyItems.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		item.add(buyItems, BorderLayout.SOUTH);

		return itemBorder;
	}

	private JPanel createAtkSelection()
	{
		JPanel atkBorder = new JPanel();
		atkBorder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
			"Attacke"));
		atkBorder.setLayout(new BorderLayout());

		JPanel atk = new JPanel();
		atk.setLayout(new GridLayout(0, 2, 5, 5));
		atkBorder.add(atk, BorderLayout.CENTER);

		btnAtks = new ArrayList<JButton>(4);

		for (int i = 0; i < 4; i++)
		{

			JButton ak = new JButton("<Leer>");
			ak.setBorder(BorderFactory.createLineBorder(Color.RED, 1, true));
			ak.setContentAreaFilled(false);
			ak.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{

					selectAttacks();
				}
			});

			atk.add(ak);
			btnAtks.add(ak);
		}

		return atkBorder;
	}

	private void selectDefends()
	{
		LOG.debug("Def selection");

		modifyRobot();
	}

	private void selectAttacks()
	{
		LOG.debug("Atk selection");

		modifyRobot();
	}

	private void selectItems()
	{
		LOG.debug("Atk selection");

		modifyRobot();
	}

	private void modifyRobot()
	{
		LOG.debug("modify robot");

		if (robots == null || robots.length <= 0)
		{
			throw new RuntimeException("Keine Roboter zum Konfigurieren vorhanden.");
		}

		Robot actualRobot = robots[selectedRobot];
		if (actualRobot.isLoadedAsUserRobot())
		{

			JOptionPane.showMessageDialog(this, "Der ausgewählte Roboter kann nicht geändert werden.",
				"Benutzerdefinierter Roboter", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Robot robo = ConfigShop.getConfiguration(actualRobot, maxCredit, items, attacks, defends);

		actualizeModifications(robo);
	}

	private void actualizeModifications(Robot robo)
	{
		LOG.debug("actualize robot config");

		List<RoboItem> items = robo.getEquippedItems();
		List<Attack> attacks = robo.getPossibleAttacks();
		List<Defense> defends = robo.getPossibleDefends();

		// Aktualisiere Items
		DefaultListModel<RoboItem> itemModel = new DefaultListModel<RoboItem>();
		for (RoboItem item : items)
		{
			itemModel.addElement(item);
		}
		itemList.setModel(itemModel);
		itemList.revalidate();
		itemList.repaint();

		// Aktualisiere Verteidigungen
		Iterator<Defense> defs = defends.iterator();
		for (JButton def : btnDefs)
		{

			if (defs.hasNext())
			{
				def.setText(defs.next().getName());
			}
			else
			{
				def.setText("<Leer>");
			}
		}

		// Aktualisiere Attacken
		Iterator<Attack> atks = attacks.iterator();
		for (JButton atk : btnAtks)
		{

			if (atks.hasNext())
			{
				atk.setText(atks.next().getName());
			}
			else
			{
				atk.setText("<Leer>");
			}
		}
	}

	private JPanel createRoboSelection()
	{

		JPanel robo = new JPanel();
		robo.setLayout(new BorderLayout());

		// Roboter Navigation
		prevRobo = new JButton();
		prevRobo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		prevRobo.setContentAreaFilled(false);
		prevRobo.setIcon(new ImageIcon(InternalImage.loadFromPath(IMAGE_PATH, "selection_left.png")));
		prevRobo.addActionListener(this);
		robo.add(prevRobo, BorderLayout.WEST);

		nextRobo = new JButton();
		nextRobo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		nextRobo.setContentAreaFilled(false);
		nextRobo.setIcon(new ImageIcon(InternalImage.loadFromPath(IMAGE_PATH, "selection_right.png")));
		nextRobo.addActionListener(this);
		robo.add(nextRobo, BorderLayout.EAST);

		// Robo stats
		JPanel bottomRow = new JPanel();
		bottomRow.setLayout(new BorderLayout());
		robo.add(bottomRow, BorderLayout.SOUTH);

		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
		bottomRow.add(stats, BorderLayout.NORTH);

		lblCosts = new JLabel("Kosten: 200(100)");
		lblCosts.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		stats.add(lblCosts);

		lblHealth = new JLabel("Leben: 180");
		lblHealth.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		stats.add(lblHealth);

		lblEnergy = new JLabel("Energie: 80");
		lblEnergy.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		stats.add(lblEnergy);

		// Eingabe für den Namen des Roboters
		JPanel centerTxt = new JPanel();
		centerTxt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerTxt.setLayout(new BorderLayout());
		bottomRow.add(centerTxt, BorderLayout.CENTER);

		txtRoboName = new BTextField(20);
		txtRoboName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		txtRoboName.setHint("Name eingeben...");
		txtRoboName.setIgnoreHintFocus(true);
		centerTxt.add(txtRoboName);

		lblRoboName = new JLabel("<Roboter Name>");
		lblRoboName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		lblRoboName.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoboName.setVerticalAlignment(SwingConstants.CENTER);
		robo.add(lblRoboName, BorderLayout.NORTH);

		// Die Roboter Animation
		clk = new Clock();

		JPanel stageBorder = new JPanel();
		stageBorder.setLayout(new BorderLayout());
		stageBorder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		robo.add(stageBorder, BorderLayout.CENTER);

		JPanel stagePanel = new JPanel();
		stagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		stageBorder.add(stagePanel, BorderLayout.CENTER);

		roboStage = new SwingStage();
		roboStage.setPreferredSize(new Dimension(128, 256));
		clk.addClockListener(roboStage);
		stagePanel.add(roboStage);

		roboScene = new AnimationScene();
		roboStage.setScene(roboScene);

		clk.start();

		// Horizontal center robo
		JPanel horizontalCenter = new JPanel();
		horizontalCenter.setLayout(new BoxLayout(horizontalCenter, BoxLayout.X_AXIS));

		robo.setMaximumSize(robo.getPreferredSize());

		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createGlue());
		hBox.add(robo);
		hBox.add(Box.createGlue());
		horizontalCenter.add(hBox);

		return horizontalCenter;
	}

	private void changeRoboIndex(int increase)
	{

		selectedRobot += increase;

		checkIndexPosition();
	}

	private void actualizeActiveRobot() throws IOException
	{

		if (robots == null)
		{
			return;
		}

		checkIndexPosition();

		setActiveRobot(robots[selectedRobot]);
	}

	private void checkIndexPosition()
	{
		if (robots == null)
		{
			return;
		}

		if (selectedRobot >= robots.length)
		{
			selectedRobot = 0;
		}
		else if (selectedRobot < 0)
		{
			selectedRobot = robots.length - 1;
		}
	}

	private void setActiveRobot(Robot robo) throws IOException
	{

		if (robo.isLoadedAsUserRobot())
		{
			txtRoboName.setText(robo.getName());
			txtRoboName.setEditable(false);
		}
		else
		{
			txtRoboName.setEditable(true);
			txtRoboName.setText("");
		}

		lblRoboName.setText(robo.getName());
		lblHealth.setText(String.format("Leben: %d", robo.getMaxHealthPoints()));
		lblEnergy.setText(String.format("Energie: %d", robo.getMaxEnergyPoints()));
		lblCosts
			.setText(String.format("Kosten: %d(%d)", getConfigurationCosts(robo), robo.getConfigurationPointCosts()));

		setRoboAni(robo.getAnimation());
		actualizeModifications(robo);
	}

	private void setRoboAni(Animation ani) throws IOException
	{

		BufferedImage img = animations.get(ani.getId());
		if (img == null)
		{

			LOG.debug("------------>" + Main.SOURCE_FOLDER + ani.getPathToFile());

			img = ImageIO.read(Paths.get(Main.SOURCE_FOLDER, ani.getPathToFile()).toFile());
			animations.put(ani.getId(), img);
		}

		int width = ani.getFrameWidth();
		int height = ani.getFrameHeight();
		Sprite sprite = new DefaultSprite(img, width, height);

		long[][] time = new long[][] { ani.getFrameTimings() };
		long defaultTime = time[0][0];

		roboScene.setAnimation(new AnimatedSceneObject(sprite, defaultTime, time));
	}

	private void checkConfigurationAndStart()
	{

		Robot robo = robots[selectedRobot];

		if (!validate(robo))
		{

			return;
		}

		String nickName = txtRoboName.getText();
		robo.setNickname(nickName);
		robo.setUuid(UUID.randomUUID());

		ConfigurationListener[] listeners = listenerList.getListeners(ConfigurationListener.class);
		for (ConfigurationListener lis : listeners)
		{

			lis.configurationCompleted(robo);
		}
	}

	private boolean validate(Robot robo)
	{

		List<Attack> atks = robo.getPossibleAttacks();
		List<Defense> defs = robo.getPossibleDefends();

		if (atks.size() > 4 || defs.size() > 4)
		{

			JOptionPane.showMessageDialog(this,
				"Du hast zu viele Attacken oder Verteidigungen. \nEs sind maximal vier Fähigkeiten erlaubt.",
				"Zu viele Fähigkeiten", JOptionPane.ERROR_MESSAGE);

			return false;
		}

		boolean basicAttack = false;
		for (Attack atk : atks)
		{

			if (atk.getEnergyCosts() <= 0)
			{

				basicAttack = true;
				break;
			}
		}

		if (!basicAttack)
		{

			JOptionPane.showMessageDialog(this,
				"Es muss mindestens eine Basisattacke(keine Energiekosten) ausgewählt sein.", "Keine Basisattacke",
				JOptionPane.ERROR_MESSAGE);

			return false;
		}

		long costs = getConfigurationCosts(robo);

		if (costs > maxCredit)
		{

			JOptionPane.showMessageDialog(this, "Du hast nicht genügend Credits um dir das leisten zu können.",
				"Keine Credits", JOptionPane.ERROR_MESSAGE);

			return false;
		}

		if (txtRoboName.getText().isEmpty())
		{

			JOptionPane.showMessageDialog(this, "Du hast noch keinen Namen für deinen Roboter eingegeben.",
				"Nickname eingeben", JOptionPane.ERROR_MESSAGE);

			return false;
		}

		return true;
	}

	private long getConfigurationCosts(Robot robo)
	{
		List<Attack> atks = robo.getPossibleAttacks();
		List<Defense> defs = robo.getPossibleDefends();
		List<RoboItem> items = robo.getEquippedItems();

		long costs = robo.getConfigurationPointCosts();
		for (Attack atk : atks)
		{

			costs += atk.getConfigurationPointCosts();
		}
		for (Defense def : defs)
		{

			costs += def.getConfigurationPointCosts();
		}
		for (RoboItem item : items)
		{

			costs += item.getConfigurationPointCosts();
		}

		return costs;
	}

	public void pauseAnimation(boolean pause)
	{

		clk.setPaused(pause);
	}

	/**
	 * Release threads
	 */
	public void dispose()
	{

		clk.destroy();
	}

	@Override
	protected void finalize() throws Throwable
	{

		clk.destroy();

		super.finalize();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		Object source = e.getSource();

		if (source == prevRobo)
		{

			LOG.debug("prev Robo");
			previousRobot();
		}
		else if (source == nextRobo)
		{

			LOG.debug("next Robo");
			nextRobot();
		}
		else if (source == buyItems)
		{

			LOG.debug("buy items");
			selectItems();
		}
		else if (source == btnStart)
		{

			LOG.debug("start");
			checkConfigurationAndStart();
		}
	}

	public void previousRobot()
	{

		changeRoboIndex(-1);
		try
		{
			setActiveRobot(robots[selectedRobot]);
		}
		catch (IOException e)
		{
		}
	}

	public void nextRobot()
	{

		changeRoboIndex(+1);
		try
		{
			setActiveRobot(robots[selectedRobot]);
		}
		catch (IOException e)
		{
		}
	}

	public Robot[] getRobots()
	{
		return robots;
	}

	public void setRobots(Robot[] robots) throws IOException
	{
		this.robots = robots;
		this.selectedRobot = 0;

		actualizeActiveRobot();
	}

	public void addConfigurationListener(ConfigurationListener li)
	{

		listenerList.add(ConfigurationListener.class, li);
	}

	public void removeConfigurationListener(ConfigurationListener li)
	{

		listenerList.remove(ConfigurationListener.class, li);
	}

}
