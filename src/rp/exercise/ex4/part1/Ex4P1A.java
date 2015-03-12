package rp.exercise.ex4.part1;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.KillMeNow;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;

import java.util.List;

import javax.swing.JFrame;

public class Ex4P1A {
	public void run() {
		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());
		RPLineMap lineMap = MapUtils.create2015Map1();

		GridMap gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);

		// view the map with 2 pixels as 1 cm
		GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, lineMap, 2, true);
		List<Node<Coordinate>> path = AStar.findPathFrom(gridMap.getNodeAt(0, 0), gridMap.getNodeAt(11, 7), SearchFunction.euclidean, SearchFunction.manhattan);
		System.out.println(path);
		// List<Node<Coordinate>> path = new ArrayList<Node<Coordinate>>();

		mapVis.setPath(path);

		frame.add(mapVis);
		frame.setSize(820, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		Ex4P1A demo = new Ex4P1A();
		demo.run();
	}
}
