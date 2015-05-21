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

	private static int CURRENT_LIST_INDEX = -1;

	private List<Game> games;

	private Timer timer;
	private BasicCreditAccess server;
	private HighscoreListVisualizer visualizer;

	public HighscoreController(BasicCreditAccess server) {
		this.server = server;
		init();

	}

	private void init() {
		games = new ArrayList<>();
		timer = new Timer("HighscoreListSwitch-Timer");

		refreshGameList();

	}

	public void showHighscoreView() {
		HighscoreListVisualizerImpl visualizer = new HighscoreListVisualizerImpl();
		visualizer.setVisible(true);
		this.visualizer = visualizer;
		switchHighscoreList();
		startTimer();
	}

	private void startTimer() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				switchHighscoreList();
			}

		};

		timer.schedule(task, _LIST_SWITCH_RATE_IN_MS, _LIST_SWITCH_RATE_IN_MS);
	}

	void switchHighscoreList() {
		
		LOG.debug("Wechsle HighScoreList...");
		
		String listTitle = "Kaputt";
		int gameListSize = games.size();

		try {
			List<GamePerson> gamePersons;

			if (CURRENT_LIST_INDEX == -1 || CURRENT_LIST_INDEX >= gameListSize) {
				gamePersons = server.getFirst10GamePersons();
				listTitle = "Gesamtpunktzahl";
			} else {
				Game game = games.get(CURRENT_LIST_INDEX);
				listTitle = game.getName();
				gamePersons = server.getFirst10GamePersonsForGame(listTitle);
			}

			List<HighscoreEntry> highscoreList = createHighscoreList(gamePersons);
			
			LOG.debug("... zeige HighscoreListe für: " + listTitle);
			
			visualizer.showHighscoreList(listTitle, highscoreList);

		} catch (RemoteException e) {
			LOG.error("GamePersonList für " + listTitle
					+ " konnte nicht vom Server abgerufen werden", e);
		}

		if (CURRENT_LIST_INDEX == (gameListSize - 1) || CURRENT_LIST_INDEX >=gameListSize) {
			CURRENT_LIST_INDEX = -1;
		} else {
			CURRENT_LIST_INDEX++;
		}
	}

	private void refreshGameList() {
		try {
			games = server.getAllGames();
		} catch (RemoteException e) {
			LOG.error("GameList konnte vom Server nicht abgerufen werden", e);
		}
	}

	List<HighscoreEntry> createHighscoreList(List<GamePerson> gamePersons) {

		if (gamePersons == null || gamePersons.isEmpty()) {
			return Collections.emptyList();
		}
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

	private Map<String, Integer> aggregateGamePersonsPoints(
			List<GamePerson> gamePersons) {
		Map<String, Integer> result = new HashMap<>();

		for (GamePerson gamePerson : gamePersons) {
			String personName = gamePerson.getPerson().getName();
			Integer gamePoints = gamePerson.getPoints();

			if (result.containsKey(personName)) {
				Integer storedGamePoints = result.get(personName);
				gamePoints += storedGamePoints;
			}

			result.put(personName, gamePoints);
		}

		return result;
	}
}
