package rp.util.remote.packet;

import rp.util.remote.ConsoleEntry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConsolePacket extends RobotPacket<ConsoleEntry> {
	public static final byte ID = 1;

	public ConsolePacket(ConsoleEntry entry) {
		super(entry);
	}
	public ConsolePacket(String s, boolean err) {
		super(new ConsoleEntry(s, (short) s.length(), err));
	}
	public ConsolePacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeBoolean(data.err);
		os.writeShort(data.length);
		os.write(data.text.getBytes());
	}
	@Override
	public ConsoleEntry read(DataInputStream is) throws IOException {
		boolean err = is.readBoolean();
		short length = is.readShort();
		byte[] text = new byte[length];
		is.read(text, 0, length);
		return length == 0 ? null : new ConsoleEntry(new String(text), length, err);
	}

	@Override
	public String toString() {
		return data == null ? "" : (data.err ? "ConsoleErr> " : "Console> ") + data.text;
	}
}