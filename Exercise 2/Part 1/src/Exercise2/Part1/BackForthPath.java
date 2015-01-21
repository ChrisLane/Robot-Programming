package Exercise2.Part1;

import lejos.robotics.navigation.DifferentialPilot;

public class BackForthPath extends MovementPath {
	public BackForthPath(DifferentialPilot pilot) {
		super(pilot, "Back and Forth");
	}
	
	@Override
	protected void path() {
		pilot.travel(50);
		pilot.rotate(180);
	}
}
