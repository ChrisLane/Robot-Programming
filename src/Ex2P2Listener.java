import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex2P2Listener {
	public static void main(String[] args) {
		TouchSensor ts = new TouchSensor(SensorPort.S4);
		DifferentialPilot pilot = JeffRobot.getDifferentialPilot();

		SensorPort.S4.addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
				// Is this necessary?
				// if (aSource != SensorPort.S4) // Touch Sensor is attached to Port 4
				// return;
				pilot.stop();
				pilot.travel(-20);
				pilot.rotate(90);
				pilot.forward();
			}
		});
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	}
}
