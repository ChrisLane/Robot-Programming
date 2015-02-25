package rp.Listener;

import rp.Sensor.BlackLineSensor;

public interface LineListener {
	void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue);
}
