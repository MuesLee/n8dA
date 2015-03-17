package de.kvwl.n8dA.robotwars.commons.game.util;

public enum GameStateType {
	
	VICTORY_LEFT, VICTORY_RIGHT, DRAW, BATTLE_IS_ACTIVE, WAITING_FOR_PLAYER_INPUT, GAME_HAS_BEGUN, GAME_HASNT_BEGUN;

	public static String getNotificationName() {
		// TODO Auto-generated method stub
		return "GAME_STATE_INFO";
	}

}