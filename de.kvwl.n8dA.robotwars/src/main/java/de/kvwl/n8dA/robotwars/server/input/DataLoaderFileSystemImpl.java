package de.kvwl.n8dA.robotwars.server.input;

import game.engine.image.sprite.DefaultSprite;
import game.engine.image.sprite.Sprite;
import game.engine.stage.scene.object.AnimatedSceneObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionPowerType;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.ItemUtil;
import de.kvwl.n8dA.robotwars.commons.game.util.StatusEffectUtil;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;
import de.kvwl.n8dA.robotwars.commons.utils.Null;

/**
 * 
 * Lädt die Animationen aus eienr Verzeichnisstruktur.<br>
 * <br>
 * Unter dem root Ordner befinden sich die Ordner für die Animationen
 * [animations] und [objects] für die actions/robots.<br>
 * <br>
 * Innerhalb der Animationen wird in [robots] und [actions] unterteilt. Die
 * Actions selber sind in [attacks] und [defends] unterteilt. <br>
 * <br>
 * Jede Animation besteht darunter wieder aus einem Ordner, der die Dateien
 * [info.xml] und [animation.png] <br>
 * <br>
 * Für die objects bestehen die Ordner [robots] und [actions].<br>
 * <br>
 * Die actions sind wiederum unterteilt in [defends] und [attacks].<br>
 * <br>
 * Jedes object besteht darunter wieder aus einem Ordner und darin den Dateien
 * [info.xml]
 */
public class DataLoaderFileSystemImpl implements DataLoader {

	private static final Logger LOG = LoggerFactory
			.getLogger(DataLoaderFileSystemImpl.class);

	private Map<String, BufferedImage> animations = new WeakHashMap<String, BufferedImage>();
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

	private Path userObjectFolder;

	private WeakReference<List<Animation>> robotAnimations;

	private WeakReference<List<Animation>> robotActionAnimations;

	private WeakReference<List<Robot>> systemRobots;

	private WeakReference<List<Robot>> userRobots;

	private WeakReference<List<Attack>> robotAttacks;

	private WeakReference<List<Defense>> robotDefends;

	public DataLoaderFileSystemImpl() {

		this(Paths.get("./data"));
	}

	public DataLoaderFileSystemImpl(Path sourceFolder) {

		this.sourceFolder = sourceFolder;
		createPaths();
	}

	private void createPaths() {

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

		userObjectFolder = sourceFolder.resolve("userobjects");
	}

	public void createFolderStructure() throws IOException {

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

		Files.createDirectories(userObjectFolder);
	}

	public Animation readAnimation(Path info) throws JDOMException, IOException {

		String id;
		String relativePathToFile;
		long[] frameTimings;
		int frameWidth;
		int frameHeight;

		Path absolutePath = info.getParent().resolve("animation.png")
				.toAbsolutePath();
		if (!Files.exists(absolutePath)) {

			throw new IOException("Animation not found");
		}

		relativePathToFile = sourceFolder.toAbsolutePath()
				.relativize(absolutePath).toString();

		Document doc = builder.build(Files.newInputStream(info));

		Element animation = doc.getRootElement();

		frameWidth = Integer.valueOf(animation.getChild("width").getValue());
		frameHeight = Integer.valueOf(animation.getChild("height").getValue());
		id = animation.getChild("id").getValue();

		Element timeContainer = animation.getChild("times");
		int containerSize = timeContainer.getAttribute("size").getIntValue();

		List<Element> times = timeContainer.getChildren("time");

		frameTimings = new long[Math.max(times.size(), containerSize)];

		for (Element time : times) {

			frameTimings[time.getAttribute("frame").getIntValue()] = Long
					.valueOf(time.getValue());
		}

		return new Animation(id, relativePathToFile, frameTimings, frameWidth,
				frameHeight);
	}

	public Attack readAttack(Path info, List<Animation> attackAnimations)
			throws JDOMException, IOException {

		RobotActionType type;
		RobotActionPowerType powerType;
		int damage;
		int configurationPointCosts;
		int energyCosts;
		String name;
		long id;
		Animation animation;
		String animationId;
		List<StatusEffect> statusEffects;

		Document doc = builder.build(Files.newInputStream(info));
		Element atk = doc.getRootElement();

		type = RobotActionType.valueOf(atk.getChild("type").getValue());
		powerType = RobotActionPowerType.valueOf(atk.getChild("powertype")
				.getValue());
		damage = Integer.valueOf(atk.getChild("damage").getValue());
		configurationPointCosts = Integer.valueOf(atk.getChild("configcosts")
				.getValue());
		energyCosts = Integer.valueOf(atk.getChild("energycosts").getValue());
		name = atk.getChild("name").getValue();
		id = Long.valueOf(atk.getChild("id").getValue());
		animationId = atk.getChild("animationid").getValue();
		animation = getAnimation(attackAnimations, animationId);
		statusEffects = readEffects(atk);

		Attack attack = new Attack(type, damage);
		attack.setConfigurationPointCosts(configurationPointCosts);
		attack.setEnergyCosts(energyCosts);
		attack.setName(name);
		attack.setAnimation(animation);
		attack.setId(id);
		attack.setStatusEffects(statusEffects);
		attack.setRobotActionPowerType(powerType);

		return attack;
	}

