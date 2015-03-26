package rp.util.remote;

import rp.util.remote.packet.ConsolePacket;
import rp.util.remote.packet.PacketSender;

import java.io.PrintStream;

public class ConsoleStream extends PrintStream {
	private final PacketSender ps;
	private final boolean err;

	public ConsoleStream(PacketSender ps) {
		super(null);
		this.ps = ps;
		err = false;
	}
	public ConsoleStream(PacketSender ps, boolean err) {
		super(null);
		this.ps = ps;
		this.err = err;
	}

	@Override
	public void write(int c) {
		print(c);
	}

	/**
	 * Flush any pending output in the stream
	 */
	@Override
	public void flush() {
		// Do nothing
	}

	/*** print() Delegates ***/

	@Override
	public void print(boolean v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(char v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(char[] v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(double v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(float v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(int v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(long v) {
		print(String.valueOf(v));
	}
	@Override
	public void print(Object v) {
		print(String.valueOf(v));
	}

	/**
	 * Writes a string to the underlying output stream.
	 *
	 * @param s the string to print
	 */
	@Override
	public void print(String s) {
		ps.send(new ConsolePacket(s, err));
	}
	/*** println() Delegates ***/

	@Override
	public void println(boolean v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(char v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(char[] v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(double v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(float v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(int v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(long v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(Object v) {
		println(String.valueOf(v));
	}
	@Override
	public void println(String s) {
		print(s + '\n');
	}
}