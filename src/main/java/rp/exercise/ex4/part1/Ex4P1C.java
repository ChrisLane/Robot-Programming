package rp.exercise.ex4.part1;

import rp.GeoffBot;
import rp.exercise.ex4.control.PathEvents;
import rp.exercise.ex4.control.PathFollower;
import rp.util.RunSystem;
import rp.util.gui.ProgressBar;
import rp.util.remote.RemoteCommunicator;
import rp.util.remote.packet.PathPacket;
import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.MapUtils;
import search.AStar;
import search.Coordinate;
import search.Node;
import search.SearchFunction;
import search.SearchProgress;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class Ex4P1C extends RunSystem implements SearchProgress, PathEvents {
	private GridMap gridMap;
	private Node<Coordinate> startNode, goalNode;

	private RemoteCommunicator locationComm;
	private PathFollower traverser;

	private ProgressBar progress;

	public Ex4P1C(GridMap gridMap, Coordinate start, Coordinate goal) {
		this.gridMap = gridMap;
		startNode = gridMap.getNodeAt(start.x, start.y);
		goalNode = gridMap.getNodeAt(goal.x, goal.y);
		locationComm = new RemoteCommunicator();
		progress = new ProgressBar("Finding Path", 0);
	}

	public static void main(String[] args) {
		GridMap gridMap = new GridMap(12, 8, 15, 15, 30, MapUtils.create2015Map1());
		Ex4P1C exercise = new Ex4P1C(gridMap, new Coordinate(0, 0), new Coordinate(11, 7));
		exercise.run();
	}

	@Override
	public void run() {
		try {
			locationComm.connect();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		pathTo(startNode, goalNode, Heading.PLUS_Y);

		synchronized (this) {
			try {
				wait();
				if (traverser.pathComplete)
					pathComplete();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void pathTo(Node<Coordinate> a, Node<Coordinate> b, Heading facing) {
		progress.setProgress(0);
		progress.render();

		List<Coordinate> path = AStar.findPathFrom(a, b, SearchFunction.euclidean, SearchFunction.manhattan, this);
		locationComm.send(new PathPacket(path));
		LCD.clear();

		traverser = new PathFollower(GeoffBot.getDifferentialPilot(), gridMap, path, facing, this, locationComm);
		traverser.start();
	}

	@Override
	public void pathInterrupted(Pose pose, Node<Coordinate> obstacleNode) {
		Heading facing = Heading.degreesToHeading(pose.getHeading());
		Node<Coordinate> location = gridMap.getNodeAt((int) pose.getX(), (int) pose.getY());
		pathTo(location, goalNode, facing);
	}

	@Override
	public void pathComplete() {
		synchronized (this) {
			notifyAll();
		}
		closeProgram();
	}

	@Override
	public void progressMade(float percent) {
		progress.setProgress((int) (percent * 100));
	}

	@Override
	public void buttonPressed(Button _b) {
		closeProgram();
	}

	public void closeProgram() {
		try {
			traverser.stop();
			locationComm.disconnect(0);
			locationComm.stop();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}