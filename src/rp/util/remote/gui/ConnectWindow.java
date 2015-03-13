package rp.util.remote.gui;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ConnectWindow extends JDialog implements ActionListener {
	private ConnectWindow dialog = this;
	private Window parent;
	private JLabel label;
	private JTextField input;
	private JButton btn;

	private Thread connThread;
	private NXTConnector conn;

	private byte fails = 0;

	public ConnectWindow(Window parent, String title) {
		super(parent, title);
		setModal(true);

		conn = new NXTConnector();

		init();
	}
	private void init() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		label = new JLabel("Enter Name of NXT");
		label.setHorizontalAlignment(SwingConstants.CENTER);

		input = new JTextField("GeoffBot");
		Dimension iSize = new Dimension((int) (input.getPreferredSize().width * 1.5), input.getPreferredSize().height);
		input.setPreferredSize(iSize);

		btn = new JButton("Connect");
		btn.addActionListener(this);

		panel.add(label);
		panel.add(input);
		panel.add(btn);
		add(panel);
		pack();
		setLocationRelativeTo(parent);
	}

	public NXTConnector getConnection() {
		setVisible(true);
		return conn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		connThread = new Thread(new Runnable() {
			@Override
			public void run() {
				btn.setText("Connecting...");
				btn.setEnabled(false);
				if (conn.connectTo(input.getText(), null, NXTCommFactory.BLUETOOTH)) {
					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					try {
						os.write(new byte[] { 'L', 'C', 'V' });
						os.flush();
					}
					catch (IOException ex) {
						// TODO: Invalid reply
						ex.printStackTrace();
					}
					dialog.setVisible(false);
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
		connThread.start();
	}
}
