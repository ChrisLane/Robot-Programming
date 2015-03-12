package rp.exercise.ex4.part1;

import rp.GeoffBot;
import rp.exercise.ex4.control.PathFollower;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

import java.util.List;

public class Ex4P1C {
	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2015Map1();
		GridMap gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);

		Node<Coordinate> start = gridMap.getNodeAt(0, 0);
		Node<Coordinate> goal = gridMap.getNodeAt(11, 7);
		List<Node<Coordinate>> path = AStar.findPathFrom(start, goal, SearchFunction.euclidean, SearchFunction.manhattan);
		path.remove(0);
		final PathFollower pf = new PathFollower(GeoffBot.getDifferentialPilot(), path, start, Heading.UP);
		for (Node<Coordinate> n : path) {
			Coordinate p = n.payload;
			System.out.println("new Point(" + p.x + ", " + p.y + "),");
		}

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			@Override
			public void buttonPressed(Button b) {
				try {
					pf.stop();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					Button.waitForAnyPress();
					System.exit(0);
				}
			}
		});

		pf.start();
	}
}