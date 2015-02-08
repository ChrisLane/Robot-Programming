package rp.Ex3.Part1;

import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex3P1A {
	private double pv, error;
	private final double sp = 25; // setPoint
	private double mv = 20; // manipulated variable (speed)
	private final double threshold = 5 * sp;

	public void run() {
		DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
		RangeFinder rf = new UltrasonicSensor(GeoffBot.getFrontUltrasonicPort());
		pilot.forward();
		while (!Button.ESCAPE.isDown() /* && rf.getRange() > sp */) {
			pv = rf.getRange();
			error = (pv <= threshold ? sp - pv : pv - sp);
			mv += (0.003 * error);
			pilot.setTravelSpeed(mv);
		}
		pilot.stop();
	}
	public static void main(String[] args) {
		Ex3P1A program = new Ex3P1A();
		program.run();
	}

}
