package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

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
	protected void paint(Graphics2D arg0, long arg1)
	{

	}

}
