package de.kvwl.n8dA.infrastructure.rewardserver.server;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.rewardserver.dao.UserDaoSqlite;
import de.kvwl.n8dA.infrastructure.rewardserver.entity.Person;

public class RewardServer extends UnicastRemoteObject {

	protected RewardServer() throws RemoteException {
	}

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(RewardServer.class);

	private static final String SERVER_NAME = "RewardServer";
	private static final int SERVER_REGISTRY_PORT = Registry.REGISTRY_PORT;
	private static final String HOST_IP_ADDRESS = "localhost";
	private static final String FULL_HOST_TCP_ADDRESS = "tcp://"
			+ HOST_IP_ADDRESS + ":1528";

	private UserDaoSqlite userDao;
	private BrokerService broker;

	public static void main(String[] args) {
		RewardServer rewardServer;
		try {
			BasicConfigurator.configure();
			rewardServer = new RewardServer();
			rewardServer.startServer(SERVER_REGISTRY_PORT);
			rewardServer.testStuff();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: Timo: persistence.xml -> drop and create zu
		// create-or-extend-tables ändern

	}

	private void testStuff() {
		Person user = new Person();
		user.setName("Derp");
		userDao.add(user);

		Person findById = userDao.findById(user.getName());
		System.out.println(findById.getName() + " " + findById.getName());
		userDao.delete(user);
	}

	public void startServer(int port) {
		try {
			startActiveMQBroker();

			LocateRegistry.createRegistry(port);

		}

		catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
		try {
			Naming.rebind(SERVER_NAME, this);
			LOG.info("##### "+ SERVER_NAME+" STARTED ####");
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
			broker.addConnector(FULL_HOST_TCP_ADDRESS);
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

}
