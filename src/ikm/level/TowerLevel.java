package ikm.level;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameLevel;
import ikm.scene.Scene;
import ikm.state.PlayState;
import ikm.state.ScreenSprite;
import ikm.state.SpriteListener;

public class TowerLevel extends GameLevel {
	private Scene scene;
	private PlayState state;
	private Image boxImage;
	
	long t = System.currentTimeMillis() - 8000;

	private int width, height;
	private int xPos, yPos;
	
	public void initialize(Scene scene, PlayState state, int width, int height) {
		this.scene = scene;
		this.state = state;
		this.width = width;
		this.height = height;
		
		try {
			boxImage = Image.createImage("/box.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		if (System.currentTimeMillis() - t > 8000) {
			createBoxSprite();
			t = System.currentTimeMillis();
		}
	}
	
	private void createBoxSprite() {
		Sprite sprite = new Sprite(boxImage);
		final ScreenSprite screenSprite = new ScreenSprite(sprite, 0, 0);
		state.addSprite(screenSprite);
		screenSprite.setSpriteListener(new SpriteListener() {
			public boolean clicked(ScreenSprite sprite) {
				scene.addBox(25, yPos + height - 25);
				state.removeSprite(screenSprite);
				return false;
			}
		});
	}
}
