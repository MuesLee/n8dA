package de.kvwl.n8dA.infrastructure.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import de.kvwl.n8dA.infrastructure.rewards.gui.HighscoreListVisualizerImpl;

public class HighscoreController {
	private static final Logger LOG = LoggerFactory
			.getLogger(HighscoreController.class);

	private static final int _LIST_SWITCH_RATE_IN_MS = 10000;

	private static int CURRENT_LIST_INDEX = 0;

	private List<Game> games;

	Timer timer;
	BasicCreditAccess server;
	HighscoreListVisualizer visualizer;

	public HighscoreController(BasicCreditAccess server) {
		this.server = server;
		init();

	}

	private void init() {
		games = new ArrayList<>();
		timer = new Timer("HighscoreListSwitch-Timer");

		refreshGameList();

		HighscoreListVisualizerImpl visualizer = new HighscoreListVisualizerImpl();
		visualizer.setVisible(true);
		this.visualizer = visualizer;
	}

	public void showHighscoreView() {
		startTimer();
	}

	private void startTimer() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				switchHighscoreList();
			}

		};

		timer.schedule(task, 0, _LIST_SWITCH_RATE_IN_MS);
	}

	void switchHighscoreList() {
		Game game = games.get(CURRENT_LIST_INDEX);
		String gameName = game.getName();

		try {
			server.getAllGamePersonsForGame(gameName);
		} catch (RemoteException e) {
			LOG.error("GamePersonList für " + gameName
					+ " konnte nicht vom Server abgerufen werden", e);
		}
	}

	private void refreshGameList() {
		try {
			games = server.getAllGames();
		} catch (RemoteException e) {
			LOG.error("GameList konnte vom Server nicht abgerufen werden", e);
		}
	}

	List<HighscoreEntry> createHighscoreList(
			List<GamePerson> gamePersons) {
		List<HighscoreEntry> highscoreEntries = new ArrayList<>();

		Map<String, Integer> aggregatedPoints = aggregateGamePersonsPoints(gamePersons);
		
		Set<String> persons = aggregatedPoints.keySet();
		for (String person : persons) {
			
			int points = aggregatedPoints.get(person);
			HighscoreEntry highscoreEntry = new HighscoreEntry(person, points);
			highscoreEntries.add(highscoreEntry);
		}

		Collections.sort(highscoreEntries);
		
		return highscoreEntries;
	}
	
	private Map<String, Integer> aggregateGamePersonsPoints(List<GamePerson> gamePersons)
	{
		Map<String, Integer> result = new HashMap<>();
		
		for (GamePerson gamePerson : gamePersons) {
			String personName = gamePerson.getPerson().getName();
			Integer gamePoints = gamePerson.getPoints();
			
			if(result.containsKey(personName))
			{
				Integer storedGamePoints = result.get(personName);
				gamePoints += storedGamePoints;
			}
			
			result.put(personName, gamePoints);
		}
		
		return result;
	}
}
