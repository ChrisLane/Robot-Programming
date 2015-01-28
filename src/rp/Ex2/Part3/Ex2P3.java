package rp.Ex2.Part3;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex2P3 implements ArcRadiusChangeListener, BumperPressListener {
	private final DifferentialPilot pilot;

	private final TouchSensor touchSensor;
	private final UltrasonicSensor usSensorFront;
	private final OpticalDistanceSensor irSensorSide;
	private double arcRadius;

	public Ex2P3(SensorPort touchPort, SensorPort irPortSide, SensorPort usPortFront) {
		this.touchSensor = new TouchSensor(touchPort);
		this.irSensorSide = new OpticalDistanceSensor(irPortSide);
		this.usSensorFront = new UltrasonicSensor(usPortFront);

		new TouchListener(touchPort, this);
		new InfraredSideListener(this.irSensorSide, this);

		this.pilot = GeoffBot.getDifferentialPilot();
	}

	private void run() {
		this.pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			this.pilot.arcForward(this.arcRadius);
		}
	}

	@Override
	public void arcRadiusChanged(double arcRadius) {
		this.arcRadius = arcRadius;
	}
	@Override
	public void bumperHit() {
		this.pilot.stop();
		this.pilot.travel(-InfraredSideListener.TARGETDISTANCE);
		this.pilot.rotate(90);
		this.pilot.forward();
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		final Ex2P3 program = new Ex2P3(GeoffBot.getTouchPort(), GeoffBot.getSideInfraredPort(), GeoffBot.getFrontUltrasonicPort());
		program.run();
	}
}