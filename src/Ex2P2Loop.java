import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P2 {
	public static void main(String[] args) {
		TouchSensor ts = new TouchSensor(SensorPort.S4);
		DifferentialPilot pilot = JeffRobot.getDifferentialPilot();

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}
		});
		SensorPortListener spl = new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
				if (aSource != SensorPort.S4) // Touch Sensor is attached to Port 4
					return;
				
			}
		};
		
		pilot.forward();
		while (true) {
			if (ts.isPressed()) {
				pilot.stop();
				pilot.travel(-20);
				pilot.rotate(90);
				pilot.forward();
			}
		}
	}
}
