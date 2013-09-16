package ikm.level;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import at.emini.physics2D.Collision;

import ikm.GameLevel;
import ikm.Res;
import ikm.Score;
import ikm.scene.Scene;
import ikm.state.PlayState;
import ikm.state.play.Overlay;
import ikm.state.play.SpriteListener;
import ikm.state.play.SpriteOverlay;
import ikm.state.play.TextOverlay;

public class TowerLevel extends GameLevel {
	private Scene scene;
	private PlayState state;
	private boolean floatingBoxActive = false;

	private long t = System.currentTimeMillis() - 3000;
	private long lastHeightCheck = t;

	private int width, height;
	private int xPos, yPos;
	private TextOverlay scoreOverlay;
	private int maxHeight = 0;
	private Score score;

	public TowerLevel(Score score) {
		this.score = score;
	}

	public void initialize(Scene scene, PlayState state, int width, int height) {
		this.scene = scene;
		this.state = state;
		this.width = width;
		this.height = height;

		scoreOverlay = new TextOverlay("Height: 0", width, 0,
				Font.getDefaultFont(), Graphics.TOP | Graphics.RIGHT);
		state.addOverlay(scoreOverlay);
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
			
			int height = scene.calculateHeight(0);
			if (height > maxHeight) {
				maxHeight = height;
				scoreOverlay.setText("Height: " + maxHeight);
			}
			
			if (maxHeight - height > 100) {
				state.finished(maxHeight, score);
			}
		}
	}

	private void createBoxSprite() {
		Sprite sprite = new Sprite(Res.boxImage);
		final Overlay overlay = new SpriteOverlay(sprite, 5, 5);
		state.addOverlay(overlay);
		overlay.setSpriteListener(new SpriteListener() {
			public boolean clicked(Overlay sprite) {
				scene.addBox(30, yPos + height - 30);
				state.removeOverlay(overlay);
				floatingBoxActive = false;
				t = System.currentTimeMillis();
				return false;
			}
		});
	}
}
