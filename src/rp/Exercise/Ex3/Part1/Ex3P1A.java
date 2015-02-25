package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Util.RunSystem;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1A extends RunSystem {
	private double mv = 20; // manipulated variable (speed)

	public static void main(String[] args) {
		Ex3P1A program = new Ex3P1A();
		program.run();
	}

	@Override
	public void run() {
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
		RangeFinder rf = new UltrasonicSensor(GeoffBot.getFrontUltrasonicPort());
		pilot.forward();
		while (isRunning) {
			double pv = rf.getRange();
			double sp = 25;
			double threshold = 5 * sp;
			double error = (pv <= threshold ? sp - pv : pv - sp);

			mv += (0.003 * error);
			pilot.setTravelSpeed(mv);
		}
	}
}
