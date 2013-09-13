package ikm.level;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameLevel;
import ikm.scene.Scene;
import ikm.state.PlayState;
import ikm.state.play.Overlay;
import ikm.state.play.SpriteListener;
import ikm.state.play.SpriteOverlay;
import ikm.state.play.TextOverlay;

public class TowerLevel extends GameLevel {
	private Scene scene;
	private PlayState state;
	private Image boxImage;
	private boolean floatingBoxActive = false;

	private long t = System.currentTimeMillis() - 3000;
	private long lastHeightCheck = t;

	private int width, height;
	private int xPos, yPos;
	private TextOverlay scoreOverlay;
	private int maxHeight = 0;

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

		scoreOverlay = new TextOverlay("Height: 0", width, 0,
				Font.getDefaultFont(), Graphics.TOP | Graphics.RIGHT);
		state.addSprite(scoreOverlay);
	}

	public void update(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		long time = System.currentTimeMillis();
		if (!floatingBoxActive && time - t > 3000) {
			createBoxSprite();
			t = time;
			floatingBoxActive = true;
		}
		
		if (time - lastHeightCheck > 1000) {
			lastHeightCheck = time;
			
			int height = scene.calculateHeight(maxHeight);
			if (height > maxHeight) {
				maxHeight = height;
				scoreOverlay.setText("Height: " + maxHeight);
			}
		}
	}

	private void createBoxSprite() {
		Sprite sprite = new Sprite(boxImage);
		final Overlay overlay = new SpriteOverlay(sprite, 5, 5);
		state.addSprite(overlay);
		overlay.setSpriteListener(new SpriteListener() {
			public boolean clicked(Overlay sprite) {
				scene.addBox(30, yPos + height - 30);
				state.removeSprite(overlay);
				floatingBoxActive = false;
				t = System.currentTimeMillis();
				return false;
			}
		});
	}
}
