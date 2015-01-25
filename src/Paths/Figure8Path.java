package Paths;

import lejos.robotics.navigation.DifferentialPilot;

public class Figure8Path extends MovementPath {
	public Figure8Path(DifferentialPilot pilot) {
		super(pilot, "Figure of Eight");
	}

	@Override
	public void path() {
		pilot.travel(20);
		pilot.arc(10, 270);
		pilot.travel(20);
		pilot.arc(-10, -270);
	}
}
