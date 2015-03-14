package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DisconnectPacket extends RobotPacket<Byte> {
	public static final byte ID = 0;

	public DisconnectPacket(byte exitCode) {
		super(exitCode);
	}
	public DisconnectPacket(DataInputStream is) throws IOException {
		super(is);
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeByte(data);
	}

	@Override
	public Byte read(DataInputStream is) throws IOException {
		return is.readByte();
	}
}