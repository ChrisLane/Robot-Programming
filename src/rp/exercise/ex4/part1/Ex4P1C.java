package rp.exercise.ex4.part1;

import rp.GeoffBot;
import rp.exercise.ex4.control.PathEvents;
import rp.exercise.ex4.control.PathFollower;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import rp.util.RunSystem;
import rp.util.gui.ProgressBar;
import rp.util.remote.RemoteCommunicator;
import rp.util.remote.packet.PathPacket;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;
import search.SearchProgress;

import lejos.nxt.Button;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class Ex4P1C extends RunSystem implements SearchProgress, PathEvents {
	private GridMap gridMap;
	private Node<Coordinate> startNode, goalNode;

	private RemoteCommunicator locationComm;
	private PathFollower traverser;
	private List<Coordinate> path;

	private ProgressBar progress;

	public Ex4P1C(GridMap gridMap, Coordinate start, Coordinate goal) {
		this.gridMap = gridMap;
		startNode = gridMap.getNodeAt(start.x, start.y);
		goalNode = gridMap.getNodeAt(goal.x, goal.y);
		locationComm = new RemoteCommunicator();
		progress = new ProgressBar("Finding Path", 0);
	}
	@Override
	public void run() {
		try {
			locationComm.connect();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		pathTo(startNode, goalNode, Heading.UP);
	}
	private void pathTo(Node<Coordinate> a, Node<Coordinate> b, Heading facing) {
		progress.setProgress(0);
		progress.render();

		path = AStar.findPathFrom(a, b, SearchFunction.euclidean, SearchFunction.manhattan, this);
		locationComm.send(new PathPacket(path));

		traverser = new PathFollower(GeoffBot.getDifferentialPilot(), path, facing, this, locationComm);
		traverser.start();
	}
	@Override
	public void pathInterrupted(Pose pose, Node<Coordinate> obstacleNode) {
		System.out.println("Interrupted at " + pose);

		Heading facing = Heading.getCompass((int) pose.getHeading());
		Node<Coordinate> location = gridMap.getNodeAt((int) pose.getX(), (int) pose.getY());
		pathTo(location, goalNode, facing);

	}
	@Override
	public void pathComplete() {
		System.out.println("Path complete!");
		Button.ESCAPE.callListeners();
	}

	@Override
	public void progressMade(float percent) {
		progress.setProgress((int) (percent * 100));
	}

	@Override
	public void buttonPressed(Button b) {
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
		GridMap gridMap = new GridMap(12, 8, 15, 15, 30, MapUtils.create2015Map1());
		Ex4P1C exercise = new Ex4P1C(gridMap, new Coordinate(0, 0), new Coordinate(11, 7));
		exercise.run();
	}
}