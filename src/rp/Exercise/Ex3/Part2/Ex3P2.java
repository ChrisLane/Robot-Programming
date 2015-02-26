package rp.Exercise.Ex3.Part2;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

import lejos.robotics.navigation.DifferentialPilot;

import java.util.Queue;

public class Ex3P2 extends RunSystem implements LineListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private Queue<Node> path;
	private Node location, target;
	private Compass facing;
	private boolean leftOnLine, rightOnLine, onIntersection;
	private BlackLineSensor lsLeft, lsRight;

	public Ex3P2(Queue<Node> path, Node location, Compass facing) {
		if (path.empty())
			return;

		this.path = path;
		this.facing = facing;
		this.location = location;
		this.leftOnLine = true;
		this.rightOnLine = true;

		int lightThreshold = 75;
		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold).addChangeListener(this);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold).addChangeListener(this);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		// Increased speed as robot can handle it fine
		pilot.setTravelSpeed(25);
		pilot.setRotateSpeed(180);
	}

	public static void main(String[] args) {
		// Path of nodes for robot to follow
		Queue<Node> path = new Queue<>();
		path.addElement(new Node(1, 0));
		path.addElement(new Node(1, 1));
		path.addElement(new Node(1, 2));
		path.addElement(new Node(1, 3));
		path.addElement(new Node(1, 2));
		path.addElement(new Node(1, 1));
		path.addElement(new Node(1, 0));
		path.addElement(new Node(0, 0));
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 0));

		// Robot starts at node (0,0) and follows path above
		Ex3P2 program = new Ex3P2(path, new Node(0, 0), Compass.UP);
		program.run();
	}

	@Override
	public void run() {
		intersectionHit(false);
		while (isRunning) {
			// If both sensors are the line then we've reached an intersection. Rotate towards new target.
			if (leftOnLine && rightOnLine && !onIntersection) {
				onIntersection = true;
				// Finish path traversal if there are no more nodes left
				if (path.empty())
					return;

				intersectionHit(true);
			}
			else if (!leftOnLine && !rightOnLine)
				// Added onIntersection to fix a big bug where it would pass all
				// intersections in a straight line after the first one.
				// This works by incrementing the current node after it has left the
				// current intersection so not to skip them all at once.
				onIntersection = false;

			// If on the line then turn the respective direction to correct
			if (leftOnLine)
				pilot.arcForward(-40);
			else if (rightOnLine)
				pilot.arcForward(40);
			else
				pilot.forward();
		}
	}

	public void intersectionHit(boolean moveForward) {
		// New target is next node in path
		target = (Node) path.pop();

		// Get relative heading from current location and global heading
		Compass heading = facing.getHeadingFrom(location.getCoord(), target.getCoord());
		// Add relative heading to global heading
		facing = facing.add(heading);

		location = target;

		// Turn towards new target node if it isn't straight ahead
		int degrees = heading.toDegrees();
		if (degrees != 0) {
			if (moveForward)
				pilot.travel(4);
			pilot.rotate(degrees);
		}
	}

	// Sets a boolean for if each sensor is on the line or not
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}