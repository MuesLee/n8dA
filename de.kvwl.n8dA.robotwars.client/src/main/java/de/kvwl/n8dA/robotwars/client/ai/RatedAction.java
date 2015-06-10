package de.kvwl.n8dA.robotwars.client.ai;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;

public class RatedAction implements Comparable<RatedAction> {

	private int rating;
	private RobotAction action;
	public RatedAction(int rating, RobotAction action) {
		super();
		this.rating = rating;
		this.action = action;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public RobotAction getAction() {
		return action;
	}
	public void setAction(RobotAction action) {
		this.action = action;
	}
	@Override
	public int compareTo(RatedAction o) {
		
		if(this.rating > o.getRating())
		{
			return 1;
		}
		else if (this.rating == o.getRating()) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	
}
