package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import game.engine.stage.scene.object.SceneObject;

public class EffectLabel extends SceneObject
{

	private Map<RobotActionType, StatusEffect> effects = new HashMap<RobotActionType, StatusEffect>();

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

		g2d.setColor(Color.MAGENTA);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}

	public void setEffect(RobotActionType type, StatusEffect effect)
	{

		effects.put(type, effect);
	}
}
