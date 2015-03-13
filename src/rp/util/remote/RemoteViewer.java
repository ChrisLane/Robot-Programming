package rp.util.remote;

import rp.exercise.ex4.mapping.GridMap;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.util.remote.packet.ConsolePacket;
import rp.util.remote.packet.DisconnectPacket;
import rp.util.remote.packet.PosePacket;
import rp.util.remote.packet.UltrasonicDistancePacket;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class RemoteViewer extends JFrame implements Runnable {
	private GridMapVisualisation vis;
	private RemoteRobot robot;

	private Thread connectThread, receiveThread;
	private JDialog connect;

	private NXTConnector conn;
	private DataInputStream is;
	private DataOutputStream os;
	private int fails = 0;

	public RemoteViewer(IGridMap gridMap, LineMap lineMap, int width, int height, float scale, boolean flip) {
		super("Remote Robot Viewer");
		vis = new GridMapVisualisation(gridMap, lineMap, scale, flip);
		robot = new RemoteRobot(new Pose(), lineMap, new float[] { 0f }, 5, 240, 255, null);
		vis.addRobot(robot);
		conn = new NXTConnector();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(vis);
		setSize(width, height);
		setLocationRelativeTo(null);

		connect = new JDialog(this, "Connect");
		connect.setModal(true);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel label = new JLabel("Enter Name of NXT");
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JTextField input = new JTextField("GeoffBot");
		Dimension iSize = new Dimension((int) (input.getPreferredSize().width * 1.5), input.getPreferredSize().height);
		input.setPreferredSize(iSize);

		JButton btn = new JButton("Connect");
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectThread = new Thread(new Runnable() {
					@Override
					public void run() {
						btn.setText("Connecting...");
						btn.setEnabled(false);
						if (conn.connectTo(input.getText(), null, NXTCommFactory.BLUETOOTH)) {
							connect.setVisible(false);
							is = new DataInputStream(conn.getInputStream());
							os = new DataOutputStream(conn.getOutputStream());
							try {
								os.write(new byte[] { 'L', 'C', 'V' });
								os.flush();
							}
							catch (IOException ex) {
								// TODO: Invalid reply
								ex.printStackTrace();
							}
							connect.dispose();
							receiveThread.start();	// Connected successfully!
						}
						else {
							System.out.println("Failed");		// Failed to connect
							fails++;
							if (fails < 5) {
								label.setText("Failed: " + fails + "/5. Retrying...");
								btn.getActionListeners()[0].actionPerformed(null);
							}
							else {
								btn.setEnabled(true);
								label.setText("Failed to Connect");
								btn.setText("Connect");
								fails = 0;
							}
						}
					}
				});
				connectThread.start();
			}
		};
		btn.addActionListener(al);

		panel.add(label);
		panel.add(input);
		panel.add(btn);
		connect.add(panel);
		connect.pack();
		connect.setLocationRelativeTo(this);

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
						// TODO: Reset viewer to reconnect or something after disconnect
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
		connect.setVisible(true);
	}
	public static void main(String[] args) {
		RPLineMap lineMap = MapUtils.create2015Map1();
		RemoteViewer rv = new RemoteViewer(new GridMap(12, 8, 15, 15, 30, lineMap), lineMap, 820, 600, 2, true);
		rv.start();
	}
}
