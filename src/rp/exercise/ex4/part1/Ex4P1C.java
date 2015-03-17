package rp.exercise.ex4.part1;

import rp.GeoffBot;
import rp.exercise.ex4.control.PathEvents;
import rp.exercise.ex4.control.PathFollower;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.util.RunSystem;
import rp.util.gui.ProgressBar;
import rp.util.remote.RemoteCommunicator;
import rp.util.remote.packet.PathPacket;
import search.Coordinate;
import search.DepthFirst;
import search.Node;
import search.SearchProgress;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class Ex4P1C extends RunSystem implements SearchProgress, PathEvents {
	private RPLineMap lineMap;
	private GridMap gridMap;

	private RemoteCommunicator locationComm;
	private PathFollower traverser;
	private List<Node<Coordinate>> path;

	private ProgressBar progress;

	@Override
	public void run() {
		locationComm = new RemoteCommunicator();
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
		path = DepthFirst.findPathFrom(start, goal, this);// , SearchFunction.euclidean, SearchFunction.manhattan, this);
		locationComm.send(new PathPacket(path));
		LCD.clear();

		traverser = new PathFollower(GeoffBot.getDifferentialPilot(), path, Heading.UP, this, locationComm);
		traverser.start();
	}
	@Override
	public void progressMade(float percent) {
		progress.setProgress((int) (percent * 100));
	}

	@Override
	public void pathInterrupted(Pose location) {
		System.out.println("Interrupted at " + location);
		// TODO Auto-generated method stub

	}
	@Override
	public void pathComplete() {
		System.out.println("Path complete!");
		Button.ESCAPE.callListeners();
	}

	@Override
	public void buttonPressed(Button b) {
		System.out.println("Escape pressed");
		try {
			traverser.stop();
			locationComm.disconnect(0);
			locationComm.stop();
			System.exit(1);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		Ex4P1C exercise = new Ex4P1C();
		exercise.run();
	}
}