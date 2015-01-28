package rp.Ex2.Part1.Paths;

import rp.Ex2.Part1.Paths.Moves.Forward;
import rp.Ex2.Part1.Paths.Moves.RotateCW;

import lejos.robotics.navigation.DifferentialPilot;

public class TrianglePath extends MovementPath {
	public TrianglePath(DifferentialPilot pilot) {
		super(pilot, "Triangle", new Movement[]{
				new Forward(pilot, 50), new RotateCW(pilot, 120)
		});
	}
}
