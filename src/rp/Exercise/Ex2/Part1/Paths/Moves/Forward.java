package rp.Exercise.Ex2.Part1.Paths.Moves;

import rp.Exercise.Ex2.Part1.Paths.Movement;

import lejos.robotics.navigation.DifferentialPilot;

public class Forward extends Movement {
	private final double dist;

	public Forward(double distance) {
		super();
		dist = distance;
	}

	@Override
	protected void startMoving(DifferentialPilot pilot) {
		pilot.travel(dist, true);
	}
}
