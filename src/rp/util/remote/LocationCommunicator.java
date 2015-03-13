package rp.util.remote;

import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.RobotPacket;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.pc.comm.NXTCommException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

public class LocationCommunicator extends Thread {
	private BTConnection bt;
	private DataInputStream is;
	private DataOutputStream os;

	private Queue<RobotPacket<?>> toSend;

	private boolean isRunning = true;

	public void connect() throws NXTCommException, IOException {
		toSend = new Queue<RobotPacket<?>>();

		LCD.clear();
		LCD.drawString("Connect to\n     Viewer", 3, 3);
		bt = Bluetooth.waitForConnection();

		byte[] handshake = new byte[32];
		int size = bt.read(handshake, handshake.length);
		if (handshake[0] != 'L' || handshake[1] != 'C' || handshake[2] != 'V' || size != 3)
			throw new NXTCommException("An error connecting to the LocationCommunicator");
		LCD.clear();

		is = bt.openDataInputStream();
		os = bt.openDataOutputStream();

		setPriority(MAX_PRIORITY);
		setDaemon(true);
		start();
	}
	public void disconnect(int exitCode) {
		send(new DisconnectPacket((byte) exitCode));
	}
	public synchronized <E> void send(RobotPacket<E> data) {
		toSend.addElement(data);
		notifyAll();
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
				if (bt == null)
					break;
				try {
					RobotPacket<?> dat = (RobotPacket<?>) toSend.pop();
					dat.write(os);
					os.flush();
					if (dat instanceof DisconnectPacket) {
						// TODO: Clean up resources after disconnect
					}
				}
				catch (IOException e) {
					try {
						bt.openOutputStream().write(e.getMessage().getBytes());
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