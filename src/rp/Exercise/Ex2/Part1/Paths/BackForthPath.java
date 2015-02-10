package rp.Exercise.Ex2.Part1.Paths;

import rp.Exercise.Ex2.Part1.Paths.Moves.Forward;
import rp.Exercise.Ex2.Part1.Paths.Moves.RotateCW;

public class BackForthPath extends MovementPath {
	public BackForthPath() {
		super("Back and Forth", new Movement[] {
				new Forward(50), new RotateCW(180)
		});
	}
}
