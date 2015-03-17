package de.kvwl.n8dA.robotwars.server.input;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

//TODO Marvin: Animation loader
/**
 * 
 * Lädt die Animationen aus eienr Verzeichnisstruktur.Darunter existieren die Ordner [animations],
 * [actions] und [robots]. Jede Animation besteht darunter wieder aus einem Ordner, der die Dateien
 * [info.xml] und [animation.png]
 */
public class DataLoaderFileSystemImpl implements DataLoader
{

	private SAXBuilder builder = new SAXBuilder();

	private Path sourceFolder;
	private Path robotAniFolder;
	private Path actionAniFolder;
	private Path atkAniFolder;
	private Path defAniFolder;

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

		robotAniFolder = sourceFolder.resolve("robots");
		actionAniFolder = sourceFolder.resolve("actions");
		atkAniFolder = actionAniFolder.resolve("attacks");
		defAniFolder = actionAniFolder.resolve("defends");
	}

	public void createFolderStructure() throws IOException
	{

		Files.createDirectories(robotAniFolder);
		Files.createDirectories(actionAniFolder);
		Files.createDirectories(atkAniFolder);
		Files.createDirectories(defAniFolder);
	}

	public Animation readAnimation(Path info) throws JDOMException, IOException
	{

		String id;
		String pathToFile;
		long[] frameTimings;
		int frameWidth;
		int frameHeight;

		pathToFile = info.getParent().resolve("animation.png").toAbsolutePath().toString();

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

		return new Animation(id, pathToFile, frameTimings, frameWidth, frameHeight);
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

		anis.addAll(loadAnimationsFromFolder(atkAniFolder));
		anis.addAll(loadAnimationsFromFolder(defAniFolder));

		return anis;
	}

	@Override
	public List<Robot> loadRobots()
	{
		return null;
	}

	@Override
	public List<Attack> loadRobotAttacks()
	{
		return null;
	}

	@Override
	public List<Defense> loadRobotDefends()
	{
		return null;
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

}
