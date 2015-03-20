package de.kvwl.n8dA.robotwars.server.network;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.swing.JOptionPane;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.exception.WrongGameStateException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.ItemUtil;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.commons.network.messages.ClientProperty;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;
import de.kvwl.n8dA.robotwars.server.controller.BattleController;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.input.DataLoaderFileSystemImpl;
import de.kvwl.n8dA.robotwars.server.network.messaging.RoboBattleJMSProducerServer;
import de.kvwl.n8dA.robotwars.server.network.messaging.RoboBattleJMSReceiverServer;

public class RoboBattleServer extends UnicastRemoteObject implements
		RoboBattleHandler, MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(RoboBattleServer.class);
	
	private static final long serialVersionUID = 1L;

	private static String BATTLE_SERVER_REGISTRY_PORT;

	private BattleController battleController;
	private RoboBattleJMSProducerServer producer;
	private RoboBattleJMSReceiverServer receiver;
	private BrokerService broker;
	
	private UUID clientUUIDLeft;
	private UUID clientUUIDRight;

	private static String BATTLE_SERVER_FULL_TCP_ADDRESS;
	
	protected RoboBattleServer() throws RemoteException {
		super();
		this.battleController = new BattleController();
		this.battleController.setServer(this);
	}

	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			
			
			BATTLE_SERVER_FULL_TCP_ADDRESS = JOptionPane.showInputDialog(null, "Bitte die vollständige TCP-Adresse des Servers eingeben!", NetworkUtils.BATTLE_SERVER_DEFAULT_FULL_TCP_ADDRESS);
			RoboBattleServer server = new RoboBattleServer();
			BATTLE_SERVER_REGISTRY_PORT = JOptionPane.showInputDialog(null, "Bitte den Registry-Port eingeben!", NetworkUtils.BATTLE_SERVER_DEFAULT_REGISTRY_PORT);
			
			server.startServer(Integer.parseInt(BATTLE_SERVER_REGISTRY_PORT));
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startServer(int port) {
		try {
			startActiveMQBroker();
			
			initJMS();
			
			LocateRegistry.createRegistry(port);
			
			loadGameData();
		}
	
		catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
		try {
			Naming.rebind(NetworkUtils.SERVER_NAME, this);
			LOG.info("##### SERVER STARTED ####");
		} catch (MalformedURLException ex) {
			LOG.error(ex.getMessage());
		} catch (RemoteException ex) {
			LOG.error(ex.getMessage());
		}
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
			broker.addConnector(BATTLE_SERVER_FULL_TCP_ADDRESS);
			broker.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
			
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
	
	private void initJMS()
	{
		producer = new RoboBattleJMSProducerServer(BATTLE_SERVER_FULL_TCP_ADDRESS);
		receiver = new RoboBattleJMSReceiverServer(BATTLE_SERVER_FULL_TCP_ADDRESS);
		receiver.setMessageListener(this);
	}

	private void loadGameData() {
		
		DataLoader loader = new DataLoaderFileSystemImpl();
		
		battleController.setAllAttacks(loader.loadRobotAttacks());
		battleController.setAllDefends(loader.loadRobotDefends());
		battleController.setAllRobots(loader.loadRobots());
		battleController.setAllItems(ItemUtil.getAllRoboItems());
		
	}

	private void handleMessage(Message message) {
		
		try {
			String uuidAsString = message.getStringProperty(ClientProperty.UUID.getName());
			boolean playerIsRdy = message.getBooleanProperty(ClientProperty.UUID.getName());
			boolean clientDisconnected = message.getBooleanProperty(ClientProperty.DISCONNECT.getName());
			UUID clientUUID = UUID.fromString(uuidAsString);
			
			
			if(playerIsRdy)
			{
				battleController.setRobotIsReady(getRobotForUUID(clientUUID));
			}
			if(clientDisconnected)
			{
				disconnectClient(clientUUID);
			}
			
		} catch (JMSException e) {
			
		} catch (UnknownRobotException e) {
		}
	}



	private void handleObjectMessage(ObjectMessage message) {
		
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Serializable object = objectMessage.getObject();
			RobotAction robotAction = (RobotAction) object;
			String uuidAsString = objectMessage.getStringProperty(ClientProperty.UUID.getName());
			UUID clientUUID = UUID.fromString(uuidAsString);
			
			setActionForRobot(robotAction, clientUUID);
			
		} catch (JMSException e) {
		LOG.error("Error while receiving Player input", e);
		}
		catch (ClassCastException e) {
			LOG.error("Wrong class format for player input", e);
		} catch (UnknownRobotException e) {
			LOG.error("Wrong robot...", e);
		} catch (RobotHasInsufficientEnergyException e) {
			LOG.error("Robot has not Energy to use this action", e);
		} catch (WrongGameStateException e) {
			LOG.error("Not the right to time to send input...", e);
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

	private void disconnectClient(UUID clientUUID) {
		
		//TODO: Timo: Spiel beenden
		// Neuer BattleController?
		LOG.info("Client disconnected: " + clientUUID);
		
		if(clientUUID.equals(clientUUIDLeft))
		{
			clientUUIDLeft = null;
			battleController.setRobotLeft(null);
		}
		else if(clientUUID.equals(clientUUIDRight))
		{
			clientUUIDRight = null;
			battleController.setRobotRight(null);
		}
		else {
			LOG.info("Unknown Client wanted to disconnect:" + clientUUID);
		}
	}
	
	
	public void setActionForRobot(RobotAction robotAction, UUID uuid)
			throws UnknownRobotException, RobotHasInsufficientEnergyException, WrongGameStateException {
		LOG.info("Action: " + robotAction + " received from UUID: " + uuid);
		
		battleController.setActionForRobot(robotAction, getRobotForUUID(uuid));
	}

	@Override
	public Robot getSynchronizedRobot(UUID uuid) throws RemoteException,
	UnknownRobotException {
		
		Robot robot = battleController.getLocalRobotForRemoteRobot(getRobotForUUID(uuid));
		LOG.info("UUID: " + uuid + " has requested update of Robot. Received " + robot);
		
		return robot;
	}

	public void sendGameStateInfoToClients(GameStateType gameStateType)
	{
		producer.sendGameStateNotificationToAllClients(gameStateType);
	}

	@Override
	public RobotPosition registerRobotAndClientForBattle(Robot robot, UUID uuid) throws RemoteException,
			NoFreeSlotInBattleArenaException {
		if (battleController.getRobotLeft() == null) {
			battleController.setRobotLeft(robot);
			clientUUIDLeft = uuid;
			LOG.info("Robot registered: " + robot + " ClientUUID: " + uuid);
			return RobotPosition.LEFT;
			
		} else if (battleController.getRobotRight() == null) {
			battleController.setRobotRight(robot);
			clientUUIDRight = uuid;
			
			LOG.info("Robot registered: " + robot + " ClientUUID: " + uuid);
			
			return RobotPosition.RIGHT;
			
		} else {
			throw new NoFreeSlotInBattleArenaException();
		}
		
		
	
	}

	@Override
	public void onMessage(Message message) {
	
		if(message instanceof ObjectMessage)
		{
			handleObjectMessage((ObjectMessage) message);
		}
		else if (message instanceof Message)
		{
			handleMessage(message);
		}
		
		
		}

	@Override
	public List<Robot> getAllPossibleRobots() {
		
		return battleController.getAllRobots();
	}

	@Override
	public List<RoboItem> getAllPossibleItems() {
		return battleController.getAllItems();
	}

	@Override
	public List<Attack> getAllPossibleAttacks() {
		return battleController.getAllAttacks();
	}

	@Override
	public List<Defense> getAllPossibleDefends() {
		return battleController.getAllDefends();
	}

	@Override
	public Robot getSynchronizedRobotOfEnemy(UUID ownUUID)
			throws RemoteException, UnknownRobotException {
		
		if(ownUUID.equals(clientUUIDLeft))
		{
			return battleController.getRobotRight();
		}
		else if (ownUUID.equals(clientUUIDRight)){
			return battleController.getRobotLeft();
		}
		throw new UnknownRobotException();
	}
		
		
	}

