package frebib.GUI;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

import java.util.ArrayList;

public abstract class Menu implements ButtonListener {
	protected Menu parent;
	protected String title;
	protected int selected;
	protected ArrayList<MenuItem> items;

	public Menu(String title) {
		this.title = title;
	}

	public void add(MenuItem i) {
		items.add(i);
		draw();
	}

	public void sort() {
		// TODO: Collections.sort(items);
	}

	public void draw() {
		LCD.clear();
		// Print in centre of screen on top row
		LCD.drawString(title, (int) Math.ceil((LCD.DISPLAY_CHAR_WIDTH - title.length() + 1) / 2), 0);
		// Invert row pixels
		LCD.bitBlt(LCD.getDisplay(), LCD.SCREEN_WIDTH, LCD.SCREEN_HEIGHT, 0, 0, 0, 0, LCD.SCREEN_WIDTH, LCD.FONT_HEIGHT, LCD.ROP_INVERT);

		LCD.refresh();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSelectedIndex() {
		return selected;
	}

	public MenuItem getSelectedItem() {
		if (selected == -1)
			return null;
		return this.items.get(selected);
	}

	@Override
	public void buttonPressed(Button b) {
		switch (b.getId()) {
			case Button.ID_ENTER:
				break;
			case Button.ID_ESCAPE:
				break;
			case Button.ID_LEFT:
				break;
			case Button.ID_RIGHT:
				break;
		}
	}

	@Override
	public void buttonReleased(Button b) {
	}
}
