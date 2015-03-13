package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConsolePacket implements RobotPacket<String> {
	public static final byte IDENTIFIER = 127;
	private short length;
	private String text;

	public ConsolePacket(String text) {
		this.text = text;
		length = (short) text.length();
	}
	public ConsolePacket(DataInputStream is) throws IOException {
		text = read(is);
		length = text == null ? 0 : (short) text.length();
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(IDENTIFIER);
		os.writeShort(length);
		os.write(text.getBytes());
	}

	@Override
	public String read(DataInputStream is) throws IOException {
		short length = is.readShort();
		byte[] text = new byte[length];
		is.read(text, 0, length);
		return length == 0 ? null : new String(text);
	}
	@Override
	public String toString() {
		return text == null ? "" : "Console> " + text;
	}
}
