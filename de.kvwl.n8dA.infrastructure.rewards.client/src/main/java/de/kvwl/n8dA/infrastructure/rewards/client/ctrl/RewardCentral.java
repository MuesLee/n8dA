package de.kvwl.n8dA.infrastructure.rewards.client.ctrl;

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.rewards.client.CreditAccessClient;

public class RewardCentral extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;

	private BasicCreditAccess client;

	public RewardCentral(BasicCreditAccess client)
	{
		this.client = client;

		createGui();
		pack();
	}

	private void createGui()
	{

		setTitle("RewardCentral");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnGame = new JMenu("Game");
		menuBar.add(mnGame);

		JMenuItem mntmCreate = new JMenuItem("Create");
		mnGame.add(mntmCreate);

		JMenuItem mntmDelete = new JMenuItem("Delete");
		mnGame.add(mntmDelete);

		JMenuItem mntmClear = new JMenuItem("Clear");
		mnGame.add(mntmClear);

		try
		{
			refresh();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	private void refresh() throws RemoteException
	{

		tabbedPane.removeAll();

		JPanel pnlAllUser = new GamePanel();
		tabbedPane.addTab("User", null, pnlAllUser, null);

		List<Game> games = client.getAllGames();
		for (Game game : games)
		{

			createGameTab(game);
		}
	}

	private void createGameTab(Game game) throws RemoteException
	{

		GamePanel pnlGame = new GamePanel();
		tabbedPane.addTab(game.getName(), null, pnlGame, null);

		List<GamePerson> users = client.getAllGamePersonsForGame(game.getName());
		for (GamePerson user : users)
		{

			pnlGame.addRecord(user);
		}
	}

	public static void main(String[] args)
	{

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		RewardCentral rewardCentral = new RewardCentral(new CreditAccessClient("localhost"));
		rewardCentral.setVisible(true);
	}
}
