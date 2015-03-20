package de.kvwl.n8dA.robotwars.server.visualization;

import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

// Welche Animation wird für welchen Roboter ausgeführt
public class AnimationPosition
{

	private Animation animation;

	private RobotPosition position;

	public AnimationPosition(Animation animation, RobotPosition position)
	{
		super();
		this.setAnimation(animation);
		this.position = position;
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

	@Override
	public String toString()
	{
		return "(" + animation + " , " + position + ")";
	}



	public Animation getAnimation() {
		return animation;
	}



	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

}
