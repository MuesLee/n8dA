package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;

public interface CreditAccesHandler extends Remote{
	
	public int getConfigurationPointsForPerson(String name)
			throws NoSuchPersonException, RemoteException;
	
	public void persistConfigurationPointsForPerson(String personName, String gameName, int points) throws RemoteException;
	
	public List<GamePerson> getAllGamesForPersonName(String personName) throws RemoteException;

}
