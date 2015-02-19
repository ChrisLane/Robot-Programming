package rp.Util;

import rp.GeoffBot;

import lejos.robotics.navigation.DifferentialPilot;

public class Turn360 extends RunSystem {

	final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();

	@Override
	public void run() {
		pilot.rotate(360);
	}

	public static void main(String[] args) {
		Turn360 program = new Turn360();
		program.run();
	}
}
