package rp.exercise.ex4.part1;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;

import java.util.Random;

public class PathTest {
	private static GridMap gridMap;

	@BeforeClass
	public void setUp() {
		RPLineMap lineMap = MapUtils.create2015Map1();
		gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);
	}

	@DataProvider(name = "randomPositions")
	public static Object[][] randomPositions() {
		Object[][] nodes = new Object[10][2];
		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(gridMap.getXSize());
			int y = random.nextInt(gridMap.getYSize());

			if (gridMap.isValidGridPosition(x, y)) {
				//nodes[i] = gridMap.getNodeAt(x, y);
			}
		}

		return new Object[0][];
	}
}
