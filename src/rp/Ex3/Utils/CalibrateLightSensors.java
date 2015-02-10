package rp.Ex3.Utils;

import rp.GeoffBot;
import rp.RunUtil;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.util.Delay;

public class CalibrateLightSensors extends RunUtil{

	@Override
	public void run() {
		LightSensor lsLeft = new LightSensor(GeoffBot.getLightSensorLeftPort(), true);
		LightSensor lsRight = new LightSensor(GeoffBot.getLightSensorRightPort(), true);
		LCD.drawString("Calibrate sensor's\nLOW value...", 0, 0);
		Button.waitForAnyPress();
		lsLeft.calibrateLow();
		lsRight.calibrateLow();
		LCD.clear();

		LCD.drawString("Calibrate sensor's\nHIGH value...", 0, 0);
		Button.waitForAnyPress();
		lsLeft.calibrateHigh();
		lsRight.calibrateHigh();
		LCD.clear();

		LCD.drawString("Calibration Values:", 0, 0);
		LCD.drawString("Left Low: " + Integer.toString(lsLeft.getLow()), 0, 2);
		LCD.drawString("Right Low: " + Integer.toString(lsRight.getLow()), 0, 3);
		LCD.drawString("Left High: " + Integer.toString(lsLeft.getHigh()), 0, 5);
		LCD.drawString("Right High: " + Integer.toString(lsRight.getHigh()), 0, 6);
		Button.waitForAnyPress();

		while (isRunning) {
			LCD.clear();
			LCD.drawString(lsLeft.getLightValue() + "     " + lsRight.getLightValue(), 0, 0);
			Delay.msDelay(100);
		}
	}

	public static void main(String[] args) {
		CalibrateLightSensors program = new CalibrateLightSensors();
		program.run();
	}
}
