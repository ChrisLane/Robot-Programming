package rp.Exercise.Ex3.Part2;

import java.util.Queue;

import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;
import rp.Listener.IntersectionListener;
import rp.Sensor.BlackLineSensor;
import rp.Sensor.IntersectionSensor;
import rp.Util.RunSystem;

public class Ex3P2 extends RunSystem implements IntersectionListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private BlackLineSensor left, right;

	private Queue<Node> path;
	private Node location, target;
	private Compass heading;

	private boolean isTravelling = false;

	public Ex3P2(Queue<Node> path, Node location, Compass heading) {
		if (path.empty())
			return;

		this.path = path;
		this.heading = heading;
		this.location = location;

		left = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, 75);
		right = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, 75);
		GeoffBot.calibrateLeftLS(left);
		GeoffBot.calibrateRightLS(right);

		new IntersectionSensor(left, right).addArriveListener(this);
	}
	@Override
	public synchronized void run() {
		while (!this.path.empty()) {
			this.target = (Node) path.pop();
			System.out.println("target is " + this.target);

			Coord delta = this.location.getDelta(this.target);				// Get difference between two co-ordinates
			Compass destHeading = this.heading.getRelativeHeading(delta);	// Get a compass heading to turn from current
			// stance towards target node
			if (destHeading != Compass.UP)
				this.pilot.rotate(destHeading.toDegrees());					// Rotate to face target node if not already

			// Drive to 'location'
			this.isTravelling = true;
			this.pilot.forward();				// TODO: Make the BlackLineSensor follow the line and adjust angle while
			try {
				this.wait(0);					// Wait for intersection to be reached
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.location = this.target;		// We are now at the target location
		}
	}
	@Override
	public synchronized void onIntersectionArrive() {
		if (this.isTravelling) {
			this.isTravelling = false;
			this.notify();						// Wake up loop to continue on path
		}
	}

	public static void main(String[] args) {
		GeoffBot.connectRemote();

		Queue<Node> path = new Queue<>();
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 2));
		path.addElement(new Node(1, 2));
		path.addElement(new Node(2, 2));
		path.addElement(new Node(3, 2));
		path.addElement(new Node(3, 1));
		path.addElement(new Node(2, 1));
		path.addElement(new Node(2, 0));

		Ex3P2 program = new Ex3P2(path, new Node(0, 0), Compass.UP);
		program.run();
	}
}
