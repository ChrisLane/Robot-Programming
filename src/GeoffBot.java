import lejos.nxt.Motor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;

public class GeoffBot {
	public final static double WHEELDIAMETER = 7, TRACKWIDTH = 12.5; // Both in cm

	private static DifferentialPilot diffPilot;

	static {
		diffPilot = new DifferentialPilot(WHEELDIAMETER, TRACKWIDTH, Motor.B, Motor.C);
		Bluetooth.setFriendlyName("GeoffBot");
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
