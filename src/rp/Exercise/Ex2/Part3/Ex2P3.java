package rp.Exercise.Ex2.Part3;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.DifferentialPilot;
import rp.GeoffBot;
import rp.Listener.ArcRadiusChangeListener;
import rp.Listener.BumperHitListener;
import rp.Listener.WallApproachListener;
import rp.Listener.WallFalloffListener;
import rp.Sensor.BumperSensor;
import rp.Sensor.InfraredSideSensor;
import rp.Sensor.UltrasonicFrontSensor;

public class Ex2P3 implements ArcRadiusChangeListener, BumperHitListener,
		WallApproachListener, WallFalloffListener {
	private final DifferentialPilot pilot;

	private final UltrasonicSensor usSensorFront;
	private final OpticalDistanceSensor irSensorSide;
	private double arcRadius;
	private boolean bumperHit, wallFalloff;

	public Ex2P3(SensorPort touchPort, SensorPort irPortSide,
			SensorPort usPortFront) {
		new TouchSensor(touchPort);
		this.irSensorSide = new OpticalDistanceSensor(irPortSide);
		this.usSensorFront = new UltrasonicSensor(usPortFront);

		new BumperSensor(touchPort, this);
		new InfraredSideSensor(this.irSensorSide, this, this, this);
		new UltrasonicFrontSensor(this.usSensorFront, this);

		this.pilot = GeoffBot.getDifferentialPilot();
	}

	private void run() {
		this.pilot.forward();
		while (!Button.ESCAPE.isDown()) {
			if (this.wallFalloff) {
				while ((this.irSensorSide.getDistance() / 10) + 4 >= InfraredSideSensor.TARGETDISTANCE * 1.2 && this.wallFalloff && !this.bumperHit) {
						this.pilot.arcForward(-InfraredSideSensor.TARGETDISTANCE);
				}
				this.wallFalloff = false;
			} 
			if (this.bumperHit) {
				this.pilot.stop();
				this.pilot.travel(-InfraredSideSensor.TARGETDISTANCE / 2);
				this.pilot.rotate(90);
				this.pilot.forward();
				this.bumperHit = false;
			}
		}
	}

	@Override
	public void onRadiusChanged(double arcRadius) {
		this.arcRadius = arcRadius;
		if (!this.bumperHit && !this.wallFalloff)
			this.pilot.arcForward(this.arcRadius);
	}

	@Override
	public void wallApproaching(double distance) {
		if(distance < 15)
			this.bumperHit = true;
	}

	@Override
	public void onWallFalloff(double wallDistance) {
		this.wallFalloff = true;
	}

	@Override
	public void onBumperHit() {
		this.bumperHit = true;
	}

	public static void main(String[] args) {
		// GeoffBot.connectRemote();
		final Ex2P3 program = new Ex2P3(GeoffBot.getTouchPort(),
				GeoffBot.getSideInfraredPort(),
				GeoffBot.getFrontUltrasonicPort());
		program.run();
	}
}