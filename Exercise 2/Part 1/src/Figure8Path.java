import lejos.robotics.navigation.DifferentialPilot;

public class Figure8Path extends MovementPath {
	public Figure8Path(DifferentialPilot pilot) {
		super(pilot);
	}

	public void run() {
		pilot.travel(50, true);
		pilot.arc(-25, 270, true);
		pilot.travel(50, true);
		pilot.arc(25, 270, true);
	}
}
