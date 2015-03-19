package de.kvwl.n8dA.robotwars.client.gui;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.kvwl.n8dA.infrastructure.commons.exception.NoSuchPersonException;
import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.client.gui.LoginDialog.CanceledException;

public class Main
{

	public static void main(String[] args)
	{

		setLaF();

		RoboBattlePlayerClient battleClient = createBattleClient();
		long maxCreditPoints = getCreditPoints();

		ClientFrame clientFrame = new ClientFrame(battleClient, maxCreditPoints);
		clientFrame.setVisible(true);
	}

	private static long getCreditPoints()
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
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,
				"Die Verbindung zum Punkteserver konnte nicht aufgebaut werden. \n" + e.getMessage(),
				"Fehler beim Verbindungsaufbau", JOptionPane.ERROR_MESSAGE);

			throw new RuntimeException(e);
		}

		long credits;
		try
		{
			credits = LoginDialog.getCreditPoints(creditClient);
		}
		catch (CanceledException e)
		{
			throw new RuntimeException(e);
		}

		return credits;
	}

	private static RoboBattlePlayerClient createBattleClient()
	{
		RoboBattlePlayerClient client;
		try
		{
			client = new RoboBattlePlayerClient();
			client.init();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,
				"Die Verbindung zum Spieleserver konnte nicht aufgebaut werden. \n" + e.getMessage(),
				"Fehler beim Verbindungsaufbau", JOptionPane.ERROR_MESSAGE);

			throw new RuntimeException(e);
		}

		return client;
	}

	private static void setLaF()
	{

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| UnsupportedLookAndFeelException e)
		{
		}
	}

}
