package rp;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;

public class GeoffBot {
	// Light sensor calibrations
	private final static int LSLeftLow = 431, LSRightLow = 345, LSLeftHigh = 554, LSRightHigh = 484;
	// GeoffBot sizes
	private final static double WHEELDIAMETER = 6.88;
	private final static double TRACKWIDTH = 13.0; // Both in cm
	private static final DifferentialPilot diffPilot;

	// GeoffBot settings
	static {
		diffPilot = new DifferentialPilot(WHEELDIAMETER, TRACKWIDTH, Motor.B, Motor.C);
		diffPilot.setTravelSpeed(20);
		diffPilot.setRotateSpeed(60);
		Bluetooth.setFriendlyName("GeoffBot");
	}

	public static DifferentialPilot getDifferentialPilot() {
		return diffPilot;
	}

	public static SensorPort getTouchPort() {
		return SensorPort.S4;
	}

	public static SensorPort getFrontUltrasonicPort() {
		return SensorPort.S2;
	}

	public static SensorPort getSideInfraredPort() {
		return SensorPort.S3;
	}

	public static SensorPort getLightSensorLeftPort() {
		return SensorPort.S3;
	}

	public static SensorPort getLightSensorRightPort() {
		return SensorPort.S4;
	}

	public static SensorPort getCameraPort() {
		return SensorPort.S1;
	}

	public static void calibrateLeftLS(LightSensor ls) {
		ls.setLow(LSLeftLow);
		ls.setHigh(LSLeftHigh);
	}

	public static void calibrateRightLS(LightSensor ls) {
		ls.setLow(LSRightLow);
		ls.setHigh(LSRightHigh);
	}

	// Return console output to PC
	public static void connectRemote() {
		RConsole.openAny(0);
		System.setOut(RConsole.getPrintStream());
		System.setErr(RConsole.getPrintStream());
	}
}