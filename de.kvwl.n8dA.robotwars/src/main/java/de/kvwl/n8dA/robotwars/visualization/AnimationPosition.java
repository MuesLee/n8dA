package de.kvwl.n8dA.robotwars.visualization;

// Welche Animation wird für welchen Roboter ausgeführt
public class AnimationPosition
{

	private String animationID;

	private RobotPosition position;

	public AnimationPosition(String animationID, RobotPosition position)
	{
		super();
		this.animationID = animationID;
		this.position = position;
	}

	public String getAnimationID()
	{
		return animationID;
	}

	public void setAnimationID(String animationID)
	{
		this.animationID = animationID;
	}

	/**
	 * Get the position of the Robot which starts the animation
	 * 
	 * @return
	 */
	public RobotPosition getPosition()
	{
		return position;
	}

	public void setPosition(RobotPosition position)
	{
		this.position = position;
	}

}
