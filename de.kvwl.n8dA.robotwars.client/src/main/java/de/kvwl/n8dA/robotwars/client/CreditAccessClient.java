package de.kvwl.n8dA.robotwars.client;

import java.rmi.RemoteException;

public class CreditAccessClient implements CreditAccess {

	
	
	public CreditAccessClient() {
		
		
	}
	
	
	@Override
	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException {
		
		// TODO Timo: implementieren
		return 0;
	}


	@Override
	public void initConnectionToServer() throws RemoteException {
		// TODO Timo: implementieren
		
	}

}
