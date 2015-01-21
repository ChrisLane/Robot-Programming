package Exercise2.Part1;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class JeffRobot {
	// Robot sizes
	public final static double WHEELDIAMETER = 6.88, TRACKWIDTH = 12.65; // Both in cm
			
	private static DifferentialPilot diffPilot;
	
	public static DifferentialPilot getDifferentialPilot() {
		if (diffPilot == null)
			diffPilot = new DifferentialPilot(WHEELDIAMETER, TRACKWIDTH, Motor.B, Motor.C);
		return diffPilot;
	}
}
