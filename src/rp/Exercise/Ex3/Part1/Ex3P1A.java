package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Util.RunSystem;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1A extends RunSystem {

	public static void main(String[] args) {
		Ex3P1A program = new Ex3P1A();
		program.run();
	}

	@Override
	public void run() {
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
		RangeFinder rf = new UltrasonicSensor(GeoffBot.getFrontUltrasonicPort());

		double mv = 0; // manipulated variable (travel speed)
		while (isRunning) {
			double range = rf.getRange(); // process variable
			double targetRange = 30; // setpoint

			if (range >= targetRange) {
				double error = range - targetRange;
				mv += 0.5 * error;
				pilot.forward();
				pilot.setTravelSpeed(mv);
			} else if (range <= targetRange) {
				double error = targetRange - range;
				mv += 2 * error;
				pilot.backward();
				pilot.setTravelSpeed(mv);
			} else pilot.setTravelSpeed(0);
		}
	}
}
