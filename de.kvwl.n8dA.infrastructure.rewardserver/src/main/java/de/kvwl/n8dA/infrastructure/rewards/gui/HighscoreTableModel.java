package de.kvwl.n8dA.infrastructure.rewards.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;


public class HighscoreTableModel extends AbstractTableModel{

	
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames;
	private List<GamePerson> data;
	
	
	
	public HighscoreTableModel(String[] columnNames, List<GamePerson> data) {
		super();
		this.columnNames = columnNames;
		this.setData(data);
	}

	@Override
	public int getRowCount() {
		return getData().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		GamePerson gamePerson = getData().get(rowIndex);
		
		if(gamePerson == null || gamePerson.getPerson() == null || gamePerson.getGame() == null)
		{
			return "";
		}
		
		String value = null;
		
		switch (columnIndex) {
		case 0:
			value = gamePerson.getPerson().getName();
			break;
		case 1:
			value = Integer.toString(gamePerson.getPoints());
			break;
		case 2:
			value = gamePerson.getGame().getName();
			break;
		default:
			value = "";
			break;
		}
		
		
		return value;
	}

	public List<GamePerson> getData() {
		return data;
	}

	public void setData(List<GamePerson> data) {
		this.data = data;
	}

}
