package de.kvwl.n8dA.infrastructure.rewards.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;
import de.kvwl.n8dA.infrastructure.rewardserver.server.RewardServer;

public class HighscoreView {
	
	private RewardServer rewardServer;
	
	private JFrame frame;

	private JTable jTable;
	private HighscoreTableModel tableModel;
	
	private JPanel mainPanel; 

	public HighscoreView(RewardServer rewardServer) {
		super();
		this.setRewardServer(rewardServer);
		
		mainPanel = createMainPanel();
		configureFrame();
		configureJTable();
		buildView();
		frame.setVisible(true);
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		return panel;
	}

	private void buildView() {
		mainPanel.add(jTable,BorderLayout.CENTER);
		frame.add(mainPanel, BorderLayout.CENTER);
	
		frame.pack();
	}

	public RewardServer getRewardServer() {
		return rewardServer;
	}

	public void setRewardServer(RewardServer rewardServer) {
		this.rewardServer = rewardServer;
	}
	
	private void configureFrame()
	{
		frame = new  JFrame("Highscores");
		frame.setLayout(new BorderLayout());
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
	}	
	private void configureJTable()
	{
		ArrayList<GamePerson> data = new ArrayList<GamePerson>();
		
		String[] columnNames = new String[]{"Nickname", "Punkte"};
		
		tableModel = new HighscoreTableModel(columnNames, data);
		jTable = new JTable(tableModel);
		jTable.setCellSelectionEnabled(false);
	}
	
	public void updateData (List<GamePerson> data)
	{
		tableModel.setData(data);
		tableModel.fireTableDataChanged();
	}
}
