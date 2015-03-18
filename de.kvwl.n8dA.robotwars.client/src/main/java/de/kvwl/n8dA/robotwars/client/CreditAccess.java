package de.kvwl.n8dA.robotwars.client;

import java.rmi.RemoteException;

//TODO Timo: Implementierung
/**
 * 
 * Verbindung zu unserem Punkteserver
 *
 */
public interface CreditAccess
{
	void initConnectionToServer() throws RemoteException;
	
	int getConfigurationPointsForPerson(String name) throws NoSuchPersonException, RemoteException;
	
}
