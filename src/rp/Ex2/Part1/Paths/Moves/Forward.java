package rp.Ex2.Part1.Paths.Moves;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Ex2.Part1.Paths.Movement;

public class Forward extends Movement {
	private final double dist;

	public Forward(DifferentialPilot pilot, double distance) {
		super(pilot);
		this.dist = distance;
	}

	@Override
	protected void startMoving() {
		pilot.travel(dist, true);
	}
}
