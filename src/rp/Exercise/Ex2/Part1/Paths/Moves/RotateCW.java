package rp.Exercise.Ex2.Part1.Paths.Moves;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Exercise.Ex2.Part1.Paths.Movement;

public class RotateCW extends Movement {
	private final double angle;

	public RotateCW(double angle) {
		super();
		this.angle = angle;
	}

	@Override
	protected void startMoving(DifferentialPilot pilot) {
		pilot.rotate(this.angle, true);
	}
}