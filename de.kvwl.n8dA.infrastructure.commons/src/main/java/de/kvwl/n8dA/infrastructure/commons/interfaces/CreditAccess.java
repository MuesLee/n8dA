package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * 
 * Interface für Clientfunktionen zum Punkteserver
 *
 */
public interface CreditAccess extends BasicCreditAccess
{
	void initConnectionToServer() throws RemoteException, MalformedURLException, NotBoundException;
}
