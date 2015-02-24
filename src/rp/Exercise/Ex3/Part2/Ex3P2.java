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
	private BlackLineSensor lsLeft, lsRight;

	private Queue<Node> path;
	private Node location, target;
	private Compass facing;

	private boolean isTravelling = false;

	public Ex3P2(Queue<Node> path, Node location, Compass facing) {
		if (path.empty())
			return;

		this.path = path;
		this.facing = facing;
		this.location = location;

		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, 75);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, 75);
		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		IntersectionSensor intersectionSensor = new IntersectionSensor(lsLeft, lsRight).addChangeListener(this);
		// new LineFollower(intersectionSensor, pilot, lsLeft, lsRight, 200, 1, true);
	}

	@Override
	public synchronized void run() {

		while (!this.path.empty()) {
			this.target = (Node) path.pop();
			System.out.println("location is " + this.location);
			System.out.println("target is " + this.target);

			System.out.println("facing " + this.facing);
			Compass heading = this.facing.getHeadingFrom(this.location.getCoord(), this.target.getCoord());
			facing = this.facing.add(heading);
			System.out.println("rel head is " + heading);
			System.out.println("------------------------");
			// stance towards target node
			if (heading != Compass.UP)
				this.pilot.rotate(heading.toDegrees());					// Rotate to face target node if not already

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
		System.out.println(lsLeft.getLightValue() + " " + lsRight.getLightValue());
		this.pilot.travel(3);

		if (this.isTravelling) {
			this.isTravelling = false;
			this.notify();						// Wake up loop to continue on path
		}
	}

	@Override
	public void onIntersectionDepart() {

	}

	public static void main(String[] args) {
		GeoffBot.connectRemote();

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
