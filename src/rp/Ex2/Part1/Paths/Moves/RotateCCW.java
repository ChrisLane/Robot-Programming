package rp.Ex2.Part1.Paths.Moves;

import lejos.robotics.navigation.DifferentialPilot;

public class RotateCCW extends RotateCW {
	public RotateCCW(DifferentialPilot pilot, double angle) {
		super(pilot, -angle);
	}
}