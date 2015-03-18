package de.kvwl.n8dA.robotwars.server.input;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.util.ItemUtil;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

/**
 * 
 * Lädt die Animationen aus eienr Verzeichnisstruktur.<br>
 * <br>
 * Unter dem root Ordner befinden sich die Ordner für die Animationen [animations] und [objects] für
 * die actions/robots.<br>
 * <br>
 * Innerhalb der Animationen wird in [robots] und [actions] unterteilt. Die Actions selber sind in
 * [attacks] und [defends] unterteilt. <br>
 * <br>
 * Jede Animation besteht darunter wieder aus einem Ordner, der die Dateien [info.xml] und
 * [animation.png] <br>
 * <br>
 * Für die objects bestehen die Ordner [robots] und [actions].<br>
 * <br>
 * Die actions sind wiederum unterteilt in [defends] und [attacks].<br>
 * <br>
 * Jedes object besteht darunter wieder aus einem Ordner und darin den Dateien [info.xml]
 */
public class DataLoaderFileSystemImpl implements DataLoader
{

	private SAXBuilder builder = new SAXBuilder();

	private Path sourceFolder;

	private Path animationFolder;
	private Path robotAniFolder;
	private Path actionAniFolder;
	private Path atkAniFolder;
	private Path defAniFolder;

	private Path objectFolder;
	private Path robotFolder;
	private Path actionFolder;
	private Path atkFolder;
	private Path defFolder;

	public DataLoaderFileSystemImpl()
	{

		this(Paths.get("./"));
	}

	public DataLoaderFileSystemImpl(Path sourceFolder)
	{

		this.sourceFolder = sourceFolder;
		createPaths();
	}

	private void createPaths()
	{

		animationFolder = sourceFolder.resolve("animations");
		robotAniFolder = animationFolder.resolve("robots");
		actionAniFolder = animationFolder.resolve("actions");
		atkAniFolder = actionAniFolder.resolve("attacks");
		defAniFolder = actionAniFolder.resolve("defends");

		objectFolder = sourceFolder.resolve("objects");
		robotFolder = objectFolder.resolve("robots");
		actionFolder = objectFolder.resolve("actions");
		atkFolder = actionFolder.resolve("attacks");
		defFolder = actionFolder.resolve("defends");
	}

	public void createFolderStructure() throws IOException
	{

		Files.createDirectories(animationFolder);
		Files.createDirectories(robotAniFolder);
		Files.createDirectories(actionAniFolder);
		Files.createDirectories(atkAniFolder);
		Files.createDirectories(defAniFolder);

		Files.createDirectories(objectFolder);
		Files.createDirectories(robotFolder);
		Files.createDirectories(actionFolder);
		Files.createDirectories(atkFolder);
		Files.createDirectories(defFolder);
	}

	public Animation readAnimation(Path info) throws JDOMException, IOException
	{

		String id;
		String relativePathToFile;
		long[] frameTimings;
		int frameWidth;
		int frameHeight;

		Path absolutePath = info.getParent().resolve("animation.png").toAbsolutePath();
		relativePathToFile = sourceFolder.toAbsolutePath().relativize(absolutePath).toString();

		Document doc = builder.build(Files.newInputStream(info));

		Element animation = doc.getRootElement();

		frameWidth = Integer.valueOf(animation.getChild("width").getValue());
		frameHeight = Integer.valueOf(animation.getChild("height").getValue());
		id = animation.getChild("id").getValue();

		Element timeContainer = animation.getChild("times");
		int containerSize = timeContainer.getAttribute("size").getIntValue();

		List<Element> times = timeContainer.getChildren("time");

		frameTimings = new long[Math.max(times.size(), containerSize)];

		for (Element time : times)
		{

			frameTimings[time.getAttribute("frame").getIntValue()] = Long.valueOf(time.getValue());
		}

		return new Animation(id, relativePathToFile, frameTimings, frameWidth, frameHeight);
	}

	public Attack readAttack(Path info, List<Animation> attackAnimations) throws JDOMException, IOException
	{

		RobotActionType type;
		int damage;
		int configurationPointCosts;
		int energyCosts;
		String name;
		long id;
		Animation animation;
		String animationId;

		Document doc = builder.build(Files.newInputStream(info));
		Element atk = doc.getRootElement();

		type = RobotActionType.valueOf(atk.getChild("type").getValue());
		damage = Integer.valueOf(atk.getChild("damage").getValue());
		configurationPointCosts = Integer.valueOf(atk.getChild("configcosts").getValue());
		energyCosts = Integer.valueOf(atk.getChild("energycosts").getValue());
		name = atk.getChild("name").getValue();
		id = Long.valueOf(atk.getChild("id").getValue());
		animationId = atk.getChild("animationid").getValue();
		animation = getAnimation(attackAnimations, animationId);

		Attack attack = new Attack(type, damage);
		attack.setConfigurationPointCosts(configurationPointCosts);
		attack.setEnergyCosts(energyCosts);
		attack.setName(name);
		attack.setAnimation(animation);
		attack.setId(id);

		return attack;
	}

