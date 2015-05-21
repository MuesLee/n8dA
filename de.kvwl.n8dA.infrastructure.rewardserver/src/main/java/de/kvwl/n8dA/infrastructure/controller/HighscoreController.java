package de.kvwl.n8dA.infrastructure.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import de.kvwl.n8dA.infrastructure.rewards.gui.HighscoreListVisualizerImpl;

public class HighscoreController {

	
	private static final int _LIST_SWITCH_RATE_IN_MS = 10000;
	
	private static int CURRENT_LIST_INDEX =0;
	
	private List<HighscoreEntry> highscoreEntries;
	
	Timer timer;
	BasicCreditAccess server;
	HighscoreListVisualizer visualizer;
	
	
	
	public HighscoreController(BasicCreditAccess server) {
	this.server = server;
	init();
	
	}	

	private void init()
	{
		highscoreEntries = new ArrayList<>();
		timer = new Timer("HighscoreListSwitch-Timer");
		visualizer = new HighscoreListVisualizerImpl();
	}
	
	public void showHighscoreView() {
		startTimer();	
	}
	
	private void startTimer()
	{
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				switchHighscoreList();
			}

		
		}; 
		
		timer.schedule(task, 0, _LIST_SWITCH_RATE_IN_MS);
	}

	
	private void switchHighscoreList() {
	
	
		
		
	}
	
}
