package de.kvwl.n8dA.infrastructure.controller;

import java.util.Timer;

import de.kvwl.n8dA.infrastructure.commons.interfaces.BasicCreditAccess;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import de.kvwl.n8dA.infrastructure.rewards.gui.HighscoreListVisualizerImpl;

public class HighscoreController {

	
	Timer timer;
	BasicCreditAccess creditAccess;
	HighscoreListVisualizer visualizer;
	
	public HighscoreController() {
	init();
	
	}	

	private void init()
	{
		timer = new Timer("HighscoreListSwitch-Timer");
		visualizer = new HighscoreListVisualizerImpl() {
		};
	}
	
}
