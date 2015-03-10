package rp.Exercise.Ex4.control;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;
import rp.robotics.mapping.Heading;
import search.Coordinate;
import search.Node;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;

import java.util.List;

public class PathFollower extends RunSystem implements LineListener {
	private final DifferentialPilot pilot;
	private BlackLineSensor lsLeft, lsRight;
	private RangeFinder rangeFinder;

	private Thread followThread;

	private Node<Coordinate> location, target;
	private List<Node<Coordinate>> path;
	private Heading facing;
	private boolean leftOnLine, rightOnLine, onIntersection, reversing;
	private byte pathCount;

	public PathFollower(final DifferentialPilot pilot, List<Node<Coordinate>> path, Node<Coordinate> location, Heading facing) {
		followThread = new Thread(this);

		this.pilot = pilot;
		this.path = path;
		this.facing = facing;
		this.location = location;
		leftOnLine = true;
		rightOnLine = true;

		int lightThreshold = 75;
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold).addChangeListener(this);
		rangeFinder = new UltrasonicSensor(GeoffBot.getFrontUltrasonicPort());

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		pilot.setTravelSpeed(20);
		pilot.setRotateSpeed(120);
	}

	public void start() {
		followThread.start();
	}

	public void stop() throws InterruptedException {
		isRunning = false;
		followThread.join();
	}

	@Override
	public void run() {
		intersectionHit(false);
		while (isRunning) {
			// Robot is now on an intersection, adjust robot to next position
			if (leftOnLine && rightOnLine && !onIntersection && !reversing) {
				onIntersection = true;
				if (path.size() <= pathCount || reversing)
					return;

				intersectionHit(true);
			}

			// Robot is no longer on an intersection
			else if (!leftOnLine && !rightOnLine)
				onIntersection = false;

			// Adjust robot to stay on the line
			if (leftOnLine) {
				if (!reversing)
					pilot.arcForward(-40);
				else
					pilot.arcBackward(-40);
			} else if (rightOnLine) {
				if (!reversing)
					pilot.arcForward(40);
				else
					pilot.arcBackward(40);
			}

			// Reversed to an intersection, create and use a new path
			else if (onIntersection && reversing) {
				System.out.println("Reached an intersection while reversing");
				reversing = false;
				// TODO: Create a method to use here that can be passed a blocked coordinate and follow a new path with that data
			}

			// Reverse if we meet an obstacle
			else if (rangeFinder.getRange() < 10) {
				System.out.println("OH FUCK, A WALL!");
				reversing = true;
				pilot.backward();
			}

			// Default: travel forwards
			else if (!reversing)
				pilot.forward();
		}
	}

	public void intersectionHit(boolean moveForward) {
		target = path.get(pathCount++);

		Heading heading = facing.getHeadingFrom(location.payload, target.payload);
		facing = facing.add(heading);

		location = target;

		int degrees = heading.toDegrees();
		if (degrees != 0) {
			if (moveForward)
				pilot.travel(1.5);
			pilot.rotate(degrees);
		}
	}

	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}