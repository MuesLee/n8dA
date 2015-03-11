package de.kvwl.n8dA.robotwars.server.network;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.entities.Robot;

public class RoboBattleServer extends UnicastRemoteObject implements RoboBattleHandler{

	
	private static final long serialVersionUID = 1L;
	
	protected RoboBattleServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args)
	  {
	    try {
	      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	    }
	    
	    catch (RemoteException ex) {
	      System.out.println(ex.getMessage());
	    }
	    try {
	      Naming.rebind("Server", new RoboBattleServer());
	    }
	    catch (MalformedURLException ex) {
	      System.out.println(ex.getMessage());
	    }
	    catch (RemoteException ex) {
	      System.out.println(ex.getMessage());
	    }
	  }
	 
	

	@Override
	public void setActionForRobot(RobotAction RobotAction, Robot robot) {
		// TODO Auto-generated method stub
		
	}
	

}
