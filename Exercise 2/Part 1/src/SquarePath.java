import lejos.robotics.navigation.DifferentialPilot;

public class SquarePath extends MovementPath {
	public SquarePath(DifferentialPilot pilot) {
		super(pilot);
	}

	public void run() {
		pilot.travel(10, false);
		pilot.rotate(90, false);
		pilot.travel(10, false);
		pilot.rotate(90, false);
	}
}
