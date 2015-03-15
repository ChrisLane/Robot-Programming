package rp.util.remote.gui;

import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
		KeyListener esc = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					setVisible(false);
			}
		};

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		label = new JLabel("Enter Name of NXT");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		input = new JTextField("GeoffBot");
		input.setCaretPosition(input.getText().length());
		input.addKeyListener(esc);

		btn = new JButton("Connect");
		btn.addActionListener(this);
		btn.addKeyListener(esc);
		getRootPane().setDefaultButton(btn);

		panel.add(label);
		panel.add(input);
		panel.add(btn);
		add(panel);
		pack();
		setMinimumSize(new Dimension((int) (panel.getPreferredSize().width * 1.4), panel.getPreferredSize().height));
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
					DataInputStream is = new DataInputStream(conn.getInputStream());
					try {
						// Some random value that is unlikely to be sent in reply
						int copyMe = (int) (System.nanoTime() << 8 ^ System.currentTimeMillis() >>> 3);
						os.write(new byte[] { 'L', 'C', 'V' });
						os.write(copyMe);
						os.flush();
						if (is.readInt() != (copyMe + 1) >> 2)
							throw new NXTCommException("The reply was unexpected and was likely sent from another progrcam");
					}
					catch (IOException ex) {
						JOptionPane.showMessageDialog(dialog, "There was an unexpected I/O error handshaking with the robot. Please try again", "I/O Error", JOptionPane.ERROR_MESSAGE);
					}
					catch (Exception ex) {
						JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
					}
					dialog.setVisible(false);
				}
				else {
					System.out.println("Failed");		// Failed to connect
					fails++;
					if (fails < 5) {
						label.setText("Failed: " + fails + "/5. Retrying...");
						actionPerformed(null);			// Trigger another connection attempt
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
