package rp.exercise.ex4.control;

import rp.GeoffBot;
import rp.listener.LineListener;
import rp.robotics.mapping.Heading;
import rp.sensor.BlackLineSensor;
import rp.util.RunSystem;
import rp.util.remote.RemoteCommunicator;
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
	private Pose pose;

	private final RemoteCommunicator lc;
	private final PathEvents listener;

	private BlackLineSensor lsLeft, lsRight;
	private RangeFinder rangeFinder;

	private Thread followThread;

	private Node<Coordinate> location, target;
	private List<Node<Coordinate>> path;
	private Heading facing;
	private boolean leftOnLine, rightOnLine, onIntersection;
	private byte pathCount;

	public PathFollower(final DifferentialPilot pilot, List<Node<Coordinate>> path, Heading facing, PathEvents listener, RemoteCommunicator locationComm) {
		followThread = new Thread(this);

		this.listener = listener;
		lc = locationComm;

		this.pilot = pilot;
		this.path = path;
		this.facing = facing;

		pathCount = 0;
		location = path.get(pathCount++);
		target = path.get(pathCount++);

		leftOnLine = true;
		rightOnLine = true;

		poseProv = new OdometryPoseProvider(pilot);
		pose = new Pose(location.payload.x * 30 + 15, location.payload.y * 30 + 15, facing.toDegrees() + 90);
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
		turnToTarget();

		while (isRunning) {
			// Get range reading from US sensor & send it to the viewer
			float range = rangeFinder.getRange();
			lc.send(new UltrasonicDistancePacket(range));
			lc.send(new PosePacket(poseProv.getPose(), 1));

			if (leftOnLine && rightOnLine && !onIntersection) {
				onIntersection = true;
				if (pathCount >= path.size()) {
					listener.pathComplete();
					return;
				}

				intersectionHit();
			}
			else if (!leftOnLine && !rightOnLine)
				onIntersection = false;

			if (leftOnLine)
				pilot.arcForward(40);
			else if (rightOnLine)
				pilot.arcForward(-40);
			else
				pilot.forward();

		}
	}
	public void intersectionHit() {
		location = target;
		target = path.get(pathCount++);

		pilot.travel(2.9);	// Moves forward to align wheels with intersection
		turnToTarget();

		// Update Pose remotely
		pose.setLocation(target.payload.x * 30 + 15, target.payload.y * 30 + 15);
		lc.send(new PosePacket(pose, 0));
	}
	public void turnToTarget() {
		Heading heading = facing.getHeadingFrom(location.payload, target.payload);
		if (heading != Heading.UP)
			pilot.rotate(-heading.toDegrees());
		facing = facing.add(heading);
		pose.setHeading(facing.toDegrees());
		poseProv.setPose(new Pose(pose.getX(), -pose.getY(), pose.getHeading()));
	}
	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}