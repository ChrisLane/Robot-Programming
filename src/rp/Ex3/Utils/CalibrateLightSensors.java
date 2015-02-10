package rp.Ex3.Utils;

import rp.GeoffBot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;

public class CalibrateLightSensors {

	public void run() {
		LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		LCD.drawString("Calibrate left\nsensor LOW...", 0, 0);
		Button.waitForAnyPress();
		lsLeft.calibrateLow();
		LCD.clear();

		LCD.drawString("Calibrate left\nsensor HIGH...", 0, 0);
		Button.waitForAnyPress();
		lsLeft.calibrateHigh();
		LCD.clear();

		LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		LCD.drawString("Calibrate right\nsensor LOW...", 0, 0);
		Button.waitForAnyPress();
		lsRight.calibrateLow();
		LCD.clear();

		LCD.drawString("Calibrate right\nsensor HIGH...", 0, 0);
		Button.waitForAnyPress();
		lsRight.calibrateHigh();
		LCD.clear();
	}

	public static void main(String[] args) {
		CalibrateLightSensors program = new CalibrateLightSensors();
		program.run();
	}
}
