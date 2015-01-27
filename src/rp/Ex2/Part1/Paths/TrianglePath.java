package rp.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Ex2.Part1.Paths.Moves.Forward;
import rp.Ex2.Part1.Paths.Moves.RotateCW;

public class TrianglePath extends MovementPath {
	public TrianglePath(DifferentialPilot pilot) {
		super(pilot, "Triangle", new Movement[] {
				new Forward(pilot, 50), new RotateCW(pilot, 120)
		});
	}
}
