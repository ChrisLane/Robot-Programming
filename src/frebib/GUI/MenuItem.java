package frebib.GUI;

import lejos.nxt.LCD;

public class MenuItem extends Object {
	private final short SCROLLSTART = 2000; // all in ms
	private final short SCROLLDELAY = 100;
	private final short FLASHDELAY = 300;

	private boolean selected, selectFlash;
	private long lastFlashTime, lastScrollTime, selectTime;

	private final Menu menu;
	private final String text;
	private final byte indent;
	private byte scrollAmnt;
	private final Object tag;

	public MenuItem(Menu menu, String text) {
		super();
		this.menu = menu;
		this.text = text;
		this.indent = 0;
		this.tag = null;
	}

	public MenuItem(Menu menu, String text, byte indent, Object tag) {
		super();
		this.menu = menu;
		this.text = text;
		this.indent = indent;
		this.tag = tag;
	}

	private void update() {
		long now = System.currentTimeMillis();
		if (this.selected) {
			if (now > selectTime + SCROLLDELAY) {
				while (lastScrollTime > now - SCROLLDELAY) {
					this.scrollAmnt++;
					this.lastScrollTime += SCROLLDELAY;
				}
			}
			if (now > lastFlashTime + FLASHDELAY)
				selectFlash = !selectFlash;
		}
	}

	public void draw(byte line) {
		update();

		// Draw plusses for indent
		for (int i = 0; i < indent; i++)
			LCD.drawChar('+', i, line);

		// Draw text and "> TEXT <" selection indicators
		LCD.drawString(text.substring(scrollAmnt, LCD.DISPLAY_CHAR_WIDTH + scrollAmnt), indent, line);
		if (selected && selectFlash) {
			LCD.drawChar('>', 0, line);
			LCD.drawChar('<', LCD.DISPLAY_CHAR_WIDTH - 1, line);
		}
	}

	public void setSelected() {
		this.selected = true;
		this.selectTime = System.currentTimeMillis();
		this.lastScrollTime = System.currentTimeMillis() + SCROLLSTART;
		this.selectFlash = true;
		this.lastFlashTime = System.currentTimeMillis() + FLASHDELAY;
	}

	public void setDeselected() {
		this.selected = false;
		this.selectTime = -1;
		this.scrollAmnt = 0;
	}
}
