package rp.exercise.ex2.part1.paths;

import rp.exercise.ex2.part1.paths.moves.Forward;
import rp.exercise.ex2.part1.paths.moves.RotateCW;

public class BackForthPath extends MovementPath {
	public BackForthPath() {
		super("Back and Forth", new Movement[] { new Forward(50), new RotateCW(180) });
	}
}
