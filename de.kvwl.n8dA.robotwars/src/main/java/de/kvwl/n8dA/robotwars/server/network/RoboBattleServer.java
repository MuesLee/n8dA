package de.kvwl.n8dA.robotwars.server.network;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.controller.BattleController;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;

public class RoboBattleServer extends UnicastRemoteObject implements
		RoboBattleHandler {

	private static final String SERVER_NAME = "RoboBattleServer";
	private static final long serialVersionUID = 1L;

	private BattleController battleController;
	
	protected RoboBattleServer() throws RemoteException {
		super();
		this.battleController = new BattleController();
	}

	public static void main(String[] args) {
		try {

			RoboBattleServer server = new RoboBattleServer();
			server.startServer(Registry.REGISTRY_PORT);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startServer(int port) {
		try {
			LocateRegistry.createRegistry(port);
		}

		catch (RemoteException ex) {
			System.out.println(ex.getMessage());
		}
		try {
			Naming.rebind(SERVER_NAME, this);
		} catch (MalformedURLException ex) {
			System.out.println(ex.getMessage());
		} catch (RemoteException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("##### SERVER STARTED ####");
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
