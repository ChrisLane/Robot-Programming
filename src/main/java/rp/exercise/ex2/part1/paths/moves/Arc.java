package rp.exercise.ex2.part1.paths.moves;

import rp.exercise.ex2.part1.paths.Movement;

import lejos.robotics.navigation.DifferentialPilot;

public class Arc extends Movement {
	private final double angle, radius;

	public Arc(double angle, double radius) {
		super();
		this.angle = angle;
		this.radius = radius;
	}

	@Override
	protected void startMoving(DifferentialPilot pilot) {
		pilot.travelArc(angle, radius, true);
	}
}