	public Defense readDefense(Path info, List<Animation> defenseAnimations) throws JDOMException, IOException
	{

		RobotActionType type;
		double bonusOnDefenceFactor;
		int configurationPointCosts;
		int energyCosts;
		String name;
		long id;
		Animation animation;
		String animationId;

		Document doc = builder.build(Files.newInputStream(info));
		Element atk = doc.getRootElement();

		type = RobotActionType.valueOf(atk.getChild("type").getValue());
		bonusOnDefenceFactor = Double.valueOf(atk.getChild("defensefactor").getValue());
		configurationPointCosts = Integer.valueOf(atk.getChild("configcosts").getValue());
		energyCosts = Integer.valueOf(atk.getChild("energycosts").getValue());
		name = atk.getChild("name").getValue();
		id = Long.valueOf(atk.getChild("id").getValue());
		animationId = atk.getChild("animationid").getValue();
		animation = getAnimation(defenseAnimations, animationId);

		Defense defense = new Defense(type, bonusOnDefenceFactor);
		defense.setConfigurationPointCosts(configurationPointCosts);
		defense.setEnergyCosts(energyCosts);
		defense.setName(name);
		defense.setAnimation(animation);
		defense.setId(id);

		return defense;
	}

	public Robot readRobot(Path info, List<Animation> robotAnimations) throws JDOMException, IOException
	{

		long id;
		String animationId;
		Animation animation;
		String name;
		int configurationPointCosts;
		int energyPoints;
		int healthPoints;
		List<RoboItem> defaultItems;

		Document doc = builder.build(Files.newInputStream(info));
		Element robo = doc.getRootElement();

		id = Long.valueOf(robo.getChild("id").getValue());
		animationId = robo.getChild("animationid").getValue();
		animation = getAnimation(robotAnimations, animationId);
		name = robo.getChild("name").getValue();
		configurationPointCosts = Integer.valueOf(robo.getChild("configcosts").getValue());
		energyPoints = Integer.valueOf(robo.getChild("energypoints").getValue());
		healthPoints = Integer.valueOf(robo.getChild("healthpoints").getValue());

		List<Element> defItems = robo.getChild("defaultitems").getChildren("item");
		defaultItems = new ArrayList<RoboItem>(defItems.size() + 10);
		for (Element it : defItems)
		{

			defaultItems.add(getItemById(Long.valueOf(it.getValue())));
		}

		Robot robot = new Robot();
		robot.setId(id);
		robot.setAnimation(animation);
		robot.setName(name);
		robot.setConfigurationPointCosts(configurationPointCosts);
		robot.setEnergyPoints(energyPoints);
		robot.setHealthPoints(healthPoints);
		robot.setEquippedItems(defaultItems);

		return robot;
	}

	private RoboItem getItemById(Long itemId)
	{
		List<RoboItem> roboItems = ItemUtil.getAllRoboItems();

		for (RoboItem item : roboItems)
		{

			if (item.getId() == itemId)
			{

				return item;
			}
		}

		throw new RuntimeException(String.format("Item %d nicht gefunden.", itemId));
	}

	private Animation getAnimation(List<Animation> animations, String animationId)
	{

		for (Animation ani : animations)
		{

			if (ani.getId().equals(animationId))
			{

				return ani;
			}
		}

		throw new RuntimeException("Keine Animtaion für " + animationId + " gefunden");
	}

	private List<Animation> loadDefAnimations()
	{
		return loadAnimationsFromFolder(defAniFolder);
	}

	private List<Animation> loadAtkAnimations()
	{
		return loadAnimationsFromFolder(atkAniFolder);
	}

	private List<Animation> loadAnimationsFromFolder(Path folder)
	{

		List<Animation> anis = new LinkedList<Animation>();

		try
		{
			DirectoryStream<Path> dirs = Files.newDirectoryStream(folder);

			for (Path dir : dirs)
			{

				anis.add(readAnimation(dir.resolve("info.xml")));
			}
		}
		catch (IOException | JDOMException e)
		{
			e.printStackTrace();
		}

		return anis;
	}

	@Override
	public List<Animation> loadAnimationsForRobots()
	{
		return loadAnimationsFromFolder(robotAniFolder);
	}

	@Override
	public List<Animation> loadAnimationsForRobotActions()
	{

		List<Animation> anis = new LinkedList<Animation>();

		anis.addAll(loadAtkAnimations());
		anis.addAll(loadDefAnimations());

		return anis;
	}

	@Override
	public List<Robot> loadRobots()
	{

		List<Animation> roboAnis = loadAnimationsForRobots();

		List<Robot> robos = new LinkedList<Robot>();

		try
		{
			DirectoryStream<Path> objs = Files.newDirectoryStream(robotFolder);

			for (Path obj : objs)
			{

				robos.add(readRobot(obj.resolve("info.xml"), roboAnis));
			}
		}
		catch (IOException | JDOMException e)
		{
			e.printStackTrace();
		}

		return robos;
	}

	@Override
	public List<Attack> loadRobotAttacks()
	{

		List<Animation> atkAnimations = loadAtkAnimations();

		List<Attack> attacks = new LinkedList<Attack>();

		try
		{
			DirectoryStream<Path> objs = Files.newDirectoryStream(atkFolder);

			for (Path obj : objs)
			{

				attacks.add(readAttack(obj.resolve("info.xml"), atkAnimations));
			}
		}
		catch (IOException | JDOMException e)
		{
			e.printStackTrace();
		}

		return attacks;
	}

	@Override
	public List<Defense> loadRobotDefends()
	{

		List<Animation> defAnimations = loadDefAnimations();

		List<Defense> defends = new LinkedList<Defense>();

		try
		{
			DirectoryStream<Path> objs = Files.newDirectoryStream(defFolder);

			for (Path obj : objs)
			{

				defends.add(readDefense(obj.resolve("info.xml"), defAnimations));
			}
		}
		catch (IOException | JDOMException e)
		{
			e.printStackTrace();
		}

		return defends;
	}

}
