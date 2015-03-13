package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DisconnectPacket implements RobotPacket<Byte> {
	public static final byte IDENTIFIER = 0;
	private byte exitCode;

	public DisconnectPacket(byte exitCode) {
		this.exitCode = exitCode;
	}
	public DisconnectPacket(DataInputStream is) throws IOException {
		exitCode = read(is);
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(IDENTIFIER);
		os.writeByte(exitCode);
	}

	@Override
	public Byte read(DataInputStream is) throws IOException {
		return is.readByte();
	}

}
