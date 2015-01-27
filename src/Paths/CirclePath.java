package Paths;

import Paths.Moves.Arc;

import lejos.robotics.navigation.DifferentialPilot;

public class CirclePath extends MovementPath {
	public CirclePath(DifferentialPilot pilot) {
		super(pilot, "Circle", new Movement[] {
			new Arc(pilot, 40, 36000)
		});
	}
}
