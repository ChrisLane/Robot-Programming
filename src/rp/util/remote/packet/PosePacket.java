package rp.util.remote.packet;

import lejos.robotics.navigation.Pose;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PosePacket implements RobotPacket<Pose> {
	public static final byte IDENTIFIER = 1;
	private final Pose p;

	public PosePacket(Pose p) {
		this.p = p;
	}
	public PosePacket(DataInputStream is) throws IOException {
		p = read(is);
	}

	@Override
	public Pose read(DataInputStream is) throws IOException {
		return new Pose(is.readFloat(), is.readFloat(), is.readFloat());
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(IDENTIFIER);
		os.writeFloat(p.getX());
		os.writeFloat(p.getY());
		os.writeFloat(p.getHeading());
	}
	public Pose getPose() {
		return p;
	}
}