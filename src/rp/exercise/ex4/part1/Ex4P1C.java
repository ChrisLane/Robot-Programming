package rp.exercise.ex4.part1;

import rp.GeoffBot;
import rp.exercise.ex4.control.PathFollower;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.util.RunSystem;
import rp.util.gui.ProgressBar;
import rp.util.remote.LocationCommunicator;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;
import search.SearchProgress;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

import java.util.List;

public class Ex4P1C extends RunSystem implements SearchProgress, ButtonListener {
	private RPLineMap lineMap;
	private GridMap gridMap;

	private LocationCommunicator locationComm;
	private PathFollower traverser;
	private List<Node<Coordinate>> path;

	private ProgressBar progress;

	@Override
	public void run() {
		Button.ESCAPE.addButtonListener(this);
		locationComm = new LocationCommunicator();
		try {
			locationComm.connect();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		lineMap = MapUtils.create2015Map1();
		gridMap = new GridMap(12, 8, 15, 15, 30, lineMap);
		progress = new ProgressBar("Finding Path", 0);

		Node<Coordinate> start = gridMap.getNodeAt(0, 0);
		Node<Coordinate> goal = gridMap.getNodeAt(11, 7);
		path = AStar.findPathFrom(start, goal, SearchFunction.euclidean, SearchFunction.manhattan, this);
		path.remove(0);
		LCD.clear();

		traverser = new PathFollower(GeoffBot.getDifferentialPilot(), path, start, Heading.UP, locationComm);
		traverser.start();
	}
	@Override
	public void progressMade(float percent) {
		progress.setProgress((int) (percent * 100));
		// System.out.println("Search progress: " + (int) (percent * 100));
	}
	@Override
	public void buttonReleased(Button b) {
	}
	@Override
	public void buttonPressed(Button b) {
		try {
			traverser.stop();
			locationComm.disconnect(0);
			locationComm.stop();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			Button.waitForAnyPress();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		Ex4P1C exercise = new Ex4P1C();
		exercise.run();
	}

}