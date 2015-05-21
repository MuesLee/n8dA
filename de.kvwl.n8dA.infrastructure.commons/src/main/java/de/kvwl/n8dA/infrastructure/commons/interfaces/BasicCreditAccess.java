package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;

/**
 * Interface für allgemeine Serverzugriffe 
 *
 */

public interface BasicCreditAccess extends Remote{
	
	public List<GamePerson> getAllGamesForPersonName(String personName) throws RemoteException;
	
	public int getConfigurationPointsForPerson(String name) throws RemoteException, NoSuchPersonException;

	public void persistConfigurationPointsForPerson(String personName,
			String gameName, int points) throws RemoteException;;
}
