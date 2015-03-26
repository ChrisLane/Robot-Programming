package rp.exercise.ex2.part1.paths;

import rp.exercise.ex2.part1.paths.moves.Arc;
import rp.exercise.ex2.part1.paths.moves.Forward;

public class Figure8Path extends MovementPath {
	public Figure8Path() {
		super("Figure of Eight", new Movement[] { new Forward(20), new Arc(270, 10), new Forward(20), new Arc(-270, -10) });
	}
}
