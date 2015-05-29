package de.kvwl.n8dA.infrastructure.rewards.client.ctrl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

		Object source = e.getSource();

		if (source == btnEditRecord)
		{

			editRecord();
		}

	}

	private void editRecord()
	{

		String userName = "";

		String name = JOptionPane.showInputDialog(this, "Enter user name - Edit record", userName);
		int points = 0;

		client.overwriteRecord();
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
		table.setAutoCreateRowSorter(true);

		scrollPane.setViewportView(table);

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);

		btnEditRecord = new JButton("Edit Record");
		btnEditRecord.addActionListener(this);
		btnEditRecord.setEnabled(game != null);
		toolBar.add(btnEditRecord);
	}

}
