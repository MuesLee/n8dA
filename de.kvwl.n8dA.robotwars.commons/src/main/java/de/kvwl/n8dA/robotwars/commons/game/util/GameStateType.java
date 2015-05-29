package de.kvwl.n8dA.robotwars.commons.game.util;

public enum GameStateType {
	
	GAME_IS_ENDING(10), VICTORY_LEFT(9), VICTORY_RIGHT(9), DRAW(9), BATTLE_IS_ACTIVE(8), WAITING_FOR_PLAYER_INPUT(7), GAME_HAS_BEGUN(6), GAME_HASNT_BEGUN(1), SERVER_BUSY(0);

	private int index;
	
	public static String getNotificationName() {
		return "GAME_STATE_INFO";
	}

	private GameStateType(int index) {
		this.setIndex(index);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	

}
