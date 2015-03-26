package rp.exercise.ex2.part1.paths.moves;

import rp.exercise.ex2.part1.paths.Movement;

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
