package de.kvwl.n8dA.robotwars.server.network;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.jms.JMSException;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.controller.BattleController;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.server.network.messaging.RoboBattleJMSProducer;

public class RoboBattleServer extends UnicastRemoteObject implements
		RoboBattleHandler {

	private static final int REGISTRY_PORT = Registry.REGISTRY_PORT;
	private static final String SERVER_NAME = "RoboBattleServer";
	private static final long serialVersionUID = 1L;

	private BattleController battleController;
	private RoboBattleJMSProducer producer;
	
	protected RoboBattleServer() throws RemoteException {
		super();
		this.battleController = new BattleController();
	}
	
	private void startActiveMQBroker ()
	{
		 System.out.println("Starting ActiveMQ Broker");
		 
		try {
			BrokerService broker = new BrokerService();
			KahaDBPersistenceAdapter adaptor = new KahaDBPersistenceAdapter();
			adaptor.setDirectory(new File("activemq"));
			broker.setPersistenceAdapter(adaptor);
			broker.setUseJmx(true);
			broker.addConnector("tcp://localhost:1527");
			broker.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//context = new ClassPathXmlApplicationContext("/jms-context.xml", RoboBattleServer.class);
			
	}
	
	private void initJMSProducer()
	{
		producer = new RoboBattleJMSProducer();
		try {
			producer.sendMessages();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
				
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
		}

		catch (RemoteException ex) {
			System.out.println(ex.getMessage());
		}
		try {
			Naming.rebind(SERVER_NAME, this);
			System.out.println("##### SERVER STARTED ####");
		} catch (MalformedURLException ex) {
			System.out.println(ex.getMessage());
		} catch (RemoteException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void setActionForRobot(RobotAction robotAction, Robot robot) throws UnknownRobotException, RobotHasInsufficientEnergyException {
		System.out.println("Server TEST");
		battleController.setActionForRobot(robotAction, robot);
	}
	
	@Override
	public void registerRobotAndClientForBattle(Robot robot) throws RemoteException,
			NoFreeSlotInBattleArenaException {
		if (battleController.getRobotLeft() == null) {
			battleController.setRobotLeft(robot);
			
			System.out.println("Robot registered: " + robot);
			
		} else if (battleController.getRobotRight() == null) {
			battleController.setRobotRight(robot);
			
		} else {
			throw new NoFreeSlotInBattleArenaException();
		}
		
		

	}

	@Override
	public Robot getSynchronizedRobot(Robot robot) throws RemoteException,
	UnknownRobotException {
		return battleController.getLocalRobotForRemoteRobot(robot);
	}

}
