package de.kvwl.n8dA.robotwars.server.network;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.HPBoostItem;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.controller.BattleController;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.input.DataLoaderFileSystemImpl;
import de.kvwl.n8dA.robotwars.server.network.messaging.RoboBattleJMSProducer;

public class RoboBattleServer extends UnicastRemoteObject implements
		RoboBattleHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RoboBattleServer.class);
	
	private static final int REGISTRY_PORT = Registry.REGISTRY_PORT;
	private static final String SERVER_NAME = "RoboBattleServer";
	private static final long serialVersionUID = 1L;

	private BattleController battleController;
	@SuppressWarnings("unused")
	private RoboBattleJMSProducer producer;
	private BrokerService broker;
	
	private UUID clientUUIDLeft;
	private UUID clientUUIDRight;
	
	protected RoboBattleServer() throws RemoteException {
		super();
		this.battleController = new BattleController();
	}

	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			RoboBattleServer server = new RoboBattleServer();
			server.startServer(REGISTRY_PORT);
	
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startServer(int port) {
		try {
			startActiveMQBroker();
			
			initJMSProducer();
			
			LocateRegistry.createRegistry(port);
			
			loadGameData();
		}
	
		catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
		try {
			Naming.rebind(SERVER_NAME, this);
			LOG.info("##### SERVER STARTED ####");
		} catch (MalformedURLException ex) {
			LOG.error(ex.getMessage());
		} catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
	}

	@Override
	public void setActionForRobot(RobotAction robotAction, UUID uuid) throws UnknownRobotException, RobotHasInsufficientEnergyException {
		LOG.info("Server TEST");
			battleController.setActionForRobot(robotAction, getRobotForUUID(uuid));
	}
	
	@Override
	public void registerRobotAndClientForBattle(Robot robot, UUID uuid) throws RemoteException,
			NoFreeSlotInBattleArenaException {
		if (battleController.getRobotLeft() == null) {
			battleController.setRobotLeft(robot);
			clientUUIDLeft = uuid;
			
			LOG.info("Robot registered: " + robot + " ClientUUID: " + uuid);
			
		} else if (battleController.getRobotRight() == null) {
			battleController.setRobotRight(robot);
			clientUUIDRight = uuid;
			LOG.info("Robot registered: " + robot + " ClientUUID: " + uuid);
			
		} else {
			throw new NoFreeSlotInBattleArenaException();
		}
		
		

	}

	@Override
	public Robot getSynchronizedRobot(UUID uuid) throws RemoteException,
	UnknownRobotException {
		
		Robot robot = battleController.getLocalRobotForRemoteRobot(getRobotForUUID(uuid));
		LOG.info("UUID: " + uuid + " has requested update of Robot. Received " + robot);
		
		return robot;
	}
	
	private void startActiveMQBroker ()
	{
		 LOG.info("Starting ActiveMQ Broker");
		 
		try {
			broker = new BrokerService();
			KahaDBPersistenceAdapter adaptor = new KahaDBPersistenceAdapter();
			adaptor.setDirectory(new File("activemq"));
			broker.setPersistenceAdapter(adaptor);
			broker.setUseJmx(true);
			broker.addConnector("tcp://localhost:1527");
			broker.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
			
	}
	
	private Robot getRobotForUUID(UUID uuid) throws UnknownRobotException
	{
		if(uuid.equals(clientUUIDLeft))
			return battleController.getRobotLeft();
		if (uuid.equals(clientUUIDRight)) 
			return battleController.getRobotRight();
		
		throw new UnknownRobotException();
			
	}
	
	@SuppressWarnings("unused")
	private void stopActiveMQBroker()
	{
		try {
			broker.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initJMSProducer()
	{
		producer = new RoboBattleJMSProducer();
		//producer.sendMessages();
	}

	private void loadGameData() {
		
		DataLoader loader = new DataLoaderFileSystemImpl();
		
		battleController.setAllAttacks(loader.loadRobotAttacks());
		battleController.setAllDefends(loader.loadRobotDefends());
		battleController.setAllRobots(loader.loadRobots());
		List<RoboItem> allItems = new ArrayList<>();
		allItems.add(new HPBoostItem());
		battleController.setAllItems(allItems);
		
	}

}
