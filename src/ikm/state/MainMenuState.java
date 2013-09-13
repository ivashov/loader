package ikm.state;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameState;
import ikm.MainCanvas;
import ikm.Res;

public class MainMenuState extends GameState {
	private Sprite newgameSprite = new Sprite(Res.newgameText);
	private Sprite scoreSprite = new Sprite(Res.scoreText);
	private Sprite exitSprite = new Sprite(Res.exitText);
	private Sprite logo = new Sprite(Res.logo);
	
	private int width, height;
	
	private void referenceToCenter(Sprite sprite) {
		sprite.defineReferencePixel(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}
	
	public MainMenuState(String name, MainCanvas canvas) {
		super(name, canvas);
		
		width = canvas.getWidth();
		height = canvas.getHeight();
		
		referenceToCenter(newgameSprite);
		referenceToCenter(scoreSprite);
		referenceToCenter(exitSprite);
		referenceToCenter(logo);

		newgameSprite.setRefPixelPosition(width / 2, 128);
		scoreSprite.setRefPixelPosition(width / 2, 192);
		exitSprite.setRefPixelPosition(width / 2, 256);
		logo.setRefPixelPosition(width / 2, 64);
	}

	public void update() {
	}

	public void paint(Graphics g) {
		g.setColor(0, 55, 240);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		
		newgameSprite.paint(g);
		scoreSprite.paint(g);
		exitSprite.paint(g);
		logo.paint(g);
	}

	public int getUpdateRate() {
		return 100;
	}

}
