package de.kvwl.n8dA.robotwars.server.input;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
}
