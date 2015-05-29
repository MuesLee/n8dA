package de.kvwl.n8dA.infrastructure.rewards.client.ctrl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.Person;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.rewards.client.CreditAccessClient;
import de.kvwl.n8dA.infrastructure.rewards.client.Settings;

public class RewardCentral extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;

	private BasicCreditAccess client;

	private JTabbedPane tabbedPane;
	private JMenuItem miGameCreate;
	private JMenuItem miGameDelete;
	private JMenuItem miGameClear;
	private JMenu mFile;
	private JMenuItem miRefresh;
	private JMenu mUser;
	private JMenuItem miDeleteUser;

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

		mFile = new JMenu("File");
		menuBar.add(mFile);

		miRefresh = new JMenuItem("Refresh");
		miRefresh.addActionListener(this);
		mFile.add(miRefresh);

		JMenu mGame = new JMenu("Game");
		menuBar.add(mGame);

		miGameCreate = new JMenuItem("Create");
		miGameCreate.addActionListener(this);
		mGame.add(miGameCreate);

		miGameDelete = new JMenuItem("Delete");
		miGameDelete.addActionListener(this);
		mGame.add(miGameDelete);

		miGameClear = new JMenuItem("Clear");
		miGameClear.addActionListener(this);
		mGame.add(miGameClear);

		mUser = new JMenu("User");
		menuBar.add(mUser);

		miDeleteUser = new JMenuItem("Delete User");
		miDeleteUser.addActionListener(this);
		mUser.add(miDeleteUser);

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

		createAllUserTab();

		List<Game> games = client.getAllGames();
		for (Game game : games)
		{

			createGameTab(game);
		}
	}

	private void createAllUserTab() throws RemoteException
	{
		GamePanel pnlAllUser = new GamePanel(client);
		tabbedPane.addTab("User", null, pnlAllUser, null);
		List<Person> persons = client.getAllPersons();
		for (Person person : persons)
		{

			long points = 0;
			List<GamePerson> games = client.getAllGamesForPersonName(person.getName());
			for (GamePerson gp : games)
			{
				points += gp.getPoints();
			}

			GamePerson gp = new GamePerson(new Game(), person, (int) Math.min(points, Integer.MAX_VALUE));

			pnlAllUser.addRecord(gp);
		}
	}

	private void createGameTab(Game game) throws RemoteException
	{

		GamePanel pnlGame = new GamePanel(game, client);
		tabbedPane.addTab(game.getName(), null, pnlGame, null);

		List<GamePerson> users = client.getAllGamePersonsForGame(game.getName());
		for (GamePerson user : users)
		{

			pnlGame.addRecord(user);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		try
		{
			Object source = e.getSource();

			if (source == miRefresh)
			{
				refresh();
			}
			else if (source == miGameCreate)
			{
				createGame();
			}
			else if (source == miGameDelete)
			{
				deleteGame();
			}
			else if (source == miGameClear)
			{
				clearGame();
			}
			else if (source == miDeleteUser)
			{
				deleteUser();
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	private void deleteUser() throws RemoteException
	{

		String name = JOptionPane.showInputDialog(this, "Enter user name - Delete");

		if (name == null || name.isEmpty())
		{
			return;
		}

		List<GamePerson> games = client.getAllGamesForPersonName(name);
		for (GamePerson game : games)
		{

			client.overwriteRecord(game.getPerson().getName(), game.getGame().getName(), 0);
		}

		client.deletePerson(name);
	}

	private void clearGame() throws RemoteException
	{

		String gameName = "";

		Component tab = tabbedPane.getSelectedComponent();
		if (tab != null && tab instanceof GamePanel)
		{
			GamePanel gp = (GamePanel) tab;
			gameName = gp.getName();
		}

		String name = JOptionPane.showInputDialog(this, "Enter game name - Clear", gameName);

		if (name == null || name.isEmpty())
		{
			return;
		}

		client.clearGame(name);
	}

	private void deleteGame() throws RemoteException
	{
		String gameName = "";

		Component tab = tabbedPane.getSelectedComponent();
		if (tab != null && tab instanceof GamePanel)
		{
			GamePanel gp = (GamePanel) tab;
			gameName = gp.getName();
		}

		String name = JOptionPane.showInputDialog(this, "Enter game name - Delete", gameName);

		if (name == null || name.isEmpty())
		{
			return;
		}

		client.deleteGame(name);
	}

	private void createGame() throws RemoteException
	{

		String name = JOptionPane.showInputDialog(this, "Enter game name - Create");

		if (name == null || name.isEmpty())
		{
			return;
		}

		client.createGame(name);
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException
	{

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		CreditAccessClient client = new CreditAccessClient(Settings.getString("ip_address_server", "localhost"),
			Settings.getBoolean("install_security_manager", false));
		client.initConnectionToServer();

		RewardCentral rewardCentral = new RewardCentral(client);
		rewardCentral.setVisible(true);
	}
}
