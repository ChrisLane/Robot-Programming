package Paths;

import lejos.robotics.navigation.DifferentialPilot;

public class SquarePath extends MovementPath {
	public SquarePath(DifferentialPilot pilot) {
		super(pilot, "Square");
	}

	@Override
	public void path() {
		pilot.travel(50);
		pilot.rotate(90);
	}
}