	public Defense readDefense(Path info, List<Animation> defenseAnimations)
			throws JDOMException, IOException {

		RobotActionType type;
		RobotActionPowerType powerType;
		double bonusOnDefenceFactor;
		int configurationPointCosts;
		int energyCosts;
		String name;
		long id;
		Animation animation;
		String animationId;
		List<StatusEffect> statusEffects;

		Document doc = builder.build(Files.newInputStream(info));
		Element def = doc.getRootElement();

		type = RobotActionType.valueOf(def.getChild("type").getValue());
		powerType = RobotActionPowerType.valueOf(def.getChild("powertype")
				.getValue());
		bonusOnDefenceFactor = Double.valueOf(def.getChild("defensefactor")
				.getValue());
		configurationPointCosts = Integer.valueOf(def.getChild("configcosts")
				.getValue());
		energyCosts = Integer.valueOf(def.getChild("energycosts").getValue());
		name = def.getChild("name").getValue();
		id = Long.valueOf(def.getChild("id").getValue());
		animationId = def.getChild("animationid").getValue();
		animation = getAnimation(defenseAnimations, animationId);
		statusEffects = readEffects(def);

		Defense defense = new Defense(type, bonusOnDefenceFactor);
		defense.setConfigurationPointCosts(configurationPointCosts);
		defense.setEnergyCosts(energyCosts);
		defense.setName(name);
		defense.setAnimation(animation);
		defense.setId(id);
		defense.setStatusEffects(statusEffects);
		defense.setRobotActionPowerType(powerType);

		return defense;
	}

	public Robot readRobot(Path info, List<Animation> robotAnimations,
			List<Attack> attacks, List<Defense> defends) throws JDOMException,
			IOException {

		long id;
		String animationId;
		Animation animation;
		String name;
		int configurationPointCosts;
		int energyPoints;
		int healthPoints;
		List<RoboItem> defaultItems;
		List<Attack> defaultAttacks;
		List<Defense> defaultDefends;
		List<StatusEffect> statusEffects;

		Document doc = builder.build(Files.newInputStream(info));
		Element robo = doc.getRootElement();

		id = Long.valueOf(robo.getChild("id").getValue());
		animationId = robo.getChild("animationid").getValue();
		animation = getAnimation(robotAnimations, animationId);
		name = robo.getChild("name").getValue();
		configurationPointCosts = Integer.valueOf(robo.getChild("configcosts")
				.getValue());
		energyPoints = Integer
				.valueOf(robo.getChild("energypoints").getValue());
		healthPoints = Integer
				.valueOf(robo.getChild("healthpoints").getValue());
		statusEffects = readEffects(robo);

		List<Element> defItems = robo.getChild("defaultitems").getChildren(
				"item");
		defaultItems = new ArrayList<RoboItem>(defItems.size() + 10);
		for (Element it : defItems) {

			defaultItems.add(getNotRemoveableItemById(Long.valueOf(it
					.getValue())));
		}

		List<Element> defAtks = robo.getChild("attacks").getChildren("id");
		defaultAttacks = new ArrayList<Attack>(defAtks.size());
		for (Element it : defAtks) {

			defaultAttacks.add(getAttack(attacks,
					Integer.valueOf(it.getValue())));
		}

		List<Element> defDefs = robo.getChild("defends").getChildren("id");
		defaultDefends = new ArrayList<Defense>(defDefs.size());
		for (Element it : defDefs) {

			defaultDefends.add(getDefense(defends,
					Integer.valueOf(it.getValue())));
		}

		Robot robot = new Robot();
		robot.setId(id);
		robot.setAnimation(animation);
		robot.setName(name);
		robot.setConfigurationPointCosts(configurationPointCosts);

		robot.setMaxEnergyPoints(energyPoints);
		robot.setEnergyPoints(energyPoints);

		robot.setMaxHealthPoints(healthPoints);
		robot.setHealthPoints(healthPoints);

		robot.setEquippedItems(defaultItems);
		robot.setPossibleAttacks(defaultAttacks);
		robot.setPossibleDefends(defaultDefends);
		robot.setLoadedAsUserRobot(false);

		robot.setStatusEffects(statusEffects);

		return robot;
	}

	private List<StatusEffect> readEffects(Element obj) {

		List<StatusEffect> ef = new LinkedList<StatusEffect>();

		Element efs = obj.getChild("statuseffects");
		if (efs == null) {
			return ef;
		}

		List<Element> ids = efs.getChildren("id");
		for (Element id : ids) {

			Integer efId = Integer.valueOf(id.getValue());
			StatusEffect efClone = StatusEffectUtil.cloneStatusEffectById(efId);

			if (efClone == null) {
				continue;
			}

			ef.add(efClone);
		}

		return ef;
	}

	private Attack getAttack(List<Attack> attacks, Integer id) {

		for (Attack atk : attacks) {

			if (atk.getId() == id) {

				return atk;
			}
		}

		throw new RuntimeException("Attacke nicht gefunden -> " + id);
	}

	private Defense getDefense(List<Defense> defense, Integer id) {

		for (Defense def : defense) {

			if (def.getId() == id) {

				return def;
			}
		}

		throw new RuntimeException("Attacke nicht gefunden");
	}

	private RoboItem getNotRemoveableItemById(Long itemId) {

		RoboItem item = ItemUtil.cloneItemById(itemId);

		if (item == null) {

			throw new RuntimeException(String.format("Item %d nicht gefunden.",
					itemId));
		}

		item.setRemoveable(false);

		return item;
	}

	private Animation getAnimation(List<Animation> animations,
			String animationId) {

		for (Animation ani : animations) {

			if (ani.getId().equals(animationId)) {

				return ani;
			}
		}

		throw new RuntimeException("Keine Animtaion für " + animationId
				+ " gefunden");
	}

	private List<Animation> loadDefAnimations() {
		return loadAnimationsFromFolder(defAniFolder);
	}

	private List<Animation> loadAtkAnimations() {
		return loadAnimationsFromFolder(atkAniFolder);
	}

	private List<Animation> loadAnimationsFromFolder(Path folder) {

		List<Animation> anis = new LinkedList<Animation>();

		try {
			DirectoryStream<Path> dirs = Files.newDirectoryStream(folder);

			for (Path dir : dirs) {

				try {
					Animation animation = readAnimation(dir.resolve("info.xml"));

					if (animation == null) {
						LOG.debug("found null animation at: {}", dir.toString());
						continue;
					}

					anis.add(animation);
				} catch (Exception e) {

					LOG.debug("Animation skipped -> {}", e.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return anis;
	}

	@Override
	public List<Animation> loadAnimationsForRobots() {

		List<Animation> robotAnimations;

		if (this.robotAnimations != null
				&& (robotAnimations = this.robotAnimations.get()) != null) {

			return robotAnimations;
		}

		robotAnimations = loadAnimationsFromFolder(robotAniFolder);
		this.robotAnimations = new WeakReference<List<Animation>>(
				robotAnimations);

		return robotAnimations;
	}

	@Override
	public List<Animation> loadAnimationsForRobotActions() {

		List<Animation> actionAnis;

		if (this.robotActionAnimations != null
				&& (actionAnis = this.robotActionAnimations.get()) != null) {

			return actionAnis;
		}

		List<Animation> anis = new LinkedList<Animation>();

		anis.addAll(loadAtkAnimations());
		anis.addAll(loadDefAnimations());

		robotActionAnimations = new WeakReference<List<Animation>>(anis);

		return anis;
	}

	@Override
	public List<Robot> loadRobots() {

		List<Robot> robos;

		if (this.systemRobots != null
				&& (robos = this.systemRobots.get()) != null) {

			return robos;
		}

		List<Animation> roboAnis = loadAnimationsForRobots();
		List<Attack> robotAttacks = loadRobotAttacks();
		List<Defense> robotDefends = loadRobotDefends();

		robos = new LinkedList<Robot>();

		try {
			DirectoryStream<Path> objs = Files.newDirectoryStream(robotFolder);

			for (Path obj : objs) {

				try {
					Robot robot = readRobot(obj.resolve("info.xml"), roboAnis,
							robotAttacks, robotDefends);

					if (robot == null) {
						LOG.debug("found null robot at: {}", obj.toString());
						continue;
					}

					robos.add(robot);
				} catch (Exception e) {

					LOG.debug("Robot skipped -> {}", e.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		systemRobots = new WeakReference<List<Robot>>(robos);

		return robos;
	}

	@Override
	public List<Robot> loadUserRobots(String userId) {

		List<Robot> robos;

		if (this.userRobots != null && (robos = this.userRobots.get()) != null) {

			return robos;
		}

		List<Animation> roboAnis = loadAnimationsForRobots();
		List<Attack> robotAttacks = loadRobotAttacks();
		List<Defense> robotDefends = loadRobotDefends();

		robos = new LinkedList<Robot>();

		try {
			DirectoryStream<Path> objs = Files
					.newDirectoryStream(userObjectFolder.resolve(userId)
							.resolve("robots"));

			for (Path obj : objs) {

				try {
					Robot userRobot = readRobot(obj.resolve("info.xml"),
							roboAnis, robotAttacks, robotDefends);

					if (userRobot == null) {
						LOG.debug("found null custom robot at: {}",
								obj.toString());
						continue;
					}

					userRobot.setLoadedAsUserRobot(true);
					robos.add(userRobot);
				} catch (Exception e) {
					LOG.debug("User Robot skipped -> {}", e.getMessage());
				}
			}
		} catch (NoSuchFileException e) {
			// Noch keine UserRoboter
			LOG.debug("No custom bots available.");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		userRobots = new WeakReference<List<Robot>>(robos);

		return robos;
	}

	@Override
	public long createUserRobot(Robot robot, String userId) throws IOException,
			JDOMException {

		if (robot.isLoadedAsUserRobot()) {
			throw new RuntimeException(
					"Robot wurde bereits als userRobot geladen.");
		}

		List<Long> usedIds = getUsedIdsOf(userId);
		long id = -1;

		while (usedIds.contains(id)) {

			id--;
		}

		Path robotFolder = userObjectFolder.resolve(userId).resolve("robots")
				.resolve(UUID.randomUUID().toString());

		Files.createDirectories(robotFolder);

		Path roboFile = robotFolder.resolve("info.xml");

		Element root = new Element("robot");
		Document doc = new Document(root);

		root.addContent(new Element("id").setText("" + id));
		root.addContent(new Element("name").setText((robot.getNickname()
				.isEmpty()) ? (robot.getName() + System.nanoTime()) : robot
				.getNickname()));
		root.addContent(new Element("configcosts").setText(""
				+ robot.getConfigurationPointCosts()));
		root.addContent(new Element("healthpoints").setText(""
				+ robot.getHealthPoints()));
		root.addContent(new Element("energypoints").setText(""
				+ robot.getEnergyPoints()));
		root.addContent(new Element("animationid").setText(""
				+ robot.getAnimation().getId()));

		Element defaultItems = new Element("defaultitems");
		for (RoboItem items : robot.getEquippedItems()) {

			defaultItems.addContent(new Element("item").setText(""
					+ items.getId()));
		}
		root.addContent(defaultItems);

		Element atks = new Element("attacks");
		for (Attack atk : robot.getPossibleAttacks()) {

			atks.addContent(new Element("id").setText("" + atk.getId()));
		}
		root.addContent(atks);

		Element defs = new Element("defends");
		for (Defense def : robot.getPossibleDefends()) {

			defs.addContent(new Element("id").setText("" + def.getId()));
		}
		root.addContent(defs);

		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat());
		output.output(doc, Files.newOutputStream(roboFile));

		return id;
	}

	private List<Long> getUsedIdsOf(String userId) {

		List<Long> usedIds = new LinkedList<Long>();

		try {
			DirectoryStream<Path> objs = Files
					.newDirectoryStream(userObjectFolder.resolve(userId)
							.resolve("robots"));

			for (Path obj : objs) {

				Document doc = builder.build(Files.newInputStream(obj
						.resolve("info.xml")));
				Element atk = doc.getRootElement();

				long id = Long.valueOf(atk.getChild("id").getValue());
				usedIds.add(id);
			}
		} catch (NoSuchFileException e) {
			// Noch keine User Robots -> keine ids

			return usedIds;
		} catch (IOException | JDOMException e) {
			throw new RuntimeException(e);
		}

		return usedIds;
	}

	@Override
	public List<Attack> loadRobotAttacks() {

		List<Attack> attacks;

		if (this.robotAttacks != null
				&& (attacks = this.robotAttacks.get()) != null) {

			return attacks;
		}

		List<Animation> atkAnimations = loadAtkAnimations();
		attacks = new LinkedList<Attack>();

		try {
			DirectoryStream<Path> objs = Files.newDirectoryStream(atkFolder);

			for (Path obj : objs) {

				try {
					Attack attack = readAttack(obj.resolve("info.xml"),
							atkAnimations);

					if (attack == null) {

						LOG.debug("found null attack at: {}", obj.toString());
						continue;
					}

					attacks.add(attack);
				} catch (Exception e) {

					LOG.debug("Skipped attack -> {}", e.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		robotAttacks = new WeakReference<List<Attack>>(attacks);

		return attacks;
	}

	@Override
	public List<Defense> loadRobotDefends() {

		List<Defense> defends;

		if (this.robotDefends != null
				&& (defends = this.robotDefends.get()) != null) {

			return defends;
		}

		List<Animation> defAnimations = loadDefAnimations();
		defends = new LinkedList<Defense>();

		try {
			DirectoryStream<Path> objs = Files.newDirectoryStream(defFolder);

			for (Path obj : objs) {

				try {
					Defense defense = readDefense(obj.resolve("info.xml"),
							defAnimations);

					if (defense == null) {

						LOG.debug("found null defense at: {}", obj.toString());
						continue;
					}

					defends.add(defense);
				} catch (Exception e) {
					LOG.debug("Defense skipped -> {}", e.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		robotDefends = new WeakReference<List<Defense>>(defends);

		return defends;
	}

	public AnimatedSceneObject createAnimatedSceneObject(Animation ani)
			throws IOException {

		System.out.println("Create Ani Obj -> " + ani.getPathToFile());

		BufferedImage img = animations.get(ani.getId());

		// Buffer loaded animations
		if (img == null) {

			img = ImageIO.read(sourceFolder.resolve(ani.getPathToFile())
					.toFile());
			animations.put(ani.getId(), img);
		}

		long[] timings = ani.getFrameTimings();

		Sprite sprite = new DefaultSprite(img, ani.getFrameWidth(),
				ani.getFrameHeight());
		long[][] time = new long[][] { timings };

		return new AnimatedSceneObject(sprite, time[0][0], time);
	}

	public void generateIdList() throws IOException {

		OutputStream out = Files.newOutputStream(sourceFolder
				.resolve("IdList.txt"));

		generateIdList(out);

		out.flush();
		out.close();
	}

	/**
	 * Erstellt eine List mit allen genutzten Ids
	 * 
	 * @throws IOException
	 */
	public void generateIdList(OutputStream outS) throws IOException {

		Set<String> ids = new HashSet<String>();

		OutputStreamWriter out = new OutputStreamWriter(outS,
				Charset.forName("UTF-8"));

		out.write("Attacken - Animationen\n");
		ids.addAll(generateIdsFromFolder(out, atkAniFolder));

		out.write("\n\nVerteidigungen - Animationen\n");
		ids.addAll(generateIdsFromFolder(out, defAniFolder));

		out.write("\n\nRoboter - Animationen\n");
		ids.addAll(generateIdsFromFolder(out, robotAniFolder));

		out.write("\n\nAttacken - Objekte\n");
		ids.addAll(generateIdsFromFolder(out, atkFolder));

		out.write("\n\nVerteidigungen - Objekte\n");
		ids.addAll(generateIdsFromFolder(out, defFolder));

		out.write("\n\nRoboter - Objekte\n");
		ids.addAll(generateIdsFromFolder(out, robotFolder));

		// Alle ids noch einmal sortiert ausgeben
		ArrayList<String> sortedIds = new ArrayList<String>(ids);
		Collections.sort(sortedIds, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {

				try {
					return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
				} catch (NumberFormatException e) {
				}

				return s1.compareTo(s2);
			}
		});
		out.write("\n\nAlle Ids\n");
		for (String id : sortedIds) {
			out.write(String.format("%s\n", id));
		}

		out.flush();
	}

	private Set<String> generateIdsFromFolder(OutputStreamWriter out,
			Path folder) throws IOException {

		Set<String> ids = new HashSet<String>();

		try {
			DirectoryStream<Path> dirs = Files.newDirectoryStream(folder);

			for (Path dir : dirs) {

				try {

					Document doc = builder.build(Files.newInputStream(dir
							.resolve("info.xml")));

					Element info = doc.getRootElement();

					String name = Null.nvl(info.getChildText("name"), dir
							.getFileName().toString());

					String id = Null.nvl(info.getChildText("id"), "<Unknown>");

					ids.add(id);

					if (out != null) {

						out.write(String.format("%s -> %s\n", name, id));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (out != null) {

			out.flush();
		}

		return ids;
	}

	public static void main(String[] args) {

		DataLoaderFileSystemImpl loader = new DataLoaderFileSystemImpl(
				Paths.get("../data/"));

		try {
			loader.generateIdList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
