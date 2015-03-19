package rp.util.remote;

import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.PacketSender;
import rp.util.remote.packet.RobotPacket;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.pc.comm.NXTCommException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Queue;

public class RemoteCommunicator extends Thread implements PacketSender {
	private NXTConnection conn;
	private DataInputStream is;
	private DataOutputStream os;

	private PrintStream defOut, defErr;

	private Queue<RobotPacket<?>> toSend;

	private boolean isRunning = true;

	public RemoteCommunicator() {
		defOut = System.out;
		defErr = System.err;
		System.setOut(new ConsoleStream(this));
		System.setErr(new ConsoleStream(this, true));
	}
	public void connect() throws NXTCommException {
		toSend = new Queue<RobotPacket<?>>();

		LCD.clear();
		LCD.drawString("Connect to\n     Viewer", 3, 3);

		conn = Bluetooth.waitForConnection();

		byte[] handshake = new byte[32];
		int size = conn.read(handshake, handshake.length);
		if (handshake[0] != 'L' || handshake[1] != 'C' || handshake[2] != 'V' || size != 3)
			throw new NXTCommException("An error connecting to the LocationCommunicator");
		LCD.clear();

		is = conn.openDataInputStream();
		os = conn.openDataOutputStream();
		// TODO: Fix handshake part 2
		// os.writeInt((is.readInt() + 1) >> 2); // Reply with copyMe + 1 >> 2

		setPriority(MAX_PRIORITY);
		setDaemon(true);
		start();
	}
	public void disconnect(int exitCode) {
		send(new DisconnectPacket((byte) exitCode));
		System.setOut(defOut);
		System.setErr(defErr);
	}
	public void stop() throws InterruptedException {
		join();
	}

	@Override
	public synchronized <E> void send(RobotPacket<E> data) {
		toSend.addElement(data);
		notify();
	}

	@Override
	public synchronized void run() {
		while (isRunning) {
			if (toSend.empty())
				try {
					wait();
				}
				catch (InterruptedException e) {
					System.setOut(defOut);
					System.setErr(defErr);
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
					System.setOut(defOut);
					System.setErr(defErr);
					try {
						os.write(e.getMessage().getBytes());
					}
					catch (IOException e1) {
						e1.printStackTrace();
						System.exit(0);
					}
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
	}
}