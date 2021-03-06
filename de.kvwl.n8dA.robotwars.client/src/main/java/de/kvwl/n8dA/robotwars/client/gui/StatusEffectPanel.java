package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;

public class StatusEffectPanel extends JPanel {

	private static final long serialVersionUID = -4849306473451426607L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private List<StatusEffect> statusEffects;
	private String title;

	public void update(List<StatusEffect> statusEffects) {
		
		this.setStatusEffects(statusEffects);
		
		removeAll();
		buildPanel();
		revalidate();
		
	}

	private void buildStatusBars() {
		for (StatusEffect statusEffect : getStatusEffects()) {
			JProgressBar statusBar = buildStatusBar(statusEffect);
			JLabel labelStatusBar = buildLabelForStatusBar(statusEffect);
			labelStatusBar.setLabelFor(statusBar);
			add(labelStatusBar);
			add(statusBar);
		}

	}

	private JLabel buildLabelForStatusBar(StatusEffect statusEffect) {

		JLabel label = new JLabel(new ImageIcon(ImageUtils.getScaledInstance(
				InternalImage.loadFromPath(IMAGE_PATH,
						statusEffect.getIconName()), 20, 20, null)));
		String text = getTextForStatusBarLabel(statusEffect);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setText(text);
		return label;
	}

	private JProgressBar buildStatusBar(StatusEffect statusEffect) {

		JProgressBar statusBar = new JProgressBar(0, 5);
		statusBar.setStringPainted(true);
		setValueForStatusBar(statusBar, statusEffect);
		statusBar.setForeground(getStatusBarColor(statusEffect));
		statusBar.setUI(new BasicProgressBarUI() {
		      protected Color getSelectionBackground() { return Color.black; }
		      protected Color getSelectionForeground() { return Color.black; }
		    });
		return statusBar;
	}
	
	private String getTextForStatusBarLabel(StatusEffect statusEffect)
	{
		return statusEffect.getModifierText();
	}

	private Color getStatusBarColor(StatusEffect statusEffect) {
		
		return statusEffect.isPositive() ? Color.green : Color.RED;
		
	}

	private void setValueForStatusBar(JProgressBar statusBar,
			StatusEffect statusEffect) {
		String text = null;
		int value = 5;
		int roundsLeft = statusEffect.getRoundsLeft() + 1;
		if (roundsLeft > 5) {
			text = "5+";
		} else {
			value = roundsLeft+1;
			text = Integer.toString(roundsLeft);
		}

		statusBar.setValue(value);
		statusBar.setString(text);
	}

	private void buildPanel() {
		buildStatusBars();
		
		setBorder(	BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(
						BorderFactory.createLineBorder(Color.BLACK, 2, true),
						title), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		
	}


	public StatusEffectPanel(List<StatusEffect> statusEffects, String title) {
		super();
		this.setStatusEffects(statusEffects);
		this.title = title;
		GridLayout mgr = new GridLayout(0, 2);
		setLayout(mgr);
		buildPanel();
	}

	public List<StatusEffect> getStatusEffects() {
		return statusEffects;
	}

	public void setStatusEffects(List<StatusEffect> statusEffects) {
		this.statusEffects = statusEffects;
	}
}
