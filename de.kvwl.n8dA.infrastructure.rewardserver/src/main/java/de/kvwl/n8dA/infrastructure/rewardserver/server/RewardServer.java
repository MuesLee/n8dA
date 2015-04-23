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
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccesHandler;
import de.kvwl.n8dA.infrastructure.commons.util.NetworkUtils;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.GameDaoHSQL;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.GamePersonDaoHSQL;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.PersonDaoHSQL;

public class RewardServer extends UnicastRemoteObject implements CreditAccesHandler
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RewardServer.class);

	private PersonDaoHSQL personDao;
	private GamePersonDaoHSQL gamePersonDao;
	private GameDaoHSQL gameDao;
	private BrokerService broker;

	private static String REWARD_SERVER_FULL_TCP_ADDRESS;

	private static String REWARD_SERVER_REGISTRY_PORT;

	protected RewardServer() throws RemoteException
	{

		this(false);
	}

	protected RewardServer(boolean installSecurityManager) throws RemoteException
	{

		if (installSecurityManager)
		{
			installSecurityManager();
		}
	}

	private void installSecurityManager()
	{
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}
	}

	public static void main(String[] args)
	{
		RewardServer rewardServer;
		try
		{
			BasicConfigurator.configure();

			REWARD_SERVER_FULL_TCP_ADDRESS = JOptionPane.showInputDialog(null,
				"Bitte die vollständige TCP-Adresse des Servers eingeben!",
				NetworkUtils.REWARD_SERVER_DEFAULT_FULL_TCP_ADRESS);
			REWARD_SERVER_REGISTRY_PORT = JOptionPane.showInputDialog(null, "Bitte den Registry-Port eingeben!",
				NetworkUtils.REWARD_SERVER_DEFAULT_REGISTRY_PORT);

			rewardServer = new RewardServer();
			rewardServer.startServer(Integer.parseInt(REWARD_SERVER_REGISTRY_PORT));
			rewardServer.testStuff();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		// TODO: Timo: persistence.xml -> drop and create zu
		// create-or-extend-tables ändern

	}

	private void testStuff()
	{

		Game game = new Game("TestGame");

		gameDao.add(game);
	}

	public void startServer(int port)
	{
		try
		{
			personDao = new PersonDaoHSQL();
			gamePersonDao = new GamePersonDaoHSQL();
			gameDao = new GameDaoHSQL();
			startActiveMQBroker();

			Registry registry = LocateRegistry.getRegistry(port);
			if (registry == null)
			{
				LocateRegistry.createRegistry(port);
			}
		}

		catch (RemoteException ex)
		{
			LOG.error(ex.getMessage());
		}
		try
		{
			Naming.rebind(NetworkUtils.REWARD_SERVER_NAME, this);
			LOG.info("##### " + NetworkUtils.REWARD_SERVER_NAME + " STARTED ####");
		}
		catch (MalformedURLException ex)
		{
			LOG.error(ex.getMessage());
		}
		catch (RemoteException ex)
		{
			LOG.error(ex.getMessage());
		}
	}

	private void startActiveMQBroker()
	{
		LOG.info("Starting ActiveMQ Broker");

		try
		{
			broker = new BrokerService();
			KahaDBPersistenceAdapter adaptor = new KahaDBPersistenceAdapter();
			adaptor.setDirectory(new File("activemq"));
			broker.setPersistenceAdapter(adaptor);
			broker.setUseJmx(true);
			broker.addConnector(REWARD_SERVER_FULL_TCP_ADDRESS);
			broker.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private void stopActiveMQBroker()
	{
		try
		{
			broker.stop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getConfigurationPointsForPerson(String name) throws NoSuchPersonException, RemoteException
	{

		LOG.info("ConfigPoints requested for " + name);

		List<GamePerson> findAllGamesByPersonName = gamePersonDao.findAllGamesByPersonName(name);

		int points = 0;
		for (GamePerson gamePerson : findAllGamesByPersonName)
		{
			points += gamePerson.getPoints();
		}

		LOG.info(points + " ConfigPoints returned for " + name);

		return points;
	}

	public List<GamePerson> getAllGamesForPersonName(String personName)
	{
		LOG.info("All Games requested for " + personName);

		List<GamePerson> findAllGamesByPersonName = gamePersonDao.findAllGamesByPersonName(personName);

		LOG.info(findAllGamesByPersonName + " returned for " + personName);

		return findAllGamesByPersonName;
	}

	@Override
	public void persistConfigurationPointsForPerson(String personName, String gameName, int points)
		throws RemoteException
	{

		LOG.info("Persist request for: " + personName + ". With " + points + " points for game: " + gameName);

		GamePerson findPersonInGame = gamePersonDao.findPersonInGame(gameName, personName);

		if (findPersonInGame == null)
		{
			LOG.info("Person: " + personName + " has not played " + gameName + " before. Creating new game entry");

			Game game = gameDao.findById(gameName);
			Person person = personDao.findById(personName);

			if (person == null)
			{
				LOG.info("It is persons: " + personName + " first game ever . Creating new person entry");
				person = new Person(personName);
			}

			gamePersonDao.add(new GamePerson(game, person, points));

			LOG.info("Persisted person: " + personName + ". With " + points + " points for game: " + gameName);
		}
		else
		{
			Integer oldPoints = findPersonInGame.getPoints();
			if (oldPoints >= points)
			{
				LOG.info("Person: " + personName + ". Already had " + oldPoints + " points for game: " + gameName
					+ ". No update needed ");
			}
			else
			{
				findPersonInGame.setPoints(points);
				gamePersonDao.update(findPersonInGame);
				LOG.info("Person: " + personName + ". Had " + oldPoints + " points for game: " + gameName
					+ ". Now has " + points);
			}
		}
	}

}
