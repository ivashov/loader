package ikm.state.play;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameState;
import ikm.GameState.Clickable;
import ikm.state.PlayState;
import ikm.util.Maths;

public abstract class Overlay implements Clickable {
	protected int y;
	protected int x;
	private SpriteListener listener;

	public Overlay(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean clicked(int xx, int yy) {
		if (listener != null && hits(xx, yy)) {
			return listener.clicked(this);
		}
		
		return false;
	}
	
	public void setSpriteListener(SpriteListener listener) {
		this.listener = listener;
	}
	
	public abstract void paint(Graphics g);
	public abstract boolean hits(int x, int y);
}
