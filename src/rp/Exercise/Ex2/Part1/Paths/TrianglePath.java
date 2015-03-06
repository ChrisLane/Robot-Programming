package rp.Exercise.Ex2.Part1.Paths;

import rp.Exercise.Ex2.Part1.Paths.Moves.Forward;
import rp.Exercise.Ex2.Part1.Paths.Moves.RotateCW;

public class TrianglePath extends MovementPath {
	public TrianglePath() {
		super("Triangle", new Movement[] { new Forward(50), new RotateCW(120) });
	}
}
