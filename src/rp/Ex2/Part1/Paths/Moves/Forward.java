package rp.Ex2.Part1.Paths.Moves;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Ex2.Part1.Paths.Movement;

public class Forward extends Movement {
	private final double dist;

	public Forward(double distance) {
		super();
		this.dist = distance;
	}

	@Override
	protected void startMoving(DifferentialPilot pilot) {
		pilot.travel(this.dist, true);
	}
}
