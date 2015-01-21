import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class Main {
	
	public static void main(String[] args) {
		// Print "Hello, World!" to the robot display for 5s
		LCD.drawString("Hello, World!", 0, 0);
		// Delay.msDelay(5000);
		
		// Robot sizes
		final double wheelDiameter = 6.88;
		final double trackWidth = 12.65; // Both in cm
		
		// On a button press, change the robot's movements
		// Button.waitForAnyPress();
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter, trackWidth, Motor.B, Motor.C);
		pilot.setTravelSpeed(30);
		
		MovementPath path = new SquarePath(pilot);
		System.out.println("Square started");
		path.start();
		Delay.msDelay(1000);
		path.stop();
		System.out.println("Square stopped");
		
		Sound.setVolume(Sound.VOL_MAX);
		Sound.beepSequence();
		
		System.out.println("8 started");
		path = new Figure8Path(pilot);
		path.start();
		Delay.msDelay(1000);
		// path.stop();
		// System.out.println("8 stopped");
	}
}
