package rp.Ex2.Part3;

import lejos.nxt.LCD;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Datalogger;

public class InfraredSideListener extends OpticalDistanceListener {
	public static final double TARGETDISTANCE = 16.0;
	private final double THRESHOLD = 0;

	private final ArcRadiusChangeListener listener;

	private double currentDistance;
	private double arcRadius;
	private final Datalogger log = new Datalogger();

	public InfraredSideListener(OpticalDistanceSensor sensor, ArcRadiusChangeListener arcl) {
		super(sensor, 0);
		this.listener = arcl;
	}
	@Override
	public void stateChanged(double value, double oldValue) {
		this.currentDistance = value;

		final double offset = TARGETDISTANCE - this.currentDistance;
		double radius = this.calcRadius(Math.abs(offset));
		if (this.currentDistance > TARGETDISTANCE) // Turn towards wall
			radius *= -1;

		final double oldAR = this.arcRadius;
		this.arcRadius = radius;

		LCD.clear();
		String s = Double.toString(this.currentDistance);
		s = s.substring(0, Math.min(6, s.length()));
		LCD.drawString(s, 0, 0);
		String s1 = Double.toString(Math.abs(radius));
		s1 = s1.substring(0, Math.min(6, s1.length()));
		LCD.drawString(s1, 0, 1);
		RConsole.println(s + ',' + s1);

		if (Math.abs(oldAR - this.arcRadius) > this.THRESHOLD)
			this.listener.arcRadiusChanged(this.arcRadius);
	}
	private double calcRadius(double offset) {
		// Calculated with online graphing app
		// http://fooplot.com/plot/far84l18y1
		// x-axis: offset
		// y-axis: turn radius
		return (200 / ((0.65 * offset) - 1)) + TARGETDISTANCE - 8.2;
	}
	public double getArcRadius() {
		return this.arcRadius;
	}

}
