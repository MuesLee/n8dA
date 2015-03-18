package de.kvwl.n8dA.robotwars.client.gui;

import javax.swing.DefaultListSelectionModel;

public class DisabledItemSelectionModel extends DefaultListSelectionModel
{

	private static final long serialVersionUID = 1L;

	@Override
	public void setSelectionInterval(int index0, int index1)
	{
		super.setSelectionInterval(-1, -1);
	}
}
