package Paths.Moves;

import lejos.robotics.navigation.DifferentialPilot;

public class Reverse extends Forward {
	public Reverse(DifferentialPilot pilot, double distance) {
		super(pilot, -distance);
	}
}
