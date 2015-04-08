package de.kvwl.n8dA.robotwars.server.visualization.scene;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffectModificationType;
import de.kvwl.n8dA.robotwars.server.visualization.Position;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.ActionType;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.RobotScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;
import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;
import game.engine.image.sprite.DefaultSprite;
import game.engine.stage.scene.FPSScene;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.time.TimeUtils;

public class SceneTest
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/server/images/";

	public static void main(String[] args) throws Exception
	{

		Scene scene = getRoboScene();

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(new FPSScene(scene));

		disp.setVisible(true);
	}

	private static Scene getRoboScene() throws IOException
	{

		final Queue<Integer> anis = new LinkedList<Integer>();

		final StatusScene stats = new StatusScene();
		final RobotScene scene = new RobotScene()
		{

			@Override
			public void paintScene(Graphics2D g2d, int width, int height, long elapsedTime)
			{

				Image bg = InternalImage.loadFromPath(IMAGE_PATH, "arena_bg.png");
				g2d.drawImage(bg, 0, 0, width, height, 0, 0, bg.getWidth(null), bg.getHeight(null), null);

				super.paintScene(g2d, width, height, elapsedTime);

				List<StatusEffect> effects = new ArrayList<StatusEffect>();
				effects.add(new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.RESISTANCE, 2));
				effects.add(new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 1));
				effects.add(new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.VULNERABILITY, 4));
				stats.setEffects(Position.LEFT, effects);
				stats.paintScene(g2d, width, height, elapsedTime);

			}

			@Override
			public EventListener[] getEventListeners()
			{

				return new EventListener[] { new KeyAdapter()
				{
					public void keyReleased(java.awt.event.KeyEvent e)
					{

						synchronized (anis)
						{

							anis.add(e.getKeyCode());
						}
					};
				} };
			}
		};

		Thread animations = new Thread(new Runnable()
		{

			@Override
			public void run()
			{

				while (true)
				{
					Integer anim;

					synchronized (anis)
					{
						anim = anis.poll();
					}

					if (anim == null)
					{
						continue;
					}

					switch (anim.intValue())
					{
						case KeyEvent.VK_LEFT:
							scene.playDamageAnimation(Position.LEFT, true);
						break;
						case KeyEvent.VK_RIGHT:
							scene.playDamageAnimation(Position.RIGHT, true);
						break;
						case KeyEvent.VK_UP:
							stats.startHealthPointAnimation(Position.LEFT, Math.random() * 50, true);
							stats.startEnergyPointAnimation(Position.RIGHT, Math.random() * 50 + 50, true);
						break;
						case KeyEvent.VK_DOWN:
							stats.startHealthPointAnimation(Position.LEFT, Math.random() * 50 + 50, true);
							stats.startEnergyPointAnimation(Position.RIGHT, Math.random() * 50, true);
						break;
						case KeyEvent.VK_SPACE:
							Action acLeft = null;
							Action acRight = null;
							try
							{
								acRight = new Action(new AnimatedSceneObject(new DefaultSprite(ImageIO.read(new File(
									"../data/animations/actions/defends/Tennis/animation.png")), 64, 128),
									TimeUtils.NanosecondsOfMilliseconds(100)), ActionType.ReflectingDefense);

								acLeft = new Action(new AnimatedSceneObject(new DefaultSprite(ImageIO.read(new File(
									"../data/animations/actions/attacks/Rakete/animation.png")), 64, 64),
									TimeUtils.NanosecondsOfMilliseconds(100)), ActionType.Attack);
								acLeft.invert();
							}
							catch (IOException e)
							{
							}
							scene.playActionAnimation(acLeft, acRight, true);
						break;
					}
				}
			}
		});
		animations.start();

		scene.setRobo(
			new AnimatedSceneObject(new DefaultSprite(ImageIO.read(new File(
				"../data/animations/robots/RadRoboter/animation.png")), 64, 128), TimeUtils
				.NanosecondsOfMilliseconds(100)), Position.LEFT);

		scene.setRobo(
			new AnimatedSceneObject(new DefaultSprite(ImageIO.read(new File(
				"../data/animations/robots/KosmosRoboter/animation.png")), 64, 128), TimeUtils
				.NanosecondsOfMilliseconds(100)), Position.RIGHT);

		return scene;
	}

	@SuppressWarnings("unused")
	private static Scene getGameScene()
	{

		return new GameScene();
	}

	@SuppressWarnings("unused")
	private static Scene getStatusScene()
	{

		final StatusScene scene = new StatusScene()
		{

			@Override
			public void paintScene(Graphics2D g2d, int width, int height, long elapsedTime)
			{

				Image bg = InternalImage.loadFromPath(IMAGE_PATH, "arena_bg.png");
				g2d.drawImage(bg, 0, 0, width, height, 0, 0, bg.getWidth(null), bg.getHeight(null), null);

				super.paintScene(g2d, width, height, elapsedTime);
			}
		};

		new Thread(new Runnable()
		{

			int value = 100;
			double round = 0;

			@Override
			public void run()
			{

				while (true)
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
					}

					round += 0.25;
					if (round >= 21)
					{
						round = 1;
					}

					value -= 1;
					if (value < 0)
					{
						value = 100;
					}

					if (Math.random() > 0.5)
					{

						scene.setEnergyPoints(Position.RIGHT, value);
						scene.setHealthPoints(Position.RIGHT, value);
					}

					scene.setRound((int) round);

					scene.setEnergyPoints(Position.LEFT, value);
					scene.setHealthPoints(Position.LEFT, value);
				}
			}
		}).start();

		return scene;
	}

}
