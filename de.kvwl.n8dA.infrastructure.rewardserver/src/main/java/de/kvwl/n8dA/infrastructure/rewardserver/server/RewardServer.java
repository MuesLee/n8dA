package de.kvwl.n8dA.infrastructure.rewardserver.server;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.Person;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.commons.util.NetworkUtils;
import de.kvwl.n8dA.infrastructure.controller.HighscoreController;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.GameDaoHSQL;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.GamePersonDaoHSQL;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.PersonDaoHSQL;

public class RewardServer extends UnicastRemoteObject implements
		BasicCreditAccess {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(RewardServer.class);

	private PersonDaoHSQL personDao;
	private GamePersonDaoHSQL gamePersonDao;
	private GameDaoHSQL gameDao;
	private BrokerService broker;

	private static String REWARD_SERVER_FULL_TCP_ADDRESS;

	private static String REWARD_SERVER_REGISTRY_PORT;

	private HighscoreController highscoreController;

	protected RewardServer() throws RemoteException {
		this(false);
	}

	protected RewardServer(boolean installSecurityManager)
			throws RemoteException {

		if (installSecurityManager) {
			installSecurityManager();
		}
	}

	private void installSecurityManager() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
	}

	public static void main(String[] args) {
		RewardServer rewardServer;
		try {
			BasicConfigurator.configure();

			REWARD_SERVER_FULL_TCP_ADDRESS = JOptionPane.showInputDialog(null,
					"Bitte die vollständige TCP-Adresse des Servers eingeben!",
					NetworkUtils.REWARD_SERVER_DEFAULT_FULL_TCP_ADRESS);
			REWARD_SERVER_REGISTRY_PORT = JOptionPane.showInputDialog(null,
					"Bitte den Registry-Port eingeben!",
					NetworkUtils.REWARD_SERVER_DEFAULT_REGISTRY_PORT);

			rewardServer = new RewardServer();
			rewardServer.startServer(Integer
					.parseInt(REWARD_SERVER_REGISTRY_PORT));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startServer(int port) {
		try {
			personDao = new PersonDaoHSQL();
			gamePersonDao = new GamePersonDaoHSQL();
			gameDao = new GameDaoHSQL();
			startActiveMQBroker();

			Registry registry = LocateRegistry.getRegistry(port);
			if (registry == null) {
				LocateRegistry.createRegistry(port);
			}
			highscoreController = new HighscoreController(this);
			highscoreController.showHighscoreView();
		}

		catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
		try {
			Naming.rebind(NetworkUtils.REWARD_SERVER_NAME, this);
			LOG.info("##### " + NetworkUtils.REWARD_SERVER_NAME
					+ " STARTED ####");
		} catch (MalformedURLException ex) {
			LOG.error(ex.getMessage());
		} catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
	}

	private void startActiveMQBroker() {
		LOG.info("Starting ActiveMQ Broker");

		try {
			broker = new BrokerService();
			KahaDBPersistenceAdapter adaptor = new KahaDBPersistenceAdapter();
			adaptor.setDirectory(new File("activemq"));
			broker.setPersistenceAdapter(adaptor);
			broker.setUseJmx(true);
			broker.addConnector(REWARD_SERVER_FULL_TCP_ADDRESS);
			broker.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private void stopActiveMQBroker() {
		try {
			broker.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException {

		LOG.info("ConfigPoints requested for " + name);

		List<GamePerson> findAllGamesByPersonName = gamePersonDao
				.findAllGamesByPersonName(name);

		int points = 0;
		for (GamePerson gamePerson : findAllGamesByPersonName) {
			points += gamePerson.getPoints();
		}

		LOG.info(points + " ConfigPoints returned for " + name);

		return points;
	}

	@Override
	public List<GamePerson> getAllGamesForPersonName(String personName) {
		LOG.info("All Games requested for " + personName);

		List<GamePerson> findAllGamesByPersonName = gamePersonDao
				.findAllGamesByPersonName(personName);

		LOG.info(findAllGamesByPersonName + " returned for " + personName);

		return findAllGamesByPersonName;
	}

	@Override
	public void persistConfigurationPointsForPerson(String personName,
			String gameName, int points) throws RemoteException {

		LOG.info("Persist request for: " + personName + ". With " + points
				+ " points for game: " + gameName);

		GamePerson findPersonInGame = gamePersonDao.findPersonInGame(gameName,
				personName);

		if (findPersonInGame == null) {
			LOG.info("Person: " + personName + " has not played " + gameName
					+ " before. Creating new game entry");

			Game game = gameDao.findById(gameName);
			if (game == null) {
				LOG.info("It is: " + gameName
						+ " first play ever . Creating new game entry");
				game = new Game(gameName);
				gameDao.add(game);
				highscoreController.refreshGameList();
			}
			Person person = personDao.findById(personName);

			if (person == null) {
				LOG.info("It is persons: " + personName
						+ " first game ever . Creating new person entry");
				person = new Person(personName);
			}

			gamePersonDao.add(new GamePerson(game, person, points));

			LOG.info("Persisted person: " + personName + ". With " + points
					+ " points for game: " + gameName);
		} else {

			if (!gameName.equals("RoboBattle")) {

				Integer oldPoints = findPersonInGame.getPoints();
				if (oldPoints >= points) {
					LOG.info("Person: " + personName + ". Already had "
							+ oldPoints + " points for game: " + gameName
							+ ". No update needed ");
				} else {
					findPersonInGame.setPoints(points);
					gamePersonDao.update(findPersonInGame);
					LOG.info("Person: " + personName + ". Had " + oldPoints
							+ " points for game: " + gameName + ". Now has "
							+ points);
				}
			} else {
				findPersonInGame.setPoints(points);
				gamePersonDao.update(findPersonInGame);
				LOG.info("Person: " + personName + " has now " + points
						+ " in " + gameName);
			}
		}
	}

	@Override
	public List<Game> getAllGames() throws RemoteException {

		return gameDao.findAll();
	}

	@Override
	public List<GamePerson> getAllGamePersonsForGame(String gameName)
			throws RemoteException {
		List<GamePerson> findAllPersonsForGameName = gamePersonDao
				.findAllPersonsForGameName(gameName);

		return findAllPersonsForGameName;
	}

	@Override
	public List<GamePerson> getAllGamePersons() throws RemoteException {
		return gamePersonDao.findAllGamePersons();
	}

	@Override
	public List<GamePerson> getFirst10GamePersonsForGame(String gameName)
			throws RemoteException {
		return gamePersonDao.findFirst10PersonsForGameName(gameName);
	}

	@Override
	public int getGamePointsForPerson(String person, String name)
			throws RemoteException, NoSuchPersonException {
		// TODO Auto-generated method stub
		GamePerson findGamePersonForPersonAndGame = gamePersonDao
				.findGamePersonForPersonAndGame(person, name);
		return findGamePersonForPersonAndGame != null ? findGamePersonForPersonAndGame
				.getPoints() : 0;
	}

	@Override
	public List<Person> getAllPersons() throws RemoteException {
		return personDao.findAll();
	}

	@Override
	public void createGame(String name) throws RemoteException {
		
		Game game = new Game(name);
		
		gameDao.add(game);
	}

	@Override
	public void deleteGame(String name) throws RemoteException {
		
		Game findById = gameDao.findById(name);
		if(findById != null)
		{
			gameDao.delete(findById);
		}
	}

	@Override
	public void clearGame(String name) throws RemoteException {
		List<GamePerson> findAllPersonsForGameName = gamePersonDao.findAllPersonsForGameName(name);
		
		for (GamePerson gamePerson : findAllPersonsForGameName) {
			gamePersonDao.delete(gamePerson);
		}
	}

	@Override
	public void deletePerson(String name) {
		
		Person findById = personDao.findById(name);
		if(findById != null)
		{
			personDao.delete(findById);
		}
	}

	@Override
	public void overwriteRecord(String personName, String gameName, int points)
			throws RemoteException {
		GamePerson findGamePersonForPersonAndGame = gamePersonDao.findGamePersonForPersonAndGame(personName, gameName);
		if(findGamePersonForPersonAndGame!= null)
		{
			findGamePersonForPersonAndGame.setPoints(points);
			gamePersonDao.update(findGamePersonForPersonAndGame);
		}
	}
}
