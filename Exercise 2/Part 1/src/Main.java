import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class Main {

	public static void main(String[] args) {
		// Print "Hello, World!" to the robot display for 5ms
		LCD.drawString("Hello, World!", 0, 0);
		Delay.msDelay(5000);

		// Robot sizes
		final double wheelDiameter = 10;
		final double trackWidth = 5; //Both in cm

		// On a button press, change the robot's movements
		Button.waitForAnyPress();
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter, trackWidth, Motor.B, Motor.C);
		pilot.setTravelSpeed(30);

		while (!Button.ESCAPE.isDown()) {
			while (!Button.ENTER.isDown()) {
				//Do a figure of 8 (hopefully.. although probably not)
				pilot.travel(50, true);
				pilot.arc(-25, 270, true);
				pilot.travel(50, true);
				pilot.arc(25, 270, true);
			}

			while (!Button.ENTER.isDown()) {
				pilot.travel(50, true);
				pilot.rotate(-90, true);
			}

			while (!Button.ENTER.isDown()) {
				pilot.travel(-50, true);
				pilot.rotate(90, true);
			}

			while (!Button.ENTER.isDown()) {
				pilot.travel(-50, true);
				pilot.rotate(-90, true);
			}
		}
	}
}
