package rp.Exercise.Ex3.Part2;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

import lejos.robotics.navigation.DifferentialPilot;

import java.util.Queue;

@Deprecated
public class Ex3P2Broken extends RunSystem implements LineListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private BlackLineSensor lsLeft, lsRight;

	private Queue<Node> path;
	private Node location, target;
	private Compass facing;
	private Thread steerLeftThread, steerRightThread;

	private boolean isTravelling = false;

	public Ex3P2Broken(Queue<Node> path, Node location, Compass facing) {
		// If the path is empty then we can't do anything
		if (path.empty())
			return;

		this.path = path;
		this.facing = facing;
		this.location = location;

		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, 40).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, 40).addChangeListener(this);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		// REMOVED
		// Detect intersections
		// new IntersectionSensor(lsLeft, lsRight, true).addChangeListener(this);

		steerLeftThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					try {
						wait(0);
						Thread.sleep(250);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (isTravelling)
						pilot.steer(-60);
					else
						pilot.forward();
				}
			}
		});
		steerRightThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					try {
						wait(0);
						Thread.sleep(250);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (isTravelling)
						pilot.steer(60);
					else
						pilot.forward();
				}
			}
		});
	}

	public static void main(String[] args) {

		Queue<Node> path = new Queue<>();
		path.addElement(new Node(1, 0));
		path.addElement(new Node(1, 1));
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 0));
		path.addElement(new Node(1, 0));
		path.addElement(new Node(1, 1));
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 0));

		Ex3P2Broken program = new Ex3P2Broken(path, new Node(0, 0), Compass.UP);
		program.run();
	}

	@Override
	public synchronized void run() {
		while (!path.empty() && isRunning) {
			target = (Node) path.pop();

			Compass heading = facing.getHeadingFrom(location.getCoord(), target.getCoord());
			facing = facing.add(heading);

			pilot.rotate(heading.toDegrees()); // Rotate to face target node if not already

			// Drive to 'location'
			isTravelling = true;
			pilot.forward();

			try { // the line and adjust angle while
				this.wait(0); // Wait for intersection to be reached
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			pilot.travel(4); // Travel to align wheels with the intersection
			isTravelling = false;

			location = target;  // We are now at the target location
		}
	}

	// @Override
	public synchronized void onIntersectionArrive() {
		if (isTravelling)
			notify(); // Wake up loop to continue on path
	}

	// @Override
	public void onIntersectionDepart() {
	}

	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsRight)
			synchronized (steerLeftThread) {
				steerLeftThread.notify();
			}
		else
			synchronized (steerRightThread) {
				steerRightThread.notify();
			}
	}
}