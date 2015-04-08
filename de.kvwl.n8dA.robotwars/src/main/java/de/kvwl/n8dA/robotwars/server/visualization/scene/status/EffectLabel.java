package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import game.engine.image.InternalImage;
import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;

//TODO Marvin: Effect Anzeige
public class EffectLabel extends SceneObject
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private Map<RobotActionType, StatusEffect> effects = new HashMap<RobotActionType, StatusEffect>();

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

		double _width = getWidth() * (1.0 / 3.0);
		double _height = getHeight();

		_height = _width = Math.min(_width, _height);

		int _x;
		int _y = 0;

		double _space = _width * 0.1;

		if (effects.containsKey(RobotActionType.PAPER))
		{

			Image img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Paper.png");
			_x = 0;

			g2d.drawImage(img, _x, _y, (int) (_x + _width), (int) (_y + _height), 0, 0, img.getWidth(null),
				img.getHeight(null), null);
		}

		if (effects.containsKey(RobotActionType.ROCK))
		{

			Image img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Rock.png");
			_x = (int) (_width + _space);

			g2d.drawImage(img, _x, _y, (int) (_x + _width), (int) (_y + _height), 0, 0, img.getWidth(null),
				img.getHeight(null), null);
		}

		if (effects.containsKey(RobotActionType.SCISSOR))
		{

			Image img = InternalImage.loadFromPath(IMAGE_PATH, "stat_Scissor.png");
			_x = (int) ((_width + _space) * 2);

			g2d.drawImage(img, _x, _y, (int) (_x + _width), (int) (_y + _height), 0, 0, img.getWidth(null),
				img.getHeight(null), null);
		}
	}

	public void setEffect(RobotActionType type, StatusEffect effect)
	{

		effects.put(type, effect);
	}

	public void reset()
	{

		effects.clear();
	}
}
