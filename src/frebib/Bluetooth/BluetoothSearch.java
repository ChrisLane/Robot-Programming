package frebib.Bluetooth;

import lejos.nxt.comm.Bluetooth;

import java.util.ArrayList;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;

public class BluetoothSearch implements Runnable, DiscoveryListener, DeviceChangeListener {
	private final DiscoveryListener discoveryListener;
	private ArrayList<DeviceChangeListener> devChListeners;
	private ArrayList<RemoteDevice> devices;
	private ArrayList<RemoteDeviceLookup> nameLookups;

	public BluetoothSearch(DiscoveryListener dl) {
		this.discoveryListener = dl;
		this.devChListeners = new ArrayList<DeviceChangeListener>();
	}

	@Override
	public void run() {
		Bluetooth.inquireNotify(999, 30, discoveryListener);
	}

	public BluetoothSearch addDeviceChangeListener(DeviceChangeListener dl) {
		devChListeners.add(dl);
		return this;
	}

	@Override
	public void deviceDiscovered(RemoteDevice dev, DeviceClass cod) {
		devices.add(dev);
		nameLookups.add(new RemoteDeviceLookup(dev, this));
		discoveryListener.deviceDiscovered(dev, cod);
	}

	@Override
	public void inquiryCompleted(int discType) {
	}

	@Override
	public void deviceChanged(DeviceChangeEvent dce) {
		for (DeviceChangeListener dcl : devChListeners)
			dcl.deviceChanged(dce);
	}
}
