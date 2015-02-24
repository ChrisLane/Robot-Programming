package rp.Exercise.Ex3.Part2;

import rp.GeoffBot;
import rp.Listener.IntersectionListener;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Sensor.IntersectionSensor;
import rp.Util.RunSystem;

import lejos.robotics.navigation.DifferentialPilot;

import java.util.Queue;

public class Ex3P2 extends RunSystem implements IntersectionListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private BlackLineSensor lsLeft, lsRight;

	private Queue<Node> path;
	private Node location, target;
	private Compass facing;
	private Thread steerLeftThread, steerRightThread;

	private boolean isTravelling = false;

	public Ex3P2(Queue<Node> path, Node location, Compass facing) {
		if (path.empty())
			return;

		this.path = path;
		this.facing = facing;
		this.location = location;

		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, 40);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, 40);
		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		new IntersectionSensor(lsLeft, lsRight, true).addChangeListener(this);

		lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				synchronized (steerRightThread) {
					steerRightThread.notify();
				}
			}
		});
		lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				synchronized (steerLeftThread) {
					steerLeftThread.notify();
				}
			}
		});

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

	@Override
	public synchronized void run() {
		while (!path.empty() && isRunning) {
			target = (Node) path.pop();

			Compass heading = facing.getHeadingFrom(location.getCoord(), target.getCoord());
			facing = facing.add(heading);

			// stance towards target node
			if (heading != Compass.UP)
				pilot.rotate(heading.toDegrees());		// Rotate to face target node if not already

			// Drive to 'location'
			isTravelling = true;
			pilot.forward();							// TODO: Make the BlackLineSensor follow

			try {										// the line and adjust angle while
				this.wait(0);							// Wait for intersection to be reached
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			pilot.travel(4);							// Travel to align wheels with the intersection
			isTravelling = false;

			location = target;							// We are now at the target location
		}
	}

	@Override
	public synchronized void onIntersectionArrive() {
		if (isTravelling)
			notify();									// Wake up loop to continue on path
	}

	@Override
	public void onIntersectionDepart() {
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

		Ex3P2 program = new Ex3P2(path, new Node(0, 0), Compass.UP);
		program.run();
	}
}