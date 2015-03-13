package de.kvwl.n8dA.robotwars.commons.gui;

public class Animation {
	
	private String id;
	private String pathToFile;
	private int[] frameTimings;
	
	public Animation(String id, String pathToFile, int[] frameTimings) {
		super();
		this.id = id;
		this.pathToFile = pathToFile;
		this.frameTimings = frameTimings;
	}
	public String getPathToFile() {
		return pathToFile;
	}
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}
	public int[] getFrameTimings() {
		return frameTimings;
	}
	public void setFrameTimings(int[] frameTimings) {
		this.frameTimings = frameTimings;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

}
