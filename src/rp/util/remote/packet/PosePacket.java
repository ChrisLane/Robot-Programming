package rp.util.remote.packet;

import lejos.robotics.navigation.Pose;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PosePacket extends RobotPacket<Pose> {
	public static final byte ID = 10;

	public PosePacket(Pose pose) {
		super(pose);
	}
	public PosePacket(DataInputStream is) throws IOException {
		super(is);
	}

	@Override
	public Pose read(DataInputStream is) throws IOException {
		return new Pose(is.readFloat(), is.readFloat(), is.readFloat());
	}
	@Override
	public void write(DataOutputStream os) throws IOException {
		os.writeByte(ID);
		os.writeFloat(data.getX());
		os.writeFloat(data.getY());
		os.writeFloat(data.getHeading());
	}
}