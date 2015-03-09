package rp.Exercise.Ex4.mapping;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;
import search.Node;

import lejos.geom.Point;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class PathFollower extends RunSystem implements LineListener {
	private Thread followerThread;

	private final DifferentialPilot pilot;
	private BlackLineSensor lsLeft, lsRight;

	private List<Node<Point>> path;
	private Pose pose;
	private Point target;
	private boolean leftOnLine, rightOnLine, onIntersection;
	private int pathCount = 0;

	public PathFollower(DifferentialPilot pilot, List<Node<Point>> path, Pose location) {
		followerThread = new Thread(this);

		this.pilot = pilot;
		this.path = path;
		// TODO: Should use OdometryPoseProvider(pilot).getPose() instead.
		// Need to fix conflict with Node points to actual grid locations in centimetres
		pose = location;
		leftOnLine = true;
		rightOnLine = true;

		int lightThreshold = 75;
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold).addChangeListener(this);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		pilot.setTravelSpeed(25);
		pilot.setRotateSpeed(180);
	}

	public void start() {
		followerThread.start();
	}

	@Override
	public void run() {
		if (path.isEmpty())
			return;

		intersectionHit(false);
		while (isRunning) {
			if (leftOnLine && rightOnLine && !onIntersection) {
				onIntersection = true;
				if (path.isEmpty())
					return;

				pose.setLocation(target);
				intersectionHit(true);
			}
			else if (!leftOnLine && !rightOnLine)
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
		target = path.get(++pathCount).contents;

		float degrees = pose.angleTo(target);
		if ((int) degrees > -85 && degrees < 85) {
			if (moveForward)
				pilot.travel(4);
			pilot.rotate(degrees);
			pose.rotateUpdate(degrees);
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