package rp.listener;

import rp.sensor.BlackLineSensor;

public interface LineListener {
	void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue);
}
