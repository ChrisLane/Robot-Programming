package rp.Exercise.Ex3.Part1;

import rp.GeoffBot;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Util.RunSystem;

import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;

public class Ex3P1B extends RunSystem {
	private final DifferentialPilot pilot = GeoffBot.getDifferentialPilot();
	private final BlackLineSensor lsLeft, lsRight;
	private boolean turningLeft, turningRight;
	private final byte darkThreshold = 80;

	public Ex3P1B() {
		this.lsLeft = new BlackLineSensor(GeoffBot.getLightSensorLeftPort(), true, darkThreshold);
		this.lsRight = new BlackLineSensor(GeoffBot.getLightSensorRightPort(), true, darkThreshold);

		GeoffBot.calibrateLeftLS(this.lsLeft);
		GeoffBot.calibrateRightLS(this.lsRight);
	}

	@Override
	public void run() {
		this.lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.clear(0);
				LCD.drawString(Integer.toString(lightValue), 0, 0);

				if (onLine)
					pilot.steer(120);

				else
					Ex3P1B.this.pilot.forward();
			}
		});

		this.lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				LCD.clear(1);
				LCD.drawString(Integer.toString(lightValue), 0, 1);
				if (onLine)
					Ex3P1B.this.pilot.steer(120);
				else
					Ex3P1B.this.pilot.forward();
			}
		});

		this.pilot.forward();
		while (this.isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		final Ex3P1B program = new Ex3P1B();
		program.run();
	}
}