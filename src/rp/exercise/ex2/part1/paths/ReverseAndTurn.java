package rp.exercise.ex2.part1.paths;

import rp.exercise.ex2.part1.paths.moves.Reverse;
import rp.exercise.ex2.part1.paths.moves.RotateCW;

public class ReverseAndTurn extends MovementPath {
	public ReverseAndTurn() {
		super("Reverse and Turn", new Movement[] { new Reverse(20), new RotateCW(90) });
	}
}
