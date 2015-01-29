package rp.Ex2.Part3;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.DifferentialPilot;

import rp.GeoffBot;

public class Ex2P3 implements ArcRadiusChangeListener, BumperPressListener, WallApproachListener, WallFalloffListener {
	private final DifferentialPilot pilot;

	private final UltrasonicSensor usSensorFront;
	private final OpticalDistanceSensor irSensorSide;
	private double arcRadius;
	private boolean bumperHit, wallFalloff;

	public Ex2P3(SensorPort touchPort, SensorPort irPortSide, SensorPort usPortFront) {
		new TouchSensor(touchPort);
		this.irSensorSide = new OpticalDistanceSensor(irPortSide);
		this.usSensorFront = new UltrasonicSensor(usPortFront);

		new TouchListener(touchPort, this);
		new InfraredSideListener(this.irSensorSide, this, this, this);
		new UltrasonicFrontListener(this.usSensorFront, this);

		this.pilot = GeoffBot.getDifferentialPilot();
	}
	private synchronized void run() {
		this.pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			if (this.wallFalloff) {
				while ((this.irSensorSide.getDistance() * 10) + 4 >= InfraredSideListener.TARGETDISTANCE * 1.2) {
					this.pilot.arcForward(InfraredSideListener.TARGETDISTANCE);
					this.wallFalloff = false;
				}
			}
			else if (this.bumperHit) {
				this.pilot.stop();
				this.pilot.travel(-InfraredSideListener.TARGETDISTANCE);
				this.pilot.rotate(90);
				this.pilot.forward();
				this.bumperHit = false;
			}
		}
	}
	@Override
	public void arcRadiusChanged(double arcRadius) {
		if (!this.bumperHit && !this.wallFalloff)
			synchronized (this.pilot) {
				this.pilot.arcForward(this.arcRadius);
			}
		// this.arcRadius = arcRadius;
	}
	@Override
	public void wallApproaching(double distance) {

	}
	@Override
	public void wallFalloff(double wallDistance) {
		this.wallFalloff = true;
	}
	@Override
	public void bumperHit() {
		this.bumperHit = true;
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		final Ex2P3 program = new Ex2P3(GeoffBot.getTouchPort(), GeoffBot.getSideInfraredPort(), GeoffBot.getFrontUltrasonicPort());
		program.run();
	}
}