package Paths;

import lejos.robotics.navigation.DifferentialPilot;

public class CirclePath extends MovementPath {
	public CirclePath(DifferentialPilot pilot) {
		super(pilot, "Circle");
	}

	@Override
	public void path() {
		pilot.arc(40, 36000);
	}
}
