package rp.Exercise.Ex4;

import rp.GeoffBot;
import rp.Exercise.Ex4.mapping.GridMap;
import rp.Exercise.Ex4.mapping.PathFollower;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import search.AStar;
import search.Node;
import search.SearchFunction;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class Ex4P1C {

	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2014Map2();
		GridMap gridMap = new GridMap(10, 7, 14, 31, 30, lineMap);

		Node<Point> start = gridMap.getNodeAt(0, 0);
		Node<Point> goal = gridMap.getNodeAt(9, 6);
		List<Node<Point>> path = AStar.findPathFrom(start, goal, SearchFunction.euclidean, SearchFunction.manhattan);
		PathFollower pf = new PathFollower(GeoffBot.getDifferentialPilot(), path, new Pose(0, 0, 0));
		pf.start();
	}
}