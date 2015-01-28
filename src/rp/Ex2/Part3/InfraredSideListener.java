package rp.Ex2.Part3;

import lejos.nxt.LCD;
import lejos.nxt.addon.OpticalDistanceSensor;

public class InfraredSideListener extends OpticalDistanceListener {
	public static final double TARGETDISTANCE = 10.0;
	private final double THRESHOLD = 2;

	private final ArcRadiusChangeListener listener;

	private double currentDistance;
	private double arcRadius;

	public InfraredSideListener(OpticalDistanceSensor sensor, ArcRadiusChangeListener arcl) {
		super(sensor, 0);
		this.listener = arcl;
	}

	@Override
	public void stateChanged(double value, double oldValue) {
		this.currentDistance = (value / 10) + 4;

		double radius = this.calcRadius(TARGETDISTANCE - this.currentDistance);
		radius = Math.abs(radius);
		if (this.currentDistance > TARGETDISTANCE) // Turn
			radius *= -1;

		final double oldAR = this.arcRadius;
		this.arcRadius = radius;

		LCD.clear();
		String s = Double.toString(value);
		s = s.substring(0, Math.min(LCD.DISPLAY_CHAR_WIDTH, s.length()));
		LCD.drawString(s, 0, 0);
		s = Double.toString(this.arcRadius);
		s = s.substring(0, Math.min(LCD.DISPLAY_CHAR_WIDTH, s.length()));
		LCD.drawString(s, 0, 1);

		if (Math.abs(oldAR - this.arcRadius) > this.THRESHOLD)
			this.listener.arcRadiusChanged(this.arcRadius);
	}

	private double calcRadius(double offset) {
		// Calculated with online graphing app
		// http://fooplot.com/plot/far84l18y1
		// x-axis: offset
		// y-axis: turn radius
		return 600 / (offset + 1) + TARGETDISTANCE - 6.5;
	}

	public double getArcRadius() {
		return this.arcRadius;
	}

}
