package de.kvwl.n8dA.robotwars.server.input;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public class DataLoaderFileSystemImplTest {

	DataLoaderFileSystemImpl dao;

	@Before
	public void setUp() throws Exception {

		dao = new DataLoaderFileSystemImpl(
				Paths.get("./src/test/resources/data/"));
	}

	@Test
	public void readAniXML() throws Exception {

		Animation ani = dao.readAnimation(Paths
				.get("./src/test/resources/animations/test.xml"));

		assertEquals("AnimationId", ani.getId());
		assertEquals(64, ani.getFrameWidth());
		assertEquals(128, ani.getFrameHeight());

		long[] frameTimings = ani.getFrameTimings();
		long[] should = new long[] { 100, 150 };

		for (int i = 0; i < should.length; i++) {

			assertEquals(should[i], frameTimings[i]);
		}

		// System.out.println(ani.getPathToFile());
	}

	@Test
	public void readAnimationTestFilesystem() throws Exception {

		List<Animation> robots = dao.loadAnimationsForRobots();
		List<Animation> actions = dao.loadAnimationsForRobotActions();

		assertEquals(2, robots.size());
		assertEquals(2, actions.size());

		boolean exist = false;
		for (Animation aR : robots) {

			if (aR.getId().equals("Alienrobot")) {
				exist = true;
				break;
			}
		}
		assertTrue(exist);

		exist = false;
		for (Animation aA : actions) {

			if (aA.getId().equals("Kissen")) {
				exist = true;
				break;
			}
		}
		assertTrue(exist);
	}

	@Test
	public void loadRobotAttacksTest() throws Exception {

		List<Attack> attacks = dao.loadRobotAttacks();
		assertEquals(1, attacks.size());

		Attack attack = attacks.get(0);
		assertEquals(64, attack.getAnimation().getFrameWidth());
	}

	@Test
	public void loadRobotDefendsTest() throws Exception {

		List<Defense> defends = dao.loadRobotDefends();
		assertEquals(1, defends.size());

		Defense defense = defends.get(0);
		assertEquals(64, defense.getAnimation().getFrameWidth());
	}

	@Test
	public void loadUserRobotsTest() throws Exception {

		List<Robot> list = dao.loadUserRobots("user123");

		assertEquals(1, list.size());

		Robot robot = list.get(0);
		assertEquals("UserMetalrobot", robot.getName());
	}

	@Test
	public void loadRobotsTest() throws Exception {

		List<Robot> robos = dao.loadRobots();
		assertEquals(2, robos.size());

		for (Robot ro : robos) {

			assertNotNull(ro.getAnimation());
		}
	}
}
