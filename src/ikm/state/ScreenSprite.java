package ikm.state;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameState;
import ikm.GameState.Clickable;
import ikm.util.Maths;

public class ScreenSprite implements Clickable {
	private int y;
	private int x;
	private Sprite sprite;
	private PlayState playState;
	private SpriteListener listener;

	public ScreenSprite(Sprite sprite, int x, int y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}
	
	public void setSpriteListener(SpriteListener listener) {
		this.listener = listener;
	}

	public boolean clicked(int xx, int yy) {
		if (Maths.pointInRect(xx, yy, x, y, sprite.getWidth(), sprite.getHeight())) {
			return listener.clicked(this);
		} else {
			return false;
		}
	}

	void setPlayState(PlayState playState) {
		this.playState = playState;
	}

	public void paint(Graphics g) {
		sprite.paint(g);
	}
}
