package rp.Ex2.Part1.Paths.Moves;

import rp.Ex2.Part1.Paths.Movement;

import lejos.robotics.navigation.DifferentialPilot;

public class RotateCW extends Movement {
	private final double angle;

	public RotateCW(DifferentialPilot pilot, double angle) {
		super(pilot);
		this.angle = angle;
	}

	@Override
	protected void startMoving() {
		pilot.rotate(angle, true);
	}
}