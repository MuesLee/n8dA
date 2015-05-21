package de.kvwl.n8dA.infrastructure.commons.interfaces;

import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;

public interface HighscoreListVisualizer {

	public void showHighscoreList(String title, List<HighscoreEntry> entries);
	
}
