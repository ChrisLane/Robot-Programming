package rp.exercise.ex2.part1.paths;

import rp.exercise.ex2.part1.paths.moves.Arc;

public class CirclePath extends MovementPath {
	public CirclePath() {
		super("Circle", new Movement[] { new Arc(40, 36000) });
	}
}
