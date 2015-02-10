package rp.Exercise.Ex3.Part2;

import lejos.robotics.navigation.DifferentialPilot;

import java.util.Queue;

import rp.GeoffBot;
import rp.RunSystem;
import rp.Listener.IntersectionHitListener;

public class Ex3P2 extends RunSystem implements IntersectionHitListener {
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
		final Compass destinationHeading = this.heading.getRelativeHeading(this.target.getCoord());
		if (destinationHeading != Compass.UP)
			this.pilot.rotate(destinationHeading.toDegrees());

		this.travelling = true;
		this.pilot.forward();
		try {
			this.wait();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

		// Drive to 'location'

		// Pop from 'path'
		// Get heading
		// Repeat

	}
	public static void main(String[] args) {
		final Queue<Node> path = new Queue<Node>();
		path.addElement(new Node(0, 1));
		path.addElement(new Node(0, 2));
		path.addElement(new Node(1, 2));
		path.addElement(new Node(2, 2));
		path.addElement(new Node(3, 2));
		path.addElement(new Node(3, 1));

		final Ex3P2 program = new Ex3P2(path, new Node(0, 0));
		program.run();
	}

	@Override
	public void onIntersectionHit() {
		// TODO Auto-generated method stub

	}
}
