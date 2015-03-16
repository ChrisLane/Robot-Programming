package rp.util.remote.gui;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.util.remote.RemoteRobot;
import rp.util.remote.packet.ConsolePacket;
import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.PathPacket;
import rp.util.remote.packet.PosePacket;
import rp.util.remote.packet.UltrasonicDistancePacket;

import lejos.pc.comm.NXTConnector;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class RemoteViewer extends JFrame implements Runnable {
	private GridPositionDistributionVisualisation vis;
	private ConsolePane console;
	private RemoteRobot robot;

	private Thread receiveThread;
	private ConnectWindow connect;

	private NXTConnector conn;
	private DataInputStream is;
	private DataOutputStream os;

	public RemoteViewer(IGridMap gridMap, LineMap lineMap, int width, int height, float scale, boolean flip) {
		super("Remote Robot Viewer");
		vis = new GridPositionDistributionVisualisation(new GridPositionDistribution(gridMap), lineMap, scale, flip);
		robot = new RemoteRobot(new Pose(), lineMap, new float[] { 0f });
		vis.addRobot(robot);
		conn = new NXTConnector();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
					case PosePacket.ID:
						robot.setPose(new PosePacket(is).getData());
						break;
					case UltrasonicDistancePacket.ID:
						robot.setRange(0, new UltrasonicDistancePacket(is).getData());
						break;
					case PathPacket.ID:
						vis.setPath(new PathPacket(is).getData());
					case ConsolePacket.ID:
						System.out.println(new ConsolePacket(is));
						break;
					case DisconnectPacket.ID:
						System.out.println("> NXT Disconnected...");
						// TODO: Test reconnection
						start(conn.getNXTInfo().name);
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
		start("");
	}
	public void start(String name) {
		conn = connect.getConnection(name);
		// TODO: Test if this works when connected
		if (conn.getNXTComm() != null) {
			is = new DataInputStream(conn.getInputStream());
			os = new DataOutputStream(conn.getOutputStream());
			if (!receiveThread.isAlive())
				receiveThread.start();
		}
		else
			System.exit(1);
	}
	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2015Map1();
		RemoteViewer rv = new RemoteViewer(new GridMap(12, 8, 15, 15, 30, lineMap), lineMap, 820, 600, 2, true);
		rv.setVisible(true);
		rv.start("GeoffBot");
	}
}
