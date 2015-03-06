package rp.Exercise.Ex2.Part1.Paths;

import rp.Exercise.Ex2.Part1.Paths.Moves.Forward;
import rp.Exercise.Ex2.Part1.Paths.Moves.RotateCW;

public class SquarePath extends MovementPath {
	public SquarePath() {
		super("Square", new Movement[] { new Forward(50), new RotateCW(90) });
	}
}
