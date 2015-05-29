package de.kvwl.n8dA.infrastructure.rewards.client.ctrl;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.commons.entity.Person;

public class GamePanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel model;

	public GamePanel()
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
		scrollPane.setViewportView(table);

	}

	public void addRecord(GamePerson user)
	{

		Person person = user.getPerson();
		model.addRow(new Object[] { person.getName(), user.getPoints() });
	}

}
