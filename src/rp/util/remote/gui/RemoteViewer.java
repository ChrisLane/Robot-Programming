package rp.util.remote.gui;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.util.remote.RemoteRobot;
import rp.util.remote.packet.ConsolePacket;
import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.PosePacket;
import rp.util.remote.packet.UltrasonicDistancePacket;

import lejos.pc.comm.NXTConnector;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class RemoteViewer extends JFrame implements Runnable {
	private GridMapVisualisation vis;
	private ConsolePane console;
	private RemoteRobot robot;

	private Thread receiveThread;
	private ConnectWindow connect;

	private NXTConnector conn;
	private DataInputStream is;
	// private DataOutputStream os;

	public RemoteViewer(IGridMap gridMap, LineMap lineMap, int width, int height, float scale, boolean flip) {
		super("Remote Robot Viewer");
		vis = new GridMapVisualisation(gridMap, lineMap, scale, flip);
		robot = new RemoteRobot(new Pose(), lineMap, new float[] { 0f });
		vis.addRobot(robot);
		conn = new NXTConnector();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(vis);
		setSize(width, height);
		setLocationRelativeTo(null);

		connect = new ConnectWindow(this, "Connect");

		receiveThread = new Thread(this, "Receive Thread");
		receiveThread.setDaemon(true);
	}
	@Override
	public void run() {
		while (true)
			try {
				switch (is.readByte()) {
					case PosePacket.IDENTIFIER:
						robot.setPose(new PosePacket(is).getPose());
						break;
					case UltrasonicDistancePacket.IDENTIFIER:
						robot.setRange(0, new UltrasonicDistancePacket(is).getDistance());
						break;
					case ConsolePacket.IDENTIFIER:
						System.out.println(new ConsolePacket(is));
						break;
					case DisconnectPacket.IDENTIFIER:
						System.out.println("> NXT Disconnected...");
						// TODO: Test reconnection
						start();
						break;
					default:
						break;		// Do nothing here
				}
			}
		catch (IOException e) {
			e.printStackTrace();
			try {
				System.out.println("Connection Closed");
				conn.close();
			}
			catch (IOException e1) {
				e1.printStackTrace();
				break;
			}
			break;
		}
	}
	public void start() {
		setVisible(true);
		conn = connect.getConnection();
		// TODO: Test if this works when connected
		if (conn.getNXTComm() != null) {
			is = new DataInputStream(conn.getInputStream());
			// os = new DataOutputStream(conn.getOutputStream());
			receiveThread.start();
		}
		else
			System.exit(1);

	}
	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2015Map1();
		RemoteViewer rv = new RemoteViewer(new GridMap(12, 8, 15, 15, 30, lineMap), lineMap, 820, 600, 2, true);
		rv.start();
	}
}
