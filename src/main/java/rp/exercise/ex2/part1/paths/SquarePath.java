package rp.exercise.ex2.part1.paths;

import rp.exercise.ex2.part1.paths.moves.Forward;
import rp.exercise.ex2.part1.paths.moves.RotateCW;

public class SquarePath extends MovementPath {
	public SquarePath() {
		super("Square", new Movement[] { new Forward(50), new RotateCW(90) });
	}
}
