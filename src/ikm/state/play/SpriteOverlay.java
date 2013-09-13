package ikm.state.play;

import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

public class SpriteOverlay extends Overlay {
	private Sprite sprite;

	public SpriteOverlay(Sprite sprite, int x, int y) {
		super(x, y);
		this.sprite = sprite;
		sprite.setPosition(x, y);
	}

	public int getWidth() {
		return sprite.getWidth();
	}

	public int getHeight() {
		return sprite.getHeight();
	}
	
	public boolean hits(int xx, int yy) {
		return Maths.pointInRect(xx, yy, x, y, getWidth(), getHeight());

	}
	
	public void paint(Graphics g) {
		sprite.paint(g);
	}
}