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

public class CreditAccessClient implements CreditAccess {

	private static final Logger LOG = LoggerFactory
			.getLogger(CreditAccessClient.class);

	private static final String url = "//"
			+ NetworkUtils.REWARD_SERVER_HOST_IP_ADDRESS + "/"
			+ NetworkUtils.REWARD_SERVER_NAME;
	private CreditAccesHandler server;
	private UUID uuid;

	public CreditAccessClient() {
		BasicConfigurator.configure();
		this.uuid = UUID.randomUUID();
	}

	public void initConnectionToServer() throws RemoteException {
		try {
			server = (CreditAccesHandler) Naming.lookup(url);
			LOG.info("Client: " + uuid + " connected to Server");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException {
		return server.getConfigurationPointsForPerson(name);
	}

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
