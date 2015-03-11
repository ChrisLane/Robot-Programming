package rp.exercise.ex2.part1.paths;

import rp.exercise.ex2.part1.paths.moves.Forward;
import rp.exercise.ex2.part1.paths.moves.RotateCW;

public class TrianglePath extends MovementPath {
	public TrianglePath() {
		super("Triangle", new Movement[] { new Forward(50), new RotateCW(120) });
	}
}
