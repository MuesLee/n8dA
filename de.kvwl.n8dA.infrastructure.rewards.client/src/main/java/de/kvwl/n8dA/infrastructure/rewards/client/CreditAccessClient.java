package de.kvwl.n8dA.infrastructure.rewards.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.Person;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.infrastructure.commons.util.NetworkUtils;

/**
 * GUI-loser Client als Verbindung zum zentralen Punkte-Server.
 */
public class CreditAccessClient implements CreditAccess {

	private static final Logger LOG = LoggerFactory
			.getLogger(CreditAccessClient.class);

	private BasicCreditAccess server;
	private UUID uuid;
	private String ipAdressServer;

	public CreditAccessClient(String ipAdressServer) {

		this(ipAdressServer, false);
	}

	public CreditAccessClient(String ipAdressServer,
			boolean installSecurityManager) {

		System.out.println(System.getProperty("user.home"));

		// TODO Timo: Bau das mal auf Startparameter um
		/*
		 * Notes:
		 * 
		 * The URL can be any regular URL or simply the name of a policy file in
		 * the current directory, as in
		 * 
		 * java -Djava.security.manager -Djava.security.policy=mypolicy SomeApp
		 * 
		 * The "-Djava.security.manager" argument ensures that the default
		 * security manager is installed, and thus the application is subject to
		 * policy checks. It is not required if the application SomeApp installs
		 * a security manager.
		 * 
		 * If you use
		 * 
		 * java -Djava.security.manager -Djava.security.policy==someURL SomeApp
		 * 
		 * (note the double equals) then just the specified policy file will be
		 * used; all the ones indicated in the security properties file will be
		 * ignored.
		 */

		if (installSecurityManager) {

			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
		}

		BasicConfigurator.configure();
		this.ipAdressServer = ipAdressServer;
		this.uuid = UUID.randomUUID();
	}

	/**
	 * Baut die Verbindung zum Punkteserver auf
	 * 
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 */
	public void initConnectionToServer() throws RemoteException,
			MalformedURLException, NotBoundException {
		String url = "//" + ipAdressServer + "/"
				+ NetworkUtils.REWARD_SERVER_NAME;
		server = (BasicCreditAccess) Naming.lookup(url);
		LOG.info("Client: " + uuid + " connected to Server");
	}

	/**
	 * Ruft den Punktestand f�r den �bergebenen Namen vom Server ab
	 */
	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException {
		if (server == null) {
			return server.getConfigurationPointsForPerson(name);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	// XXX Timo: Nur zu Testzwecken. Später entfernen.
	public static void main(String[] args) throws Exception {

		CreditAccessClient client = new CreditAccessClient("localhost");
		try {
			client.initConnectionToServer();

			client.persistConfigurationPointsForPerson("Derp", "TestGame", 5);
			client.persistConfigurationPointsForPerson("Derp", "TestGame", 6);
			System.out.println(client.getAllGamesForPersonName("Derp"));
		} catch (RemoteException e) {
			LOG.error("Remote Error", e);
		}
	}

	@Override
	public void persistConfigurationPointsForPerson(String personName,
			String gameName, int points) throws RemoteException {
		if (server == null) {
			server.persistConfigurationPointsForPerson(personName, gameName,
					points);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	public List<GamePerson> getAllGamesForPersonName(String personName)
			throws RemoteException {
		if (server == null) {
			return server.getAllGamesForPersonName(personName);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public List<Game> getAllGames() throws RemoteException {
		if (server == null) {
			return server.getAllGames();
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public List<GamePerson> getAllGamePersonsForGame(String gameName)
			throws RemoteException {
		if (server == null) {
			return server.getAllGamePersonsForGame(gameName);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public List<GamePerson> getAllGamePersons() throws RemoteException {
		if (server == null) {
			return server.getAllGamePersons();
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public List<GamePerson> getFirst10GamePersonsForGame(String gameName)
			throws RemoteException {
		if (server == null) {
			return server.getFirst10GamePersonsForGame(gameName);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public int getGamePointsForPerson(String person, String name)
			throws RemoteException, NoSuchPersonException {
		if (server == null) {
			return server.getGamePointsForPerson(person, name);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public List<Person> getAllPersons() throws RemoteException {
		if (server == null) {
			return server.getAllPersons();
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public void createGame(String name) throws RemoteException {

		if (server == null) {
			server.createGame(name);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");

	}

	@Override
	public void deleteGame(String name) throws RemoteException {
		if (server == null) {
			server.deleteGame(name);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");

	}

	@Override
	public void clearGame(String name) throws RemoteException {
		if (server == null) {
			server.clearGame(name);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public void deletePerson(String name) throws RemoteException {
		if (server == null) {
			server.deletePerson(name);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}

	@Override
	public void overwriteRecord(String personName, String gameName, int points)
			throws RemoteException {
		if (server == null) {
			server.overwriteRecord(personName, gameName, points);
		}
		throw new RemoteException("Server ist null / nicht erreichbar.");
	}
}
