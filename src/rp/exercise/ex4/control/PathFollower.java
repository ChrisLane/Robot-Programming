package rp.exercise.ex4.control;

import rp.GeoffBot;
import rp.listener.LineListener;
import rp.robotics.mapping.Heading;
import rp.sensor.BlackLineSensor;
import rp.util.RunSystem;
import rp.util.remote.LocationCommunicator;
import rp.util.remote.packet.PosePacket;
import rp.util.remote.packet.UltrasonicDistancePacket;
import search.Coordinate;
import search.Node;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class PathFollower extends RunSystem implements LineListener {
	private final DifferentialPilot pilot;
	private final OdometryPoseProvider poseProv;
	private final LocationCommunicator lc;
	private Pose pose;

	private BlackLineSensor lsLeft, lsRight;
	private RangeFinder rangeFinder;

	private Thread followThread;

	private Node<Coordinate> location, target;
	private List<Node<Coordinate>> path;
	private Heading facing;
	private boolean leftOnLine, rightOnLine, onIntersection, reversing;
	private byte pathCount;

	public PathFollower(final DifferentialPilot pilot, List<Node<Coordinate>> path, Node<Coordinate> location, Heading facing, LocationCommunicator locationComm) {
		followThread = new Thread(this);

		lc = locationComm;

		this.pilot = pilot;
		this.path = path;
		this.facing = facing;
		this.location = location;
		leftOnLine = true;
		rightOnLine = true;

		poseProv = new OdometryPoseProvider(pilot);
		pose = new Pose(location.payload.x * 30 + 15, location.payload.y * 30 + 15, 0);
		poseProv.setPose(pose);

		int lightThreshold = 75;
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold).addChangeListener(this);
		rangeFinder = new UltrasonicSensor(GeoffBot.getFrontUltrasonicPort());

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		pilot.setTravelSpeed(25);
		pilot.setRotateSpeed(180);
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
			// Update Pose remotely
			lc.send(new PosePacket(poseProv.getPose()));
			// Robot is now on an intersection, adjust robot to next position
			if (leftOnLine && rightOnLine && !onIntersection) {
				onIntersection = true;
				if (reversing)
					continue;
				if (path.size() <= pathCount)
					return;

				intersectionHit(true);
			}

			// Robot is no longer on an intersection
			else if (!leftOnLine && !rightOnLine && onIntersection)
				onIntersection = false;

			// Adjust robot to stay on the line
			if (leftOnLine && !rightOnLine) {
				if (!reversing)
					pilot.arcForward(-30);
				else
					pilot.arcBackward(-30);
			}
			else if (rightOnLine && !leftOnLine)
				if (!reversing)
					pilot.arcForward(30);
				else
					pilot.arcBackward(30);

			// Reversed to an intersection, create and use a new path
			if (onIntersection && reversing)
				reversing = false;
			// TODO: We've reached an unexpected obstacle, re-localise the robot!
			else {
				float range = rangeFinder.getRange();
				lc.send(new UltrasonicDistancePacket(range));
				if (range < 10 && !reversing) {
					reversing = true;
					pilot.backward();
				}
				// Default: travel forwards
				else if (!reversing)
					pilot.forward();
			}

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
				pilot.travel(2.8);
			pilot.rotate(degrees);
			// pose.setHeading(-facing.toDegrees() - degrees);
		}
		pose = poseProv.getPose();
		pose.setLocation(target.payload.y * 30 + 15, target.payload.x * 30 + 15);
		poseProv.setPose(pose);
	}

	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}