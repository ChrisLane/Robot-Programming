import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;

public class JeffRobot {
	public final static double WHEELDIAMETER = 6.88, TRACKWIDTH = 12.65; // Both in cm

	private static DifferentialPilot diffPilot;

	static {
		diffPilot = new DifferentialPilot(WHEELDIAMETER, TRACKWIDTH, Motor.B, Motor.C);
	}

	public static DifferentialPilot getDifferentialPilot() {
		return diffPilot;
	}

	public static void connectRemote() {
		RConsole.openAny(0);
		System.setOut(RConsole.getPrintStream());
		System.setErr(RConsole.getPrintStream());
	}
}
