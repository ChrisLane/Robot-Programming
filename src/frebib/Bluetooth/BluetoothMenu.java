package frebib.Bluetooth;

import frebib.GUI.Menu;

import lejos.nxt.Button;

public class BluetoothMenu extends Menu implements Runnable {
	private Thread searchThread;

	public BluetoothMenu() {
		super("Bluetooth");
		// this.searchThread = new Thread(this);
		// searchThread.start();
	}

	public void showMenu() {
		this.draw();
	}

	@Override
	public void run() {
	}

	public static void main(String[] args) {
		new BluetoothMenu().showMenu();
		Button.waitForAnyPress();
	}
}
