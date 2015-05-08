package de.kvwl.n8dA.robotwars.server.visualization.java;

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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((animation == null) ? 0 : animation.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
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
		AnimationPosition other = (AnimationPosition) obj;
		if (animation == null) {
			if (other.animation != null)
				return false;
		} else if (!animation.equals(other.animation))
			return false;
		if (position != other.position)
			return false;
		return true;
	}
	
	

}
