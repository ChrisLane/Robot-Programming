package Paths.Moves;

import Paths.Movement;

import lejos.robotics.navigation.DifferentialPilot;

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
