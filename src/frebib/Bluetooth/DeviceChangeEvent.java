package frebib.Bluetooth;

import javax.bluetooth.RemoteDevice;

public class DeviceChangeEvent {
	private RemoteDevice device;

	public DeviceChangeEvent(RemoteDevice device) {
		super();
		this.device = device;
	}

	public RemoteDevice getDevice() {
		return device;
	}
}
