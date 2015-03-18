package de.kvwl.n8dA.robotwars.client;

//TODO Timo: Implementierung
/**
 * 
 * Verbindung zu unserem Punkteserver
 *
 */
public interface CreditAccess
{
	int getConfigurationPointsForPerson(String name) throws NoSuchPersonException;
	
}
