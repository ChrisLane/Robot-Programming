package rp.Exercise.Ex3.Part1;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;
import rp.RunSystem;
import rp.Listener.BlackLineChangeListener;
import rp.Listener.BlackLineListener;

public class Ex3P1B extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final LightSensor lsLeft, lsRight;

	public Ex3P1B() {
		this.lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		this.lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);

		GeoffBot.calibrateLeftLS(this.lsLeft);
		GeoffBot.calibrateRightLS(this.lsRight);
	}

	@Override
	public void run() {
		new BlackLineListener(this.lsLeft, GeoffBot.LSThreshold).setChangeListener(new BlackLineChangeListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.drawString("Left:  " + Integer.toString(lightValue) + onLine, 0, LCD.DISPLAY_CHAR_DEPTH - 2);
				if (onLine)
					Ex3P1B.this.pilot.steer(-180, 7, true);
				else
					Ex3P1B.this.pilot.forward();

				System.out.println("Left:\t" + lightValue);
			}
		});

		new BlackLineListener(this.lsRight, GeoffBot.LSThreshold).setChangeListener(new BlackLineChangeListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.drawString("Right: " + Integer.toString(lightValue) + onLine, 0, LCD.DISPLAY_CHAR_DEPTH - 1);
				if (onLine)
					Ex3P1B.this.pilot.steer(180, 7, true);
				else
					Ex3P1B.this.pilot.forward();
				System.out.println("Right:\t" + lightValue);
			}
		});

		this.pilot.forward();
		while (this.isRunning)
			Thread.yield();
	}
	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}
}