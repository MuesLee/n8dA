package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.Person;
import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;

/**
 * Interface für allgemeine Serverzugriffe
 *
 */

public interface BasicCreditAccess extends Remote
{

	public List<GamePerson> getAllGamesForPersonName(String personName) throws RemoteException;

	public List<Game> getAllGames() throws RemoteException;

	public List<GamePerson> getAllGamePersonsForGame(String gameName) throws RemoteException;

	public List<GamePerson> getFirst10GamePersonsForGame(String gameName) throws RemoteException;

	public List<GamePerson> getAllGamePersons() throws RemoteException;
	
	public List<GamePerson> getFirst10GamePersons() throws RemoteException;

	public int getConfigurationPointsForPerson(String name) throws RemoteException, NoSuchPersonException;

	public int getGamePointsForPerson(String person, String name) throws RemoteException, NoSuchPersonException;

	public List<Person> getAllPersons() throws RemoteException;

	public void createGame(String name) throws RemoteException;

	public void deleteGame(String name) throws RemoteException;

	public void clearGame(String name) throws RemoteException;

	/**
	 * Create or overwrite. Egal ob besserer Punktestand oder nicht
	 */
	public void overwriteRecord(String personName, String gameName, int points) throws RemoteException;

	public void persistConfigurationPointsForPerson(String personName, String gameName, int points)
		throws RemoteException;

	public void deletePerson(String name) throws RemoteException;;
}
