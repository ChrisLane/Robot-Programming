package rp.Ex2.Part1.Paths;

import rp.Ex2.Part1.Paths.Moves.Reverse;
import rp.Ex2.Part1.Paths.Moves.RotateCW;

public class ReverseAndTurn extends MovementPath {
	public ReverseAndTurn() {
		super("Reverse and Turn", new Movement[] { new Reverse(20),
				new RotateCW(90) });
	}
}