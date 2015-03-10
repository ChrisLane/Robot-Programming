package rp.Exercise.Ex4;

import rp.GeoffBot;
import rp.Exercise.Ex4.mapping.GridMap;
import rp.Exercise.Ex4.control.PathFollower;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;

import java.util.List;

public class Ex4P1C {

	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2014Map2();
		GridMap gridMap = new GridMap(10, 7, 14, 31, 30, lineMap);

		Node<Coordinate> start = gridMap.getNodeAt(0, 0);
		Node<Coordinate> goal = gridMap.getNodeAt(9, 6);
		List<Node<Coordinate>> path = AStar.findPathFrom(start, goal, SearchFunction.euclidean, SearchFunction.manhattan);
		PathFollower pf = new PathFollower(GeoffBot.getDifferentialPilot(), path, path.get(0), Heading.UP);
		for (Node<Coordinate> n : path) {
			Coordinate p = n.payload;
			System.out.println("new Point(" + p.x + ", " + p.y + "),");
		}
		pf.start();
	}
}