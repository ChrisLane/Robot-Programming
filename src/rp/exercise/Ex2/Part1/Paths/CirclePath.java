package rp.exercise.Ex2.Part1.Paths;

import rp.exercise.Ex2.Part1.Paths.Moves.Arc;

public class CirclePath extends MovementPath {
	public CirclePath() {
		super("Circle", new Movement[] { new Arc(40, 36000) });
	}
}
