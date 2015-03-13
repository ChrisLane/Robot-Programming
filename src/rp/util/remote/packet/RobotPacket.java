package rp.util.remote.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface RobotPacket<E> {
	/**
	 * Writes a Packet to a {@link DataOutputStream} The packet identifier byte should always be written first
	 *
	 * @param os The stream to write to
	 * @throws IOException Exception in writing to the stream
	 */
	void write(DataOutputStream os) throws IOException;
	/**
	 * Reads data from a {@link DataInputStream}. The packet identifier byte shouldn't be read here.
	 *
	 * @param is Stream to parse data from
	 * @return Data parsed from Stream
	 * @throws IOExceptionException in reading from the stream
	 */
	E read(DataInputStream is) throws IOException;
}
