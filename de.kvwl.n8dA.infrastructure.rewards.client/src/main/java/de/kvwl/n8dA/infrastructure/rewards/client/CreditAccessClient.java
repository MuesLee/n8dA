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

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccesHandler;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.infrastructure.commons.util.NetworkUtils;

/**
 * GUI-loser Client als Verbindung zum zentralen Punkte-Server.
 */
public class CreditAccessClient implements CreditAccess
{

	private static final Logger LOG = LoggerFactory.getLogger(CreditAccessClient.class);

	private CreditAccesHandler server;
	private UUID uuid;
	private String ipAdressServer;
	
	public CreditAccessClient(String ipAdressServer)
	{
		System.setSecurityManager(new SecurityManager());
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
	public void initConnectionToServer() throws RemoteException, MalformedURLException, NotBoundException
	{
		String url = "//" + ipAdressServer + "/" + NetworkUtils.REWARD_SERVER_NAME;
		server = (CreditAccesHandler) Naming.lookup(url);
		LOG.info("Client: " + uuid + " connected to Server");
	}

	/**
	 * Ruft den Punktestand f�r den �bergebenen Namen vom Server ab
	 */
	public int getConfigurationPointsForPerson(String name) throws NoSuchPersonException, RemoteException
	{
		return server.getConfigurationPointsForPerson(name);
	}

	// TODO: Timo: Nur zu Testzwecken. Sp�ter entfernen.
	public static void main(String[] args) throws Exception
	{

		CreditAccessClient client = new CreditAccessClient("localhost");
		try
		{
			client.initConnectionToServer();
			
			client.persistConfigurationPointsForPerson("Derp","TestGame", 5);
			client.persistConfigurationPointsForPerson("Derp","TestGame", 6);
			System.out.println(client.getAllGamesForPersonName("Derp"));
		}
		catch (RemoteException e)
		{
			LOG.error("Remote Error", e);
		}
	}

	@Override
	public void persistConfigurationPointsForPerson(String personName, String gameName, int points)
			throws RemoteException {
		server.persistConfigurationPointsForPerson(personName,gameName, points);
	}
	
	public List<GamePerson> getAllGamesForPersonName(String personName) throws RemoteException
	{
		return server.getAllGamesForPersonName(personName);
	}
	
	

}
