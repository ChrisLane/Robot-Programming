package rp.Exercise.Ex4;

import rp.Exercise.Ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.KillMeNow;

import javax.swing.JFrame;

public class Ex4P1A {

	/***
	 * Create an instance of an object that implements IGridMap from a LineMap. You don't need to use this method, but it's a useful way for me to document the parameters you might need.
	 *
	 * @param _lineMap The underlying line map
	 * @param _gridXSize How many grid positions along the x axis
	 * @param _gridYSize How many grid positions along the y axis
	 * @param _xStart The x coordinate where grid position (0,0) starts
	 * @param _yStart The y coordinate where grid position (0,0) starts
	 * @param _cellSize The distance between grid positions
	 * @return
	 */
	public static IGridMap createGridMap(RPLineMap lineMap, int gridXSize, int gridYSize, float xStart, float yStart, float cellSize) {
		return new GridMap(gridXSize, gridYSize, xStart, yStart, cellSize, lineMap);
	}

	public void run() {
		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());
		RPLineMap lineMap = MapUtils.create2014Map2();

		// grid map dimensions for this line map
		int xJunctions = 10;
		int yJunctions = 7;
		float junctionSeparation = 30;

		// position of grid map 0,0
		int xInset = 14;
		int yInset = 31;

		IGridMap gridMap = createGridMap(lineMap, xJunctions, yJunctions, xInset, yInset, junctionSeparation);

		int x = 1, y = 1;

		System.out.println("distance PLUS_Y (down): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.PLUS_Y)));
		System.out.println("distance PLUS_X (right): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.PLUS_X)));
		System.out.println("distance MINUS_Y (up): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.MINUS_Y)));
		System.out.println("distance MINUS_X (left): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.MINUS_X)));

		// view the map with 2 pixels as 1 cm
		GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, lineMap, 2);

		frame.add(mapVis);
		frame.pack();
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Ex4P1A demo = new Ex4P1A();
		demo.run();
	}
}
