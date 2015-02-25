package rp.Exercise.Ex3.Part2;

import rp.GeoffBot;
import rp.Util.RunSystem;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;

import java.util.Queue;

public class Ex3P2Simple extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final int lightThreshold = 75;
	private Queue<Node> path;
	private Node location;
	private Compass facing;
	private boolean leftOnline, rightOnline;
	private LightSensor lsLeft, lsRight;

	public Ex3P2Simple(Queue<Node> path, Node location, Compass facing) {

		if (path.empty())
			return;

		this.path = path;
		this.facing = facing;
		this.location = location;

		lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsLeft.getLightValue() < lightThreshold)
					leftOnline = true;
				else
					leftOnline = false;
			}
		});

		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				if (lsRight.getLightValue() < lightThreshold)
					rightOnline = true;
				else
					rightOnline = false;
			}
		});
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();

		Queue<Node> path = new Queue<>();
		path.addElement(new Node(1, 0));
		path.addElement(new Node(1, 1));
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 0));
		path.addElement(new Node(1, 0));
		path.addElement(new Node(1, 1));
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 0));

		Ex3P2Simple program = new Ex3P2Simple(path, new Node(0, 0), Compass.UP);
		program.run();
	}

	@Override
	public void run() {
		while (!path.empty() && isRunning) {

			if (leftOnline && rightOnline) {
				System.out.println("Reached intersect");
				Node target = (Node) path.pop();

				Compass heading = facing.getHeadingFrom(location.getCoord(), target.getCoord());
				facing = facing.add(heading);

				pilot.travel(4);

				location = target;

				pilot.rotate(heading.toDegrees());
			} else if (leftOnline) {
				System.out.println("Left on line");
				pilot.steer(180, -2, false);
			} else if (rightOnline) {
				System.out.println("Right on line");
				pilot.steer(180, 2, false);
			}
			pilot.forward();
		}
	}
}