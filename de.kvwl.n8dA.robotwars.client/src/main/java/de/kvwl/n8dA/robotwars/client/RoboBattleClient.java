package de.kvwl.n8dA.robotwars.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.util.Properties;
import java.util.UUID;

import javax.jms.MessageListener;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.client.communication.AsyncServerCommunicator;
import de.kvwl.n8dA.robotwars.client.communication.RoboBattleJMSProducerClient;
import de.kvwl.n8dA.robotwars.client.communication.RoboBattleJMSReceiverClient;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;

/**
 * Abstract Client Class Establishes a connection to the gameserver und listens
 * to the JMS-Queue
 * 
 */
public abstract class RoboBattleClient implements MessageListener {

	protected final Logger LOG = LoggerFactory
			.getLogger(RoboBattleClient.class);
	private static final String PROPERTY_NAME = "config.properties";

	protected UUID uuid;
	protected RoboBattleHandler server;
	protected RoboBattleJMSReceiverClient roboBattleJMSReceiver;
	protected AsyncServerCommunicator producer;
	private Properties properties;

	public RoboBattleClient() {

		this.uuid = UUID.randomUUID();

		BasicConfigurator.configure();

	}

	public void init() {
		String ipAdressServer = "localhost";
		String BATTLE_SERVER_FULL_TCP_ADDRESS = "tcp://localhost:1527";

		try {
			loadProperties();
			ipAdressServer = properties.getProperty("BATTLE_SERVER_IP_ADDRESS");
			BATTLE_SERVER_FULL_TCP_ADDRESS = properties
					.getProperty("BATTLE_SERVER_FULL_TCP_ADDRESS");

		} catch (IOException e) {
			e.printStackTrace();
		}

		roboBattleJMSReceiver = new RoboBattleJMSReceiverClient(uuid,
				BATTLE_SERVER_FULL_TCP_ADDRESS);
		producer = new RoboBattleJMSProducerClient(uuid,
				BATTLE_SERVER_FULL_TCP_ADDRESS);

		String url = "//" + ipAdressServer + "/" + NetworkUtils.SERVER_NAME;
		connectToServer(url);
		listenToJMSReceiver();
	}

	public String getProperty(String property) {
		return properties.getProperty(property);
	}

	public void loadProperties() throws IOException {
		properties = new Properties();
		try {
			FileInputStream file = new FileInputStream("./" + PROPERTY_NAME);
			properties.load(file);
			file.close();
		} catch (IOException e) {
			throw new IOException("Die Datei " + PROPERTY_NAME
					+ " konnte nicht gefunden werden.");
		}
	}

	private void connectToServer(String url) {
		try {
			server = (RoboBattleHandler) Naming.lookup(url);
			LOG.info("Client: " + uuid + " connected to Server");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void listenToJMSReceiver() {

		roboBattleJMSReceiver.setMessageListener(this);
		LOG.info("Client: " + uuid + "is listening on JSM-Topic");
	}
}
