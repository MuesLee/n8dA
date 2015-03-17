package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.InternalImage;
import game.engine.image.sprite.DefaultSprite;
import game.engine.image.sprite.Sprite;
import game.engine.stage.SwingStage;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.time.Clock;
import game.engine.time.TimeUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bno.swing2.widget.BTextField;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public class RoboConfigurationPanel extends JPanel implements ActionListener
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/client/images/";
	private static final long serialVersionUID = 1L;
	private SwingStage roboStage;
	private RoboScene roboScene;
	private JLabel lblRoboName;
	private BTextField txtRoboName;
	private JButton nextRobo;
	private JButton prevRobo;
	private Clock clk;

	private int selectedRobot;
	private Robot[] robots;

	public RoboConfigurationPanel(Robot[] robots) throws IOException
	{
		this();
		setRobots(robots);
	}

	public RoboConfigurationPanel()
	{

		createGui();
	}

	private void createGui()
	{

		setLayout(new BorderLayout());

		add(createRoboSelection(), BorderLayout.CENTER);
	}

	private JPanel createRoboSelection()
	{

		JPanel robo = new JPanel();
		robo.setLayout(new BorderLayout());
		add(robo, BorderLayout.CENTER);

		//Roboter Navigation
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

		//Eingabe für den Namen des Roboters
		JPanel centerTxt = new JPanel();
		centerTxt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerTxt.setLayout(new BorderLayout());
		robo.add(centerTxt, BorderLayout.SOUTH);

		txtRoboName = new BTextField(20);
		txtRoboName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		txtRoboName.setHint("Name eingeben...");
		txtRoboName.setIgnoreHintFocus(true);
		centerTxt.add(txtRoboName);

		lblRoboName = new JLabel("<Roboter Name>");
		lblRoboName.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoboName.setVerticalAlignment(SwingConstants.CENTER);
		robo.add(lblRoboName, BorderLayout.NORTH);

		//Die Roboter Animation
		clk = new Clock();

		JPanel stagePanel = new JPanel();
		stagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		robo.add(stagePanel, BorderLayout.CENTER);

		roboStage = new SwingStage();
		roboStage.setPreferredSize(new Dimension(128, 256));
		clk.addClockListener(roboStage);
		stagePanel.add(roboStage);

		roboScene = new RoboScene();
		roboStage.setScene(roboScene);

		clk.start();

		return robo;
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

		lblRoboName.setText(robo.getName());

		setRoboAni(robo.getAnimation());
	}

	private void setRoboAni(Animation ani) throws IOException
	{

		BufferedImage img = ImageIO.read(new File(ani.getPathToFile()));
		int width = ani.getFrameWidth();
		int height = ani.getFrameHeight();
		Sprite sprite = new DefaultSprite(img, width, height);

		long[][] time = new long[][] { ani.getFrameTimings() };
		long defaultTime = time[0][0];

		roboScene.setRoboAnimation(new AnimatedSceneObject(sprite, defaultTime, time));
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

			previousRobot();
		}
		else if (source == nextRobo)
		{

			nextRobot();
		}
	}

	public void previousRobot()
	{

		changeRoboIndex(-1);
	}

	public void nextRobot()
	{

		changeRoboIndex(+1);
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

	public static void main(String[] args) throws IOException
	{
		JFrame disp = new JFrame();
		disp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		disp.setSize(800, 600);
		disp.setLocationRelativeTo(null);

		RoboConfigurationPanel comp = new RoboConfigurationPanel();
		comp.setRoboAni(new Animation("id",
			"M:\\34000_GB_IT_Austausch\\N8derAusbildung\\Animationen\\Roboter\\GirlRoboter\\GirlRoboterZAnimation.png",
			new long[] { TimeUtils.NanosecondsOfMilliseconds(100) }, 64, 128));

		disp.add(comp);

		disp.pack();
		disp.setVisible(true);
	}
}
