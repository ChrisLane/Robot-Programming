package rp.util.remote.gui;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.util.remote.RemoteRobot;
import rp.util.remote.packet.ConsolePacket;
import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.ObstaclePacket;
import rp.util.remote.packet.PathPacket;
import rp.util.remote.packet.PosePacket;
import rp.util.remote.packet.RangePacket;
import search.Coordinate;

import lejos.pc.comm.NXTConnector;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class RemoteViewer extends JFrame implements Runnable {
	private final GridMapVisualisation vis;
	private final GridMap gridMap;
	private ConsolePane console;
	private final RemoteRobot robot;

	private final Thread receiveThread;
	private ConnectWindow connect;

	private NXTConnector conn;
	private DataInputStream is;
	private DataOutputStream os;

	public RemoteViewer(GridMap gridMap, LineMap lineMap, int width, int height, float scale, boolean flip) {
		super("Remote Robot Viewer");
		vis = new GridMapVisualisation(gridMap, lineMap, scale, flip);
		this.gridMap = gridMap;
		robot = new RemoteRobot(new Pose(), lineMap, new float[] { 0f });
		vis.addRobot(robot);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(vis);
		setSize(width, height);
		setLocationRelativeTo(null);

		receiveThread = new Thread(this, "Receive Thread");
		receiveThread.setDaemon(true);
	}
	@Override
	public void run() {
		while (true)
			try {
				switch (is.readByte()) {
					case PosePacket.ID:
						PosePacket p = new PosePacket(is);
						robot.setPose(p.getData());
						break;
					case RangePacket.ID:
						RangePacket p1 = new RangePacket(is);
						if (p1.rangeId == 0)
							robot.setRange(0, p1.getData());
						break;
					case ObstaclePacket.ID:
						Coordinate from = new ObstaclePacket(is).getData();
						assert (is.readByte() == ObstaclePacket.ID);
						Coordinate to = new ObstaclePacket(is).getData();
						gridMap.removeSuccessor(gridMap.getNodeAt(from), gridMap.getNodeAt(to));
						gridMap.addObstacle(from.midpoint(to));
						break;
					case PathPacket.ID:
						vis.setPath(new PathPacket(is).getData());
						break;
					case ConsolePacket.ID:
						System.out.print(new ConsolePacket(is));
						break;
					case DisconnectPacket.ID:
						System.out.print("> NXT Disconnected... ");
						System.out.println("Exit Code: " + is.readByte());
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
				System.exit(0);
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
		connect = new ConnectWindow(this, "Connect");
		robot.setPose(new Pose());
		vis.setPath(null);

		conn = connect.getConnection(name);
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
