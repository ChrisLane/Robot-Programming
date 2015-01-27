package rp.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Ex2.Part1.Paths.Moves.Arc;

public class CirclePath extends MovementPath {
	public CirclePath(DifferentialPilot pilot) {
		super(pilot, "Circle", new Movement[] {
			new Arc(pilot, 40, 36000)
		});
	}
}
