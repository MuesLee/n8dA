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

	// TODO Timo: IP:Port dynamisch
	// mach das mal so, dass die IP:Port angaben oder was es da so
	// gibt zumindest über die -Dxxx JVM options angegeben werden können
	private static final String url = "//"
			+ NetworkUtils.REWARD_SERVER_HOST_IP_ADDRESS + "/"
			+ NetworkUtils.REWARD_SERVER_NAME;
	private CreditAccesHandler server;
	private UUID uuid;

	public CreditAccessClient() {
		BasicConfigurator.configure();
		this.uuid = UUID.randomUUID();
	}

	/**
	 * Baut eine vorkonfigurierte Verbindung zum Punkteserver auch
	 */
	public void initConnectionToServer() throws RemoteException {
		try {
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
		CreditAccessClient client = new CreditAccessClient();
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
