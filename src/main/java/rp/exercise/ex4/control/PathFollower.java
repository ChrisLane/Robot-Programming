package rp.exercise.ex4.control;

import rp.GeoffBot;
import rp.exercise.ex4.mapping.GridMap;
import rp.listener.LineListener;
import rp.robotics.mapping.Heading;
import rp.sensor.BlackLineSensor;
import rp.util.remote.RemoteCommunicator;
import rp.util.remote.packet.PosePacket;
import rp.util.remote.packet.RangePacket;
import search.Coordinate;

import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

import java.util.List;

public class PathFollower implements LineListener, Runnable {
	private final DifferentialPilot pilot;
	private final OdometryPoseProvider poseProv;
	private final RemoteCommunicator lc;
	private final PathEvents listener;
	private final GridMap gridMap;
	public boolean pathComplete;
	private final Pose pose;
	private final BlackLineSensor lsLeft;
	private final BlackLineSensor lsRight;
	private final OpticalDistanceSensor rangeFinder;

	private final Thread followThread;
	private boolean isRunning, stopping;

	private Coordinate location, target;
	private final List<Coordinate> path;
	private Heading facing;
	private boolean leftOnLine, rightOnLine, onIntersection;
	private byte pathCount;
	private float range;

	public PathFollower(final DifferentialPilot pilot, GridMap gridMap, List<Coordinate> path, Heading facing, PathEvents listener, RemoteCommunicator locationComm) {
		followThread = new Thread(this);

		this.listener = listener;
		lc = locationComm;

		this.gridMap = gridMap;
		this.pilot = pilot;
		this.path = path;
		this.facing = facing;

		pathCount = 0;
		location = path.get(pathCount++);
		target = path.get(pathCount++);

		poseProv = new OdometryPoseProvider(pilot);
		pose = new Pose(location.x * 30 + 15, location.y * 30 + 15, facing.toDegrees());
		poseProv.setPose(pose);

		int lightThreshold = 75;
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold).addChangeListener(this);
		rangeFinder = new OpticalDistanceSensor(GeoffBot.getInfraredPort());

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		pilot.setTravelSpeed(30);
		pilot.setRotateSpeed(180);
	}

	public void start() {
		followThread.start();
	}

	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		turnToTarget();

		while (isRunning) {
			// Get range reading from US sensor & send it to the viewer
			float range = rangeFinder.getRange() + 3.5f;
			lc.send(new RangePacket(range, 0));
			lc.send(new PosePacket(poseProv.getPose(), 0));

			if (leftOnLine && rightOnLine && !onIntersection) {
				onIntersection = true;
				if (pathCount >= path.size()) {
					pilot.stop();
					listener.pathComplete();
					return;
				}

				intersectionHit();
				if (stopping)
					break;
			}
			else if (!leftOnLine && !rightOnLine)
				onIntersection = false;

			if (leftOnLine)
				pilot.arcForward(65);
			else if (rightOnLine)
				pilot.arcForward(-65);
			else
				pilot.forward();
		}
	}

	public void intersectionHit() {
		location = target;
		target = path.get(pathCount++);

		pilot.travel(2.9); // Moves forward to align wheels with intersection
		turnToTarget();

		float mapRange = gridMap.rangeToObstacleFromGridPosition((int) pose.getX(), (int) pose.getY(), pose.getHeading());
		float sensorRange = rangeFinder.getRange() + 3.5f;
		if (mapRange <= 32 && sensorRange < mapRange + 10) {
			System.out.println("Map Range: " + mapRange);
			System.out.println("Sensor Range: " + sensorRange);

			float tolerance = (mapRange < 50) ? 5f : 10f;
			if (Math.abs(sensorRange - mapRange) > tolerance) {
				System.out.println("Obstacle found!");
				System.out.println("Range: " + range);
				System.out.println("Map Range: " + mapRange);

				listener.pathInterrupted(pose, gridMap.getNodeAt(location), gridMap.getNodeAt(target));

				isRunning = false;
				stopping = true;
			}
		}
	}

	public void turnToTarget() {
		Heading heading = facing.getHeadingFrom(location, target);
		if (heading != Heading.PLUS_X)
			pilot.rotate(heading.toDegrees());
		facing = facing.add(heading);

		// Update Pose remotely
		pose.setHeading(facing.toDegrees());
		pose.setLocation(location.x * 30 + 15, location.y * 30 + 15);
		poseProv.setPose(new Pose(pose.getX(), pose.getY(), pose.getHeading()));
	}

	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}