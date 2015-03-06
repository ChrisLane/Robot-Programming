package rp.Exercise.Ex2.Part1.Paths;

import rp.Exercise.Ex2.Part1.Paths.Moves.Arc;
import rp.Exercise.Ex2.Part1.Paths.Moves.Forward;

public class Figure8Path extends MovementPath {
	public Figure8Path() {
		super("Figure of Eight", new Movement[] { new Forward(20), new Arc(270, 10), new Forward(20), new Arc(-270, -10) });
	}
}
