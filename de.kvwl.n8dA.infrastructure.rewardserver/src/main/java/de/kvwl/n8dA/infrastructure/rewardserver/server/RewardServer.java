package de.kvwl.n8dA.infrastructure.rewardserver.server;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.commons.entity.Person;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccesHandler;
import de.kvwl.n8dA.infrastructure.commons.util.NetworkUtils;
import de.kvwl.n8dA.infrastructure.rewardserver.dao.UserDaoSqlite;

public class RewardServer extends UnicastRemoteObject implements CreditAccesHandler {

	protected RewardServer() throws RemoteException {
	}

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(RewardServer.class);

	private UserDaoSqlite userDao;
	private BrokerService broker;

	private static String REWARD_SERVER_FULL_TCP_ADDRESS;

	private static String REWARD_SERVER_REGISTRY_PORT;

	public static void main(String[] args) {
		RewardServer rewardServer;
		try {
			BasicConfigurator.configure();
			
			REWARD_SERVER_FULL_TCP_ADDRESS = JOptionPane.showInputDialog(null, "Bitte die vollständige TCP-Adresse des Servers eingeben!", NetworkUtils.REWARD_SERVER_DEFAULT_FULL_TCP_ADRESS);
			REWARD_SERVER_REGISTRY_PORT = JOptionPane.showInputDialog(null, "Bitte den Registry-Port eingeben!", NetworkUtils.REWARD_SERVER_DEFAULT_REGISTRY_PORT);
			
			rewardServer = new RewardServer();
			rewardServer.startServer(Integer.parseInt(REWARD_SERVER_REGISTRY_PORT));
			rewardServer.testStuff();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// TODO: Timo: persistence.xml -> drop and create zu
		// create-or-extend-tables ändern

	}

	private void testStuff() {
		Person user = new Person();
		user.setName("Derp");
		userDao.add(user);
	}

	public void startServer(int port) {
		try {
			userDao = new UserDaoSqlite();
			startActiveMQBroker();

			LocateRegistry.createRegistry(port);
		}

		catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
		try {
			Naming.rebind(NetworkUtils.REWARD_SERVER_NAME, this);
			LOG.info("##### "+ NetworkUtils.REWARD_SERVER_NAME+" STARTED ####");
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

	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException {
		
		LOG.info("ConfigPoints requested for " + name);
		
		Person person= userDao.findById(name);
		int points = person.getPoints();
		
		LOG.info(points + " ConfigPoints returned for " + name);
		
		return points;
	}

}
