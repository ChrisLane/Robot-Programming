package rp.exercise.ex4.part1;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.util.List;

public class Ex4P1A {
	private JFrame frame;

	private GridMapVisualisation mapVis;
	private RPLineMap lineMap;
	private GridMap gridMap;

	public void run() {
		lineMap = MapUtils.create2015Map1();
		gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);
		mapVis = new GridMapVisualisation(gridMap, lineMap, 2, true);

		List<Node<Coordinate>> path = AStar.findPathFrom(gridMap.getNodeAt(0, 0), gridMap.getNodeAt(11, 7), SearchFunction.euclidean, SearchFunction.manhattan, null);
		System.out.println(path);

		mapVis.setPath(path);

		frame = new JFrame("Map Viewer");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
