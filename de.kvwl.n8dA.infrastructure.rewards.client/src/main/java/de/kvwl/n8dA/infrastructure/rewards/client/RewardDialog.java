package de.kvwl.n8dA.infrastructure.rewards.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

public class RewardDialog
{

	/**
	 * Versucht alle nicht angegebenen Werte aus der Propertiesdatei zu lesen.
	 * <code>./reward_settings.properties</code>
	 * 
	 * @see #showRewardDialog(String, int, String, String, boolean)
	 */
	public static void showRewardDialog(int points) throws RemoteException, MalformedURLException, NotBoundException,
		PlayerNameException
	{

		String gameName = Settings.getString("game_name", null);
		if (gameName == null || gameName.isEmpty())
		{
			throw new IllegalArgumentException("Illegal game name: " + gameName);
		}

		showRewardDialog(gameName, points);
	}

	/**
	 * Versucht alle nicht angegebenen Werte aus der Propertiesdatei zu lesen.
	 * <code>./reward_settings.properties</code>
	 * 
	 * @see #showRewardDialog(String, int, String, String, boolean)
	 */
	public static void showRewardDialog(String gameName, int points) throws RemoteException, MalformedURLException,
		NotBoundException, PlayerNameException
	{
		showRewardDialog(gameName, points, null);
	}

	/**
	 * Versucht alle nicht angegebenen Werte aus der Propertiesdatei zu lesen.
	 * <code>./reward_settings.properties</code>
	 * 
	 * @see #showRewardDialog(String, int, String, String, boolean)
	 */
	public static void showRewardDialog(int points, String playerName) throws RemoteException, MalformedURLException,
		NotBoundException, PlayerNameException
	{

		String gameName = Settings.getString("game_name", null);
		if (gameName == null || gameName.isEmpty())
		{
			throw new IllegalArgumentException("Illegal game name: " + gameName);
		}

		showRewardDialog(gameName, points, playerName);
	}

	/**
	 * Versucht alle nicht angegebenen Werte aus der Propertiesdatei zu lesen.
	 * <code>./reward_settings.properties</code>
	 * 
	 * @see #showRewardDialog(String, int, String, String, boolean)
	 */
	public static void showRewardDialog(String gameName, int points, String playerName) throws RemoteException,
		MalformedURLException, NotBoundException, PlayerNameException
	{

		String ipAdressServer = Settings.getString("ip_address_server", "localhost");

		showRewardDialog(gameName, points, playerName, ipAdressServer);
	}

	/**
	 * Versucht alle nicht angegebenen Werte aus der Propertiesdatei zu lesen.
	 * <code>./reward_settings.properties</code>
	 * 
	 * @see #showRewardDialog(String, int, String, String, boolean)
	 */
	public static void showRewardDialog(String gameName, int points, String playerName, String ipAdressServer)
		throws RemoteException, MalformedURLException, NotBoundException, PlayerNameException
	{
		boolean securityManager = Settings.getBoolean("install_security_manager", false);

		showRewardDialog(gameName, points, playerName, ipAdressServer, securityManager);
	}

	/**
	 * Speichert einen Punktestand auf dem Server. Fehler werden mit {@link ExceptionDialog}
	 * visualisiert und weitergereicht.<br>
	 * Mögliche Fehler bei der Eingabe des Spielernamens werden mit einer
	 * {@link PlayerNameException} quittiert.
	 */
	public static void showRewardDialog(String gameName, int points, String playerName, String ipAdressServer,
		boolean securityManager) throws RemoteException, MalformedURLException, NotBoundException, PlayerNameException
	{

		CreditAccessClient accessClient = null;
		try
		{
			accessClient = new CreditAccessClient(ipAdressServer, securityManager);
			accessClient.initConnectionToServer();
		}
		catch (Exception e)
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
		catch (Exception e)
		{

			ExceptionDialog.showExceptionDialog(null, "Verbindungsfehler",
				"Beim senden der Punkte ist ein Fehler aufgetreten.", e);
			throw (e);
		}
	}
}
