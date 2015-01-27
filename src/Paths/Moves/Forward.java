package Paths.Moves;

import Paths.Movement;

import lejos.robotics.navigation.DifferentialPilot;

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
