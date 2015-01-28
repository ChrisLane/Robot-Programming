package rp.Ex2.Part3;

import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicSideListener extends UltrasonicListener {
	public static final double TARGETDISTANCE = 15.0;
	private final double THRESHOLD = 2;

	private ArcRadiusChangeListener listener;

	private double currentDistance;
	private double arcRadius;

	public UltrasonicSideListener(UltrasonicSensor sensor, ArcRadiusChangeListener arcl) {
		super(sensor, 0);
		this.listener = arcl;
	}

	@Override
	public void stateChanged(int value, int oldValue) {
		LCD.clear();
		this.currentDistance = value;

		double radius = this.calcRadius(TARGETDISTANCE - this.currentDistance);
		radius = Math.abs(radius);
		if (this.currentDistance > TARGETDISTANCE) // Turn
			radius *= -1;

		final double oldAR = this.arcRadius;
		this.arcRadius = radius;

		if (Math.abs(oldAR - this.arcRadius) > THRESHOLD)
			listener.arcRadiusChanged(this.arcRadius);

		System.out.println(this.currentDistance);
		System.out.println(radius);
	}
	private double calcRadius(double offset) {
		// Calculated with online graphing app
		// http://fooplot.com/plot/far84l18y1
		// x-axis: offset
		// y-axis: turn radius
		return 900 / (offset + 4) + TARGETDISTANCE + 2.5;
	}
	public double getArcRadius() {
		return this.arcRadius;
	}
}
