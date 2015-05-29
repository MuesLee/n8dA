package de.kvwl.n8dA.infrastructure.rewards.client.ctrl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import de.kvwl.n8dA.infrastructure.commons.entity.Game;
import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.Person;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;

public class GamePanel extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;

	private BasicCreditAccess client;

	private JTable table;
	private DefaultTableModel model;
	private Game game;
	private JButton btnRefresh;

	private JButton btnEditRecord;

	public GamePanel(BasicCreditAccess client)
	{

		this(null, client);
	}

	public GamePanel(Game game, BasicCreditAccess client)
	{
		this.game = game;
		this.client = client;
		setName((game != null) ? game.getName() : "");

		createGui();
	}

	public void addRecord(GamePerson user)
	{

		Person person = user.getPerson();
		model.addRow(new Object[] { person.getName(), user.getPoints() });
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		try
		{
			Object source = e.getSource();

			if (source == btnEditRecord)
			{

				editRecord();
			}
			else if (source == btnRefresh)
			{

				refresh();
			}
		}
		catch (RemoteException e1)
		{
			e1.printStackTrace();
		}

	}

	private void editRecord() throws RemoteException
	{

		if (game == null || game.getName() == null)
		{
			return;
		}

		String userName = "";
		String userPoints = "0";

		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0)
		{
			int modelRowIndex = table.convertRowIndexToModel(selectedRow);
			userName = model.getValueAt(modelRowIndex, 0).toString();

			userPoints = model.getValueAt(modelRowIndex, 1).toString();
		}

		String name = JOptionPane.showInputDialog(this, "Enter user name - Edit record", userName);
		int points = Integer.valueOf(JOptionPane.showInputDialog(this, "Enter points for " + name + " - Edit record",
			userPoints));

		if (name == null || name.isEmpty() || points < 0)
		{
			return;
		}

		client.overwriteRecord(name, game.getName(), points);
	}

	public void refresh() throws RemoteException
	{
		if (game == null || game.getName() == null)
		{
			return;
		}

		while (model.getRowCount() > 0)
		{
			model.removeRow(0);
		}

		List<GamePerson> persons = client.getAllGamePersonsForGame(game.getName());

		for (GamePerson person : persons)
		{

			addRecord(person);
		}
	}

	private void createGui()
	{
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		model = new DefaultTableModel(new Object[][] {}, new String[] { "Name", "Points" })
		{
			private static final long serialVersionUID = 1L;
			Class<?>[] columnTypes = new Class[] { String.class, Long.class };

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		};

		table = new JTable();
		table.setModel(model);
		table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);

		scrollPane.setViewportView(table);

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(this);
		btnRefresh.setEnabled(game != null);
		toolBar.add(btnRefresh);

		btnEditRecord = new JButton("Edit Record");
		btnEditRecord.addActionListener(this);
		btnEditRecord.setEnabled(game != null);
		toolBar.add(btnEditRecord);
	}

}
