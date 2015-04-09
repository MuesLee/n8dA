package de.kvwl.n8dA.robotwars.client.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffectModificationType;

public class StatusEffectPanelTest {

	public static void main(String[] args) {
		List<StatusEffect> statusEffects = new ArrayList<>();
		List<StatusEffect> otherStatusEffects = new ArrayList<>();
		
		statusEffects.add(new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.RESISTANCE, 5));
		statusEffects.add(new TypeEffect(RobotActionType.WATER, TypeEffectModificationType.VULNERABILITY, 3));
		otherStatusEffects.add(new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.RESISTANCE, 5));
		otherStatusEffects.add(new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.VULNERABILITY, 0));
		
		StatusEffectPanel panel = new StatusEffectPanel(statusEffects, "Eigene Statuseffkte");
		
		JFrame frame = new JFrame();
		frame.setBounds(0, 0, 300, 300);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		
		int i = 0;
		while(true)
		{
			try {
				Thread.sleep(1000);
				
				if(i%2 == 0)
				{
					panel.update(statusEffects);
				}else {
					panel.update(otherStatusEffects);
				}
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
