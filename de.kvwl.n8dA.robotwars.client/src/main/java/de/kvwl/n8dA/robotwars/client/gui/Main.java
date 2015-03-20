package de.kvwl.n8dA.robotwars.client.gui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.client.gui.LoginDialog.CanceledException;
import de.kvwl.n8da.infrastructure.rewards.client.CreditAccessClient;

public class Main
{

	public static String SOURCE_FOLDER = "./data";

	public static void main(String[] args)
	{

		setLaF();

		RoboBattlePlayerClient battleClient = createBattleClient();
		battleClient.sendPlayerIsReadyToBattleToServer();

		SOURCE_FOLDER = battleClient.getProperty("SOURCE_FOLDER");

		long maxCreditPoints = getCreditPoints(battleClient);

		ClientFrame clientFrame = new ClientFrame(battleClient, maxCreditPoints);
		clientFrame.setVisible(true);
	}

	private static long getCreditPoints(RoboBattlePlayerClient battleClient)
	{
		String rewardServerIpAdress = battleClient.getProperty("REWARD_SERVER_IP_ADDRESS");
		CreditAccess creditClient = new CreditAccessClient(rewardServerIpAdress);
		try
		{
			creditClient.initConnectionToServer();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(
				null,
				"Die Verbindung zum Punkteserver konnte nicht aufgebaut werden. Standardwerte werden benutzt.\n"
					+ e.getMessage(), "Fehler beim Verbindungsaufbau", JOptionPane.ERROR_MESSAGE);

			return Long.valueOf(battleClient.getProperty("DEFAULT_CREDITS"));
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
