package rp.Ex2.Part1.Paths.Moves;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Ex2.Part1.Paths.Movement;

public class Arc extends Movement {
	private final double angle, radius;

	public Arc(DifferentialPilot pilot, double angle, double radius) {
		super(pilot);
		this.angle = angle;
		this.radius = radius;
	}

	@Override
	protected void startMoving() {
		pilot.travelArc(angle, radius, true);
	}
}
