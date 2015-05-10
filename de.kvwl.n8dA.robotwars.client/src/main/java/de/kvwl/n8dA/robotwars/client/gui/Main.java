package de.kvwl.n8dA.robotwars.client.gui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.infrastructure.commons.interfaces.CreditAccess;
import de.kvwl.n8dA.infrastructure.rewards.client.CreditAccessClient;
import de.kvwl.n8dA.robotwars.client.RoboBattlePlayerClient;
import de.kvwl.n8dA.robotwars.client.gui.LoginDialog.CanceledException;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static String SOURCE_FOLDER = "../data";

	public static void main(String[] args) {

		setLaF();

		RoboBattlePlayerClient battleClient = createBattleClient();
		battleClient.sendPlayerIsReadyToBattleToServer();

		SOURCE_FOLDER = battleClient.getProperty("SOURCE_FOLDER");

		LoginResult result = getCreditPoints(battleClient);

		ClientFrame clientFrame = new ClientFrame(battleClient,
				result.getCredits(), result.getPlayerName());
		clientFrame.setVisible(true);
	}

	private static LoginResult getCreditPoints(
			RoboBattlePlayerClient battleClient) {
		String rewardServerIpAdress = battleClient
				.getProperty("REWARD_SERVER_IP_ADDRESS");
		CreditAccess creditClient = new CreditAccessClient(rewardServerIpAdress);
		try {
			creditClient.initConnectionToServer();
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Die Verbindung zum Punkteserver konnte nicht aufgebaut werden. Standardwerte werden benutzt.\n"
									+ e.getMessage(),
							"Fehler beim Verbindungsaufbau",
							JOptionPane.ERROR_MESSAGE);

			LOG.debug("Default credit points: {}",
					battleClient.getProperty("DEFAULT_CREDITS"));

			LoginResult result = new LoginResult();
			result.setCredits(Long.valueOf(battleClient
					.getProperty("DEFAULT_CREDITS")));
			result.setPlayerName("unregistered");

			return result;
		}

		try {
			return LoginDialog.getCreditPoints(creditClient);
		} catch (CanceledException e) {
			throw new RuntimeException(e);
		}
	}

	private static RoboBattlePlayerClient createBattleClient() {
		RoboBattlePlayerClient client;
		try {
			client = new RoboBattlePlayerClient();
			createShutdownHook(client);

			client.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Die Verbindung zum Spieleserver konnte nicht aufgebaut werden. \n"
							+ e.getMessage(), "Fehler beim Verbindungsaufbau",
					JOptionPane.ERROR_MESSAGE);

			throw new RuntimeException(e);
		}

		return client;
	}

	private static void setLaF() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
	}

	private static void createShutdownHook(
			final RoboBattlePlayerClient battleClient) {

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {

				LOG.debug("ShutdownHook -> Force close");
				try {
					battleClient.disconnectFromServer();
				} catch (Throwable e) {
				}
			}
		}));
	}
}
