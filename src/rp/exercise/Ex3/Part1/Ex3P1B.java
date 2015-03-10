package rp.exercise.Ex3.Part1;

import rp.listener.LineListener;

import rp.sensor.BlackLineSensor;
import rp.util.RunSystem;
import rp.GeoffBot;

import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1B extends RunSystem implements LineListener {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final BlackLineSensor lsLeft, lsRight;
	private boolean leftOnLine, rightOnLine;

	public Ex3P1B() {
		byte lightThreshold = 75;

		lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, lightThreshold);
		lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, lightThreshold);

		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		lsLeft.addChangeListener(this);
		lsRight.addChangeListener(this);
	}

	public static void main(String[] args) {
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}

	@Override
	public void run() {
		while (isRunning)
			// If the left sensor is on the line, turn left
			if (leftOnLine)
				pilot.steer(200, -10, false);
			// If the right sensor is on the line, turn right
			else if (rightOnLine)
				pilot.steer(200, 10, false);
			// Otherwise continue on straight
			else
				pilot.forward();
	}

	// Sets a boolean for if each sensor is on the line or not
	@Override
	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == lsLeft)
			leftOnLine = onLine;
		else
			rightOnLine = onLine;
	}
}