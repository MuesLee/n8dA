package de.kvwl.n8dA.robotwars.server.visualization.java.scene;

import game.engine.frame.SwingGameFrame;
import game.engine.stage.scene.FPSScene;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.CachedLabelSceneObject;
import game.engine.stage.scene.object.LabelSceneObject;
import game.engine.stage.scene.object.Point;
import game.engine.time.TimeUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.EventListener;

import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.AnimatedSceneObject;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.Animation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.CompoundAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.MoveAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.RotateAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.ScaleAnimation;

public class AnimationTest
{

	public static void main(String[] args) throws Exception
	{

		Scene scene = getScene();

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(new FPSScene(scene));

		disp.setVisible(true);
	}

	private static Scene getScene() throws IOException
	{

		final LabelSceneObject lbl = new CachedLabelSceneObject();
		lbl.setTopLeftPosition(new Point(0, 0));
		lbl.setText("Animation...");

		lbl.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
			new float[] { 4, 4, 6 }, 0));
		lbl.setOutlineColor(Color.RED);
		lbl.setColor(Color.GREEN);

		// new ScaleAnimation(0,
		// 1.0, TimeUtils.NanosecondsOfSeconds(2))

		long animationTime = TimeUtils.NanosecondsOfSeconds(1);
		Animation animation = new CompoundAnimation(new RotateAnimation(0, Math.PI * 2, true, animationTime),
			new ScaleAnimation(0, 1, animationTime), new MoveAnimation(0.1, 0.1, TimeUtils.NanosecondsOfSeconds(2)));

		final AnimatedSceneObject ani = new AnimatedSceneObject(lbl, animation);

		ani.setTopLeftPosition(new Point(0, 0));

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{

				while (true)
				{
					ani.startAnimation(true);
					System.out.println("Ani Ende");
				}
			}
		}).start();

		final Scene scene = new Scene()
		{

			@Override
			public void paintScene(Graphics2D g, int width, int height, long time)
			{
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, width, height);

				ani.setSize(width, height);
				ani.paintOnScene(g, time);
			}

			@Override
			public EventListener[] getEventListeners()
			{
				return new EventListener[] { new KeyAdapter()
				{
					@Override
					public void keyReleased(KeyEvent e)
					{

						if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
						{

							System.exit(0);
						}
					}
				} };
			}
		};

		return scene;
	}
}
