package rp.exercise.Ex2.Part1.Paths.Moves;

import rp.exercise.Ex2.Part1.Paths.Movement;

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