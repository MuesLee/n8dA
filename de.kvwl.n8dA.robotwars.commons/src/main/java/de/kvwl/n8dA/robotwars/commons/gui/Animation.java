package de.kvwl.n8dA.robotwars.commons.gui;

import java.io.Serializable;
import java.util.Arrays;

public class Animation implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7579511131523102751L;

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
	
	@Override
	public String toString() {
		return "Animation ID: " + id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frameHeight;
		result = prime * result + Arrays.hashCode(frameTimings);
		result = prime * result + frameWidth;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((pathToFile == null) ? 0 : pathToFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animation other = (Animation) obj;
		if (frameHeight != other.frameHeight)
			return false;
		if (!Arrays.equals(frameTimings, other.frameTimings))
			return false;
		if (frameWidth != other.frameWidth)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pathToFile == null) {
			if (other.pathToFile != null)
				return false;
		} else if (!pathToFile.equals(other.pathToFile))
			return false;
		return true;
	}
	
	

}
