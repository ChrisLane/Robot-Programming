package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Util.RunSystem;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1A extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final RangeFinder rf = new UltrasonicSensor(GeoffBot.getFrontUltrasonicPort());

	private final double targetRange = 30; // setpoint

	public static void main(String[] args) {
		Ex3P1A program = new Ex3P1A();
		program.run();
	}

	@Override
	public void run() {
		while (isRunning) {
			double range = rf.getRange(); // process variable
			double mv = 0; // manipulated variable (travel speed)

			// If too far from target range, gradually increase forward speed
			if (range >= targetRange) {
				double error = range - targetRange;
				mv += 0.5 * error;
				pilot.setTravelSpeed(mv);
				pilot.forward();
			}
			// If we're past the target range, gradually increase reverse speed
			else if (range <= targetRange) {
				double error = targetRange - range;
				mv += 2 * error;
				pilot.setTravelSpeed(mv);
				pilot.backward();
			}
			// We're at the target distance, stop the robot.
			else
				pilot.setTravelSpeed(0);
		}
	}
}
