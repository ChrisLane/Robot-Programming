package rp.exercise.ex2.part1.paths.moves;

import rp.exercise.ex2.part1.paths.Movement;

import lejos.robotics.navigation.DifferentialPilot;

public class RotateCW extends Movement {
	private final double angle;

	public RotateCW(double angle) {
		super();
		this.angle = angle;
	}

	@Override
	protected void startMoving(DifferentialPilot pilot) {
		pilot.rotate(angle, true);
	}
}