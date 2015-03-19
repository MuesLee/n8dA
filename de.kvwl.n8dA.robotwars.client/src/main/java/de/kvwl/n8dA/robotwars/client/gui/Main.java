package de.kvwl.n8dA.robotwars.client.gui;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.client.gui.LoginDialog.CanceledException;

public class Main
{

	public static void main(String[] args) throws CanceledException
	{

		RoboBattlePlayerClient battleClient = createBattleClient();
		long maxCreditPoints = getCreditPoints();

		ClientFrame clientFrame = new ClientFrame(battleClient, maxCreditPoints);
		clientFrame.setVisible(true);
	}

	private static long getCreditPoints() throws CanceledException
	{

		//TODO Timo: CreditAccess Implementierung einsetzen
		CreditAccess creditClient = new CreditAccess()
		{

			@Override
			public void initConnectionToServer() throws RemoteException
			{
			}

			@Override
			public int getConfigurationPointsForPerson(String name) throws NoSuchPersonException, RemoteException
			{
				return 365 + (int) (Math.random() * 100);
			}
		};
		try
		{
			creditClient.initConnectionToServer();
		}
		catch (RemoteException e)
		{
			JOptionPane.showMessageDialog(null,
				"Die Verbindung zum Punkteserver konnte nicht aufgebaut werden. \n" + e.getMessage(),
				"Fehler beim Verbindungsaufbau", JOptionPane.ERROR_MESSAGE);
		}

		return LoginDialog.getCreditPoints(creditClient);
	}

	private static RoboBattlePlayerClient createBattleClient()
	{
		RoboBattlePlayerClient client = new RoboBattlePlayerClient();
		client.init();

		return client;
	}

}
