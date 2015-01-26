import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P2Loop {
	public static void main(String[] args) {
		TouchSensor ts = new TouchSensor(SensorPort.S4);
		DifferentialPilot pilot = JeffRobot.getDifferentialPilot();

		pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			if (ts.isPressed()) {
				pilot.stop();
				pilot.travel(-20);
				pilot.rotate(90);
				pilot.forward();
			}
		}
	}
}