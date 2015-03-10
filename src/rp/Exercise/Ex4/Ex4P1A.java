package rp.Exercise.Ex4;

import rp.Exercise.Ex4.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.KillMeNow;
import search.Coordinate;
import search.Node;

import java.util.ArrayList;
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

		// view the map with 2 pixels as 1 cm
		GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, lineMap, 2, true);
		// List<Node<Coordinate>> path = AStar.findPathFrom(gridMap.getNodeAt(0, 0), gridMap.getNodeAt(6, 4), SearchFunction.euclidean, SearchFunction.manhattan);
		List<Node<Coordinate>> path = new ArrayList<Node<Coordinate>>();
		path.add(new Node<Coordinate>(new Coordinate(0, 0)));
		path.add(new Node<Coordinate>(new Coordinate(0, 1)));
		path.add(new Node<Coordinate>(new Coordinate(1, 1)));
		path.add(new Node<Coordinate>(new Coordinate(1, 2)));
		path.add(new Node<Coordinate>(new Coordinate(2, 2)));
		path.add(new Node<Coordinate>(new Coordinate(3, 2)));
		path.add(new Node<Coordinate>(new Coordinate(4, 2)));
		path.add(new Node<Coordinate>(new Coordinate(5, 2)));
		path.add(new Node<Coordinate>(new Coordinate(6, 2)));
		path.add(new Node<Coordinate>(new Coordinate(7, 2)));
		path.add(new Node<Coordinate>(new Coordinate(7, 3)));
		path.add(new Node<Coordinate>(new Coordinate(7, 4)));
		path.add(new Node<Coordinate>(new Coordinate(7, 5)));
		path.add(new Node<Coordinate>(new Coordinate(7, 6)));
		path.add(new Node<Coordinate>(new Coordinate(8, 6)));
		path.add(new Node<Coordinate>(new Coordinate(9, 6)));

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
