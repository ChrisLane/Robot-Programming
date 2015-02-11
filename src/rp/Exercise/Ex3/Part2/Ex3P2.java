package rp.Exercise.Ex3.Part2;

import java.util.Queue;

import lejos.robotics.navigation.DifferentialPilot;
import rp.GeoffBot;
import rp.Listener.IntersectionListener;
import rp.Util.RunSystem;

public class Ex3P2 extends RunSystem implements IntersectionListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private Queue<Node> path;
	private Node location, target;
	private Compass heading;

	private boolean travelling = false;

	public Ex3P2(Queue<Node> path, Node location) {
		if (path.empty())
			return;

		this.path = path;
		this.location = location;
		this.target = (Node) path.pop();
	}

	@Override
	public void run() {
		// Rotate to face target node if not already
		Compass destHeading = this.heading.getRelativeHeading(this.target.getCoord());
		if (destHeading != Compass.UP)
			this.pilot.rotate(destHeading.toDegrees());

		// Drive to 'location'
		this.travelling = true;
		this.pilot.forward();
		try {
			this.wait(0); // Wait for intersection to be reached
		}
		catch (Exception e) {
			this.travelling = false;
			e.printStackTrace();
		}
		this.travelling = false;

		// Pop from 'path'
		// Get heading
		// Repeat
	}

	public static void main(String[] args) {
		Queue<Node> path = new Queue<Node>();
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 2));
		path.addElement(new Node(1, 2));
		path.addElement(new Node(2, 2));
		path.addElement(new Node(3, 2));
		path.addElement(new Node(3, 1));

		Ex3P2 program = new Ex3P2(path, new Node(0, 0));
		program.run();
	}

	@Override
	public void onIntersectionArrive() {
		// TODO Auto-generated method stub

	}
}
