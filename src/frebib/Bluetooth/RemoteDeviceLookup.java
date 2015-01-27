package frebib.Bluetooth;

import javax.bluetooth.RemoteDevice;

public class RemoteDeviceLookup implements Runnable {
	private RemoteDevice dev;
	private DeviceChangeListener devChListener;

	public RemoteDeviceLookup(RemoteDevice dev, DeviceChangeListener devChListener) {
	}

	@Override
	public void run() {
		dev.getFriendlyName(true);
		devChListener.deviceChanged(new DeviceChangeEvent(dev));
	}
}
