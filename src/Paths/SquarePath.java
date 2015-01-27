package Paths;

import Paths.Moves.Forward;
import Paths.Moves.RotateCW;

import lejos.robotics.navigation.DifferentialPilot;

public class SquarePath extends MovementPath {
	public SquarePath(DifferentialPilot pilot) {
		super(pilot, "Square", new Movement[] {
				new Forward(pilot, 50), new RotateCW(pilot, 90)
		});
	}
}
