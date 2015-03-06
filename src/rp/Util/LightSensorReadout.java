package rp.Util;

import rp.GeoffBot;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class LightSensorReadout extends RunSystem {

	@Override
	public void run() {

		// Create new light sensors
		LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);

		// Calibrate light sensors
		GeoffBot.calibrateLeftLS(lsLeft);
		GeoffBot.calibrateRightLS(lsRight);

		// Print light values for light sensors
		GeoffBot.getLightSensorLeftPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				LCD.clear(1);
				LCD.drawString("Left:", 0, 0);
				LCD.drawString(Integer.toString(lsLeft.getLightValue()), 0, 1);
			}
		});

		GeoffBot.getLightSensorRightPort().addSensorPortListener(new SensorPortListener() {
			@Override
			public void stateChanged(SensorPort sensorPort, int oldVal, int newVal) {
				LCD.clear(3);
				LCD.drawString("Right:", 0, 2);
				LCD.drawString(Integer.toString(lsRight.getLightValue()), 0, 3);
			}
		});

		while (isRunning)
			Thread.yield();
	}

	public static void main(String[] args) {
		LightSensorReadout program = new LightSensorReadout();
		program.run();
	}
}
