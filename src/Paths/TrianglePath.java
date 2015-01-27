package Paths;

import lejos.robotics.navigation.DifferentialPilot;
import Paths.Moves.Forward;
import Paths.Moves.RotateCW;

public class TrianglePath extends MovementPath {
	public TrianglePath(DifferentialPilot pilot) {
		super(pilot, "Triangle", new Movement[] {
				new Forward(pilot, 50), new RotateCW(pilot, 120)
		});
	}
}
