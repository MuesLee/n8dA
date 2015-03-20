package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;

//TODO Timo: Implementierung
/**
 * 
 * Verbindung zu unserem Punkteserver
 *
 */
public interface CreditAccess
{
	void initConnectionToServer() throws RemoteException, MalformedURLException, NotBoundException;

	int getConfigurationPointsForPerson(String name) throws NoSuchPersonException, RemoteException;

}
