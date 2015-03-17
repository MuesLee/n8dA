package de.kvwl.n8dA.robotwars.commons.gui;

public class Animation
{

	private String id;

	//Muss mit new File() lesbar sein
	private String pathToFile;

	//Sind nanosekunden
	private long[] frameTimings;

	//Dimesnion eines Bildes in der Animation
	private int frameWidth;
	private int frameHeight;

	public Animation(String id, String pathToFile, long[] frameTimings, int frameWidth, int frameHeight)
	{
		super();
		this.id = id;
		this.pathToFile = pathToFile;
		this.frameTimings = frameTimings;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
	}

	public String getPathToFile()
	{
		return pathToFile;
	}

	public void setPathToFile(String pathToFile)
	{
		this.pathToFile = pathToFile;
	}

	public long[] getFrameTimings()
	{
		return frameTimings;
	}

	public void setFrameTimings(long[] frameTimings)
	{
		this.frameTimings = frameTimings;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getFrameWidth()
	{
		return frameWidth;
	}

	public void setFrameWidth(int frameWidth)
	{
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight()
	{
		return frameHeight;
	}

	public void setFrameHeight(int frameHeight)
	{
		this.frameHeight = frameHeight;
	}

}
