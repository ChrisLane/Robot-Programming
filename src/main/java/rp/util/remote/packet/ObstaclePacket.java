package rp.util.remote.packet;

import search.Coordinate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ObstaclePacket extends CoordPacket {
	public static final byte ID = 18;

	public ObstaclePacket(Coordinate coord) {
		super(coord);
	}
	public ObstaclePacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeByte(data.x);
		os.writeByte(data.y);
	}
}