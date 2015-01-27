package Paths;

import Paths.Moves.Arc;
import Paths.Moves.Forward;

import lejos.robotics.navigation.DifferentialPilot;

public class Figure8Path extends MovementPath {
	public Figure8Path(DifferentialPilot pilot) {
		super(pilot, "Figure of Eight", new Movement[] {
				new Forward(pilot, 20), new Arc(pilot, 270, 10), new Forward(pilot, 20), new Arc(pilot, -270, -10)
		});
	}
}
