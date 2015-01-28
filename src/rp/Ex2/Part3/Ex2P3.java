package rp.Ex2.Part3;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex2P3 implements ArcRadiusChangeListener, BumperPressListener {
	private DifferentialPilot pilot;

	private TouchSensor touchSensor;
	private UltrasonicSensor usSensorSide;
	private double arcRadius;

	public Ex2P3(SensorPort touchPort, SensorPort usPortSide, SensorPort usPortFront) {
		this.touchSensor = new TouchSensor(touchPort);
		this.usSensorSide = new UltrasonicSensor(usPortSide);

		new TouchListener(touchPort, this);
		new UltrasonicSideListener(usSensorSide, this);

		pilot = GeoffBot.getDifferentialPilot();
	}

	private void run() {
		pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			pilot.arcForward(arcRadius);
		}
	}

	@Override
	public void arcRadiusChanged(double arcRadius) {
		this.arcRadius = arcRadius;
	}
	@Override
	public void bumperHit() {
		pilot.stop();
		pilot.travel(-UltrasonicSideListener.TARGETDISTANCE);
		pilot.rotate(90);
		pilot.forward();
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		Ex2P3 program = new Ex2P3(GeoffBot.getTouchPort(), GeoffBot.getSideUltrasonicPort(), GeoffBot.getFrontUltrasonicPort());
		program.run();
	}
}