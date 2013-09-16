package ikm.state;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

import ikm.GameState;
import ikm.LoaderMain;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Score;
import ikm.level.TowerLevel;
import ikm.state.play.Overlay;
import ikm.state.play.SpriteListener;
import ikm.state.play.SpriteOverlay;

public class MainMenuState extends GameState {
	private Sprite newgameSprite = new Sprite(Res.newgameText);
	private Sprite scoreSprite = new Sprite(Res.scoreText);
	private Sprite exitSprite = new Sprite(Res.exitText);
	private Sprite logo = new Sprite(Res.logo);
	private Sprite aboutSprite = new Sprite(Res.aboutText);
	
	private int width, height;
	private LoaderMain main;
	private Score score = new Score();
	
	private void addItem(Sprite sprite, int yPos, SpriteListener listener) {
		SpriteOverlay o;
		addOverlay(o = new SpriteOverlay(sprite, width / 2 - sprite.getWidth() / 2, yPos));
		o.setSpriteListener(listener);
	}
	
	public MainMenuState(String name, MainCanvas canvas_, LoaderMain main_) {
		super(name, canvas_);
		this.main = main_;
		width = canvas.getWidth();
		height = canvas.getHeight();

		int y = 100;
		
		addItem(newgameSprite, y, new SpriteListener() {
			public boolean clicked(Overlay sprite) {
				GameState playState = new PlayState("Play", canvas, new TowerLevel(score));
				canvas.pushState(playState);
				
				return true;
			}
		});
		
		addItem(scoreSprite, y + 48, new SpriteListener() {
			public boolean clicked(Overlay sprite) {
				GameState state = new HighscoreState("Highscore", canvas, score);
				canvas.pushState(state);
				
				//GameState state = new RecordState("New record", canvas, 10, score); 
				//canvas.pushState(state);
				
				return true;
			}
		});
		
		addItem(aboutSprite, y + 48 * 2, new SpriteListener() {
			public boolean clicked(Overlay sprite) {
				GameState state = new AboutState("About", canvas);
				canvas.pushState(state);

				return true;
			}
		});
		
		addItem(exitSprite, y + 48 * 3, new SpriteListener() {
			public boolean clicked(Overlay sprite) {
				main.quit();
				return false;
			}
		});
		
		addItem(logo, 16, null);
		
		score.loadScore();
	}

	public void update() {
	}

	public void paint(Graphics g) {
		g.drawRegion(Res.background, 0, Res.background.getHeight() - height, width, height, 0, 0, 0, Graphics.TOP | Graphics.LEFT);
		
		newgameSprite.paint(g);
		scoreSprite.paint(g);
		exitSprite.paint(g);
		logo.paint(g);
		aboutSprite.paint(g);
		
		g.drawImage(Res.boxImage, 0, height - 10, Graphics.BOTTOM | Graphics.LEFT);
		g.drawImage(Res.boxImage, width, height - 10, Graphics.BOTTOM | Graphics.RIGHT);
	}

	public int getUpdateRate() {
		return 100;
	}
}
