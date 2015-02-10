package rp.Listener;

import lejos.nxt.LightSensor;

import rp.GeoffBot;

public class IntersectionListener {
	private boolean leftDark, rightDark;
	private IntersectionHitListener ihl;

	public IntersectionListener(LightSensor left, LightSensor right) {
		new BlackLineListener(left, GeoffBot.LSThreshold).setChangeListener(new BlackLineChangeListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				IntersectionListener.this.leftDark = onLine;
				IntersectionListener.this.stateChanged();
			}
		});
		new BlackLineListener(left, GeoffBot.LSThreshold).setChangeListener(new BlackLineChangeListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				IntersectionListener.this.rightDark = onLine;
				IntersectionListener.this.stateChanged();
			}
		});
	}

	public IntersectionListener setArriveListener(IntersectionHitListener listener) {
		this.ihl = listener;
		return this;
	}

	private void stateChanged() {
		if (this.leftDark && this.rightDark && this.ihl != null)
			this.ihl.onIntersectionHit();
	}
}