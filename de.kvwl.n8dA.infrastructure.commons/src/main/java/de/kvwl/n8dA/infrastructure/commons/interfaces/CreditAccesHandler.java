package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;

public interface CreditAccesHandler extends Remote{
	
	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException;
	
	public void persistConfigurationPointsForPerson(String name, int points) throws RemoteException;

}
