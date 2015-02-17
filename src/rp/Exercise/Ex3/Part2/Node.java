package rp.Exercise.Ex3.Part2;

import java.util.ArrayList;

public class Node {
	private final Coord location;
	@SuppressWarnings("unused") private final ArrayList<Node> adjacents;

	public Node(int x, int y) {
		this(new Coord(x, y), new ArrayList<Node>());
	}

	public Node(int x, int y, ArrayList<Node> adjacents) {
		this(new Coord(x, y), adjacents);
	}

	public Node(Coord location, ArrayList<Node> adjacents) {
		super();
		this.location = location;
		this.adjacents = adjacents;
	}

	public Coord getCoord() {
		return this.location;
	}
	public Coord getDelta(Node to) {
		return this.location.getDelta(to.location);
	}

	@Override
	public String toString() {
		return location.toString();
	}
}