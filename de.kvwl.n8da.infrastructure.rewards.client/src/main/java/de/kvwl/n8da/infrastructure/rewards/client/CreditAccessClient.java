package de.kvwl.n8da.infrastructure.rewards.client;

import java.rmi.RemoteException;

import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;

public class CreditAccessClient implements CreditAccess {

	
	
	public CreditAccessClient() {
		
		
	}
	
	public void initConnectionToServer() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

}
