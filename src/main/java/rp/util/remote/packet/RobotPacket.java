package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class RobotPacket<E> {
	protected E data;
	public RobotPacket(E data) {
		this.data = data;
	}
	public RobotPacket(DataInputStream is) throws IOException {
		data = read(is);
	}
	/**
	 * Writes a Packet to a {@link DataOutputStream} The packet identifier byte should always be written first
	 *
	 * @param os The stream to write to
	 * @throws IOException Exception in writing to the stream
	 */
	public abstract void write(DataOutputStream os) throws IOException;
	/**
	 * Reads data from a {@link DataInputStream}. The packet identifier byte shouldn't be read here.
	 *
	 * @param is Stream to parse data from
	 * @return Data parsed from Stream
	 * @throws IOException in reading from the stream
	 */
	protected abstract E read(DataInputStream is) throws IOException;
	/**
	 * Gets the data from a packet
	 *
	 * @return Packet Data
	 */
	public E getData() {
		return data;
	}
}