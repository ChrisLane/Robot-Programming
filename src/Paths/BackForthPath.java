package Paths;

import Paths.Moves.Forward;
import Paths.Moves.RotateCW;

import lejos.robotics.navigation.DifferentialPilot;

public class BackForthPath extends MovementPath {
	public BackForthPath(DifferentialPilot pilot) {
		super(pilot, "Back and Forth", new Movement[] {
				new Forward(pilot, 50), new RotateCW(pilot, 180)
		});
	}
}
