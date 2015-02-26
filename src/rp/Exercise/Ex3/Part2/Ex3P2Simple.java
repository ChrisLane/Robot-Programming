package rp.Exercise.Ex3.Part2;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

import lejos.robotics.navigation.DifferentialPilot;

import java.util.Queue;

public class Ex3P2Simple extends RunSystem implements LineListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final int lightThreshold = 75;
	private Queue<Node> path;
	private Node location;
	private Compass facing;
	private boolean leftOnline, rightOnline;
	private BlackLineSensor lsLeft, lsRight;

	public Ex3P2Simple(Queue<Node> path, Node location, Compass facing) {

		if (path.empty())
			return;

		this.path = path;
		this.facing = facing;
		this.location = location;

		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold).addChangeListener(this);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);
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
				Node target = (Node) path.pop();

				Compass heading = facing.getHeadingFrom(location.getCoord(), target.getCoord());
				facing = facing.add(heading);

				pilot.travel(4);

				location = target;

				pilot.rotate(heading.toDegrees());
			} else if (leftOnline) {
				pilot.steer(180, -2, false);
			} else if (rightOnline) {
				pilot.steer(180, 2, false);
			}
			pilot.forward();
		}
	}

	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnline = onLine;
		else
			rightOnline = onLine;
	}
}