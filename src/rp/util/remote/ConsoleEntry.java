package rp.util.remote;

public class ConsoleEntry {
	public String text;
	public short length;
	public boolean err;

	public ConsoleEntry(String text, short length, boolean err) {
		this.text = text;
		this.length = length;
		this.err = err;
	}
	@Override
	public String toString() {
		return text == null ? "" : (err ? "ConsoleErr> " : "Console> ") + text;
	}
}
