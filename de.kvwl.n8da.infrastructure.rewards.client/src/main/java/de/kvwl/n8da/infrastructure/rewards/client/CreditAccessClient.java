package de.kvwl.n8da.infrastructure.rewards.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccesHandler;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.infrastructure.commons.util.NetworkUtils;

/**
 * GUI-loser Client als Verbindung zum zentralen Punkte-Server.
 */
public class CreditAccessClient implements CreditAccess {

	private static final Logger LOG = LoggerFactory
			.getLogger(CreditAccessClient.class);

	private CreditAccesHandler server;
	private UUID uuid;
	private String ipAdressServer;
	
	
	public CreditAccessClient(String ipAdressServer) {
		
		BasicConfigurator.configure();
		this.ipAdressServer = ipAdressServer;
		this.uuid = UUID.randomUUID();
	}

	/**
	 * Baut die Verbindung zum Punkteserver auf
	 */
	public void initConnectionToServer() throws RemoteException {
		try {
			String url = "//"
					+ ipAdressServer + "/"
					+ NetworkUtils.REWARD_SERVER_NAME;
			server = (CreditAccesHandler) Naming.lookup(url);
			LOG.info("Client: " + uuid + " connected to Server");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Ruft den Punktestand f�r den �bergebenen Namen vom Server ab
	 */
	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException {
		return server.getConfigurationPointsForPerson(name);
	}

	// TODO: Timo: Nur zu Testzwecken. Sp�ter entfernen.
	public static void main(String[] args) {
		
		CreditAccessClient client = new CreditAccessClient(NetworkUtils.REWARD_SERVER_HOST_IP_ADDRESS);
		try {
			client.initConnectionToServer();
			client.getConfigurationPointsForPerson("Derp");
		} catch (RemoteException e) {
			LOG.error("Remote Error", e);
		} catch (NoSuchPersonException e) {
			LOG.error("Unbekannte Person", e);
		}
	}

}
