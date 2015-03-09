package rp.Exercise.Ex4;

import rp.Exercise.Ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.KillMeNow;
import search.AStar;
import search.Node;
import search.SearchFunction;

import lejos.geom.Point;

import java.util.List;

import javax.swing.JFrame;

public class Ex4P1A {

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

		GridMap gridMap = new GridMap(xJunctions, yJunctions, xInset, yInset, junctionSeparation, lineMap);

		int x = 1, y = 1;

		System.out.println("distance PLUS_Y (down): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.PLUS_Y)));
		System.out.println("distance PLUS_X (right): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.PLUS_X)));
		System.out.println("distance MINUS_Y (up): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.MINUS_Y)));
		System.out.println("distance MINUS_X (left): " + gridMap.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(Heading.MINUS_X)));

		// view the map with 2 pixels as 1 cm
		GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, lineMap, 2);
		List<Node<Point>> path = AStar.findPathFrom(gridMap.getNodeAt(0, 0), gridMap.getNodeAt(6, 3), SearchFunction.euclidean, SearchFunction.manhattan);
		mapVis.setPath(path);

		frame.add(mapVis);
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		Ex4P1A demo = new Ex4P1A();
		demo.run();
	}
}
