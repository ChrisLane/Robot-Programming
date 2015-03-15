package rp.util.remote;

import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.PacketSender;
import rp.util.remote.packet.RobotPacket;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.pc.comm.NXTCommException;
import lejos.util.Delay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

public class LocationCommunicator extends Thread implements PacketSender {
	private NXTConnection conn;
	private DataInputStream is;
	private DataOutputStream os;

	private Queue<RobotPacket<?>> toSend;

	private boolean isRunning = true;

	public LocationCommunicator() {
		System.setOut(new ConsoleStream(this));
		System.setErr(new ConsoleStream(this, true));
	}
	public void connect() throws NXTCommException, IOException {
		toSend = new Queue<RobotPacket<?>>();

		LCD.clear();
		LCD.drawString("Connect to\n     Viewer", 3, 3);

		getConnection();

		byte[] handshake = new byte[4];
		int size = conn.read(handshake, handshake.length);
		if (handshake[0] != 'L' || handshake[1] != 'C' || handshake[2] != 'V' || size != 3)
			throw new NXTCommException("An error connecting to the LocationCommunicator");
		LCD.clear();

		is = conn.openDataInputStream();
		os = conn.openDataOutputStream();
		os.writeInt((is.readInt() + 1) >> 2);	// Reply with copyMe + 1 >> 2

		setPriority(MAX_PRIORITY);
		setDaemon(true);
		start();
	}
	public void disconnect(int exitCode) {
		send(new DisconnectPacket((byte) exitCode));
	}

	@Override
	public synchronized <E> void send(RobotPacket<E> data) {
		toSend.addElement(data);
		notifyAll();
	}

	private static class ConnectThread extends Thread {
		ConnectThread other;
		boolean done = false;
	}
	// Implementation taken from RConsole
	private void getConnection() {
		// Connect to either a Bluetooth or USB connection/**
		ConnectThread usbThread, btThread = new ConnectThread() {
			@Override
			public void run() {
				conn = Bluetooth.waitForConnection();
				done = true;
				while (!other.done)
					USB.cancelConnect();
			}
		};
		usbThread = new ConnectThread() {
			@Override
			public void run() {
				conn = USB.waitForConnection();
				done = true;
				while (!other.done)
					Bluetooth.cancelConnect();
			}
		};
		btThread.other = usbThread;
		usbThread.other = btThread;
		btThread.start();
		usbThread.start();
		Delay.msDelay(10);
		try {
			btThread.join();
			usbThread.join();
		}
		catch (InterruptedException e) {
		}
	}

	@Override
	public synchronized void run() {
		while (isRunning) {
			if (toSend.empty())
				try {
					wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

			synchronized (os) {
				if (conn == null)
					break;
				try {
					RobotPacket<?> dat = (RobotPacket<?>) toSend.pop();
					dat.write(os);
					os.flush();
					if (dat instanceof DisconnectPacket) {
						os.close();
						conn.close();
						isRunning = false;
						break;
					}
				}
				catch (IOException e) {
					try {
						os.write(e.getMessage().getBytes());
					}
					catch (IOException e1) {
						e1.printStackTrace();
						Button.waitForAnyPress();
						System.exit(0);
					}
					Button.waitForAnyPress();
					System.exit(0);
				}
			}
		}
	}
}