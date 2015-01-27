package rp.Ex2.Part1.Paths;

import lejos.robotics.navigation.DifferentialPilot;

import rp.Ex2.Part1.Paths.Moves.Forward;
import rp.Ex2.Part1.Paths.Moves.RotateCW;

public class BackForthPath extends MovementPath {
	public BackForthPath(DifferentialPilot pilot) {
		super(pilot, "Back and Forth", new Movement[] {
				new Forward(pilot, 50), new RotateCW(pilot, 180)
		});
	}
}
