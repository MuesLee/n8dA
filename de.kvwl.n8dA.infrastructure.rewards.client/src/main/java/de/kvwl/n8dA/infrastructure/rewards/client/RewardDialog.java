package de.kvwl.n8dA.infrastructure.rewards.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

public class RewardDialog
{

	public static void showRewardDialog(String gameName, int points) throws RemoteException, MalformedURLException,
		NotBoundException, PlayerNameException
	{
		showRewardDialog(gameName, points, null);
	}

	public static void showRewardDialog(String gameName, int points, String playerName) throws RemoteException,
		MalformedURLException, NotBoundException, PlayerNameException
	{

		String ipAdressServer = Settings.getString("ip_address_server", "localhost");

		showRewardDialog(gameName, points, playerName, ipAdressServer);
	}

	public static void showRewardDialog(String gameName, int points, String playerName, String ipAdressServer)
		throws RemoteException, MalformedURLException, NotBoundException, PlayerNameException
	{
		boolean securityManager = Settings.getBoolean("install_security_manager", false);

		showRewardDialog(gameName, points, playerName, ipAdressServer, securityManager);
	}

	public static void showRewardDialog(String gameName, int points, String playerName, String ipAdressServer,
		boolean securityManager) throws RemoteException, MalformedURLException, NotBoundException, PlayerNameException
	{

		CreditAccessClient accessClient = null;
		try
		{
			accessClient = new CreditAccessClient(ipAdressServer, securityManager);
			accessClient.initConnectionToServer();
		}
		catch (RemoteException | MalformedURLException | NotBoundException e)
		{

			ExceptionDialog.showExceptionDialog(null, "Verbindungsfehler",
				"Es konnte keien Verbindung zum Punkteserver aufgebaut werden.", e);
			throw (e);
		}

		if (playerName == null)
		{

			playerName = JOptionPane.showInputDialog(null, "Bitte gebe deinen Benutzername ein");
		}

		if (playerName == null || playerName.isEmpty())
		{
			JOptionPane.showMessageDialog(null, String.format("Der angegebene Name '%s' ist üngültig", playerName),
				"Üngültiger Name", JOptionPane.ERROR_MESSAGE);

			throw new PlayerNameException("Illegal player name: " + playerName);
		}

		try
		{
			accessClient.persistConfigurationPointsForPerson(playerName, gameName, points);
		}
		catch (RemoteException e)
		{

			ExceptionDialog.showExceptionDialog(null, "Verbindungsfehler",
				"Beim senden der Punkte ist ein Fehler aufgetreten.", e);
			throw (e);
		}
	}

	public static void main(String[] args) throws Exception
	{

		RewardDialog.showRewardDialog("test", 100);
	}
}
