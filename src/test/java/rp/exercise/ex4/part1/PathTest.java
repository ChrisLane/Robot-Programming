package rp.exercise.ex4.part1;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;

import java.util.Random;

public class PathTest {
	private static GridMap gridMap;

	@DataProvider(name = "randomPositions")
	public static Object[][] randomPositions() {
		Object[][] nodes = new Object[10][2];
		Random random = new Random();
		int x = random.nextInt(gridMap.getXSize()), y = random.nextInt(gridMap.getYSize());

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 2; j++) {
				boolean shouldRun = true;
				while (shouldRun) {
					x = random.nextInt(gridMap.getXSize());
					y = random.nextInt(gridMap.getYSize());

					if (!gridMap.isObstructed(x, y)) {
						shouldRun = false;
						nodes[i][j] = gridMap.getNodeAt(x, y);
					}
				}

			}
		}

		return nodes;
	}

	@Test(dataProvider = "randomPositions")
	public static void searchTest(Node<Coordinate> start, Node<Coordinate> goal) {
		Assert.assertNotNull(AStar.findPathFrom(start, goal, SearchFunction.euclidean, SearchFunction.manhattan, null));
	}

	@BeforeClass
	public void setUp() {
		RPLineMap lineMap = MapUtils.create2015Map1();
		gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);
	}
}
