package Exercise2.Part1;

import lejos.robotics.navigation.DifferentialPilot;

public class CirclePath extends MovementPath {
	
	public CirclePath(DifferentialPilot pilot) {
		super(pilot, "Circle");
	}
	
	@Override
	protected void path() {
		pilot.arc(40, Integer.MAX_VALUE);
	}
}
