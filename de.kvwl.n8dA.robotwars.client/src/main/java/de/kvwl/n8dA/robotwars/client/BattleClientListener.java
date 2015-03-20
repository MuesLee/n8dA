package de.kvwl.n8dA.robotwars.client;

import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;

public interface BattleClientListener
{

	public void startActionSelection();

	public void gameOver(GameStateType result);
}