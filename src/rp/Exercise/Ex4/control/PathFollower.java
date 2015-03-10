package rp.Exercise.Ex4.control;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;
import rp.robotics.mapping.Heading;
import search.Coordinate;
import search.Node;

import lejos.robotics.navigation.DifferentialPilot;

import java.util.List;

public class PathFollower extends RunSystem implements LineListener {
	private final DifferentialPilot pilot;
	private BlackLineSensor lsLeft, lsRight;

	private Thread followThread;

	private Node<Coordinate> location, target;
	private List<Node<Coordinate>> path;
	private Heading facing;
	private boolean leftOnLine, rightOnLine, onIntersection;
	private byte pathCount;

	public PathFollower(DifferentialPilot pilot, List<Node<Coordinate>> path, Node<Coordinate> location, Heading facing) {
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

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		// Increased speed as robot can handle it fine
		pilot.setTravelSpeed(20);
		pilot.setRotateSpeed(120);
	}

	public void start() {
		followThread.start();
	}

	@Override
	public void run() {
		intersectionHit(false);
		while (isRunning) {
			if (leftOnLine && rightOnLine && !onIntersection) {
				onIntersection = true;
				if (path.size() <= pathCount)
					return;

				intersectionHit(true);
			} else if (!leftOnLine && !rightOnLine)
				onIntersection = false;

			if (leftOnLine)
				pilot.arcForward(-40);
			else if (rightOnLine)
				pilot.arcForward(40);
			else
				pilot.forward();
		}
	}

	public void intersectionHit(boolean moveForward) {
		target = path.get(pathCount++);

		Heading heading = facing.getHeadingFrom(location.payload, target.payload);
		facing = facing.add(heading);

		location = target;

		// Turn towards new target node if it isn't straight ahead
		int degrees = heading.toDegrees();
		if (degrees != 0) {
			if (moveForward)
				pilot.travel(1.5);
			pilot.rotate(degrees);
		}
	}

	// Sets a boolean for if each sensor is on the line or not
	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}