package ikm.state;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.game.Sprite;

import com.nokia.mid.ui.TextEditor;

import ikm.GameState;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Score;
import ikm.state.play.Overlay;
import ikm.state.play.SpriteListener;
import ikm.state.play.SpriteOverlay;

public class RecordState extends GameState implements SpriteListener {
	private static final int GAMEOVER_POS = 5 + 8,
			SCOREBOX_POS = 97 - 8,
			LABEL_POS = 130 - 8,
			INPUT_POS = 145 - 8,
			OK_POS = 179 - 8;
	
	private int width, height;
	
	private Font largeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	private int score;
	private TextEditor textEditor;
	private SpriteOverlay okButton;
	private Score storage;
	
	public RecordState(String name, MainCanvas canvas, int record, Score storage) {
		super(name, canvas);
		width = canvas.getWidth();
		height = canvas.getHeight();
		this.storage = storage;
	
		this.score = record;
		textEditor = TextEditor.createTextEditor(12, TextField.NON_PREDICTIVE, 160, 1);
		textEditor.setMultiline(false);
		textEditor.setParent(canvas);

		textEditor.setPosition(width / 2 - 80, INPUT_POS);
		textEditor.setVisible(true);
		textEditor.setFocus(true);
		textEditor.setForegroundColor(0xafffffff);
		
		okButton  = new SpriteOverlay(new Sprite(Res.ok), width / 2 - Res.ok.getWidth() / 2, OK_POS);
		addOverlay(okButton);
		okButton.setSpriteListener(this);
	}

	public void update() {
	}

	public int getUpdateRate() {
		return 200;
	}
	
	public void paint(Graphics g) {
		g.drawRegion(Res.background, 0, Res.background.getHeight() - height, width, height, 0, 0, 0, Graphics.TOP | Graphics.LEFT);
				
		g.drawImage(Res.gameoverText, width / 2, GAMEOVER_POS, Graphics.HCENTER | Graphics.TOP);
		g.drawImage(Res.boxImage, 0, height - 10, Graphics.BOTTOM | Graphics.LEFT);
		g.drawImage(Res.boxImage, width, height - 10, Graphics.BOTTOM | Graphics.RIGHT);
		
		
		g.setFont(largeFont);
		g.setColor(0xbafeff);
		g.drawImage(Res.scoreBox, width / 2, SCOREBOX_POS, Graphics.HCENTER | Graphics.VCENTER);
		g.drawString(String.valueOf(score), width / 2, SCOREBOX_POS + 7, Graphics.HCENTER | Graphics.BASELINE);
		
		g.drawString("Enter your name: ", width / 2, LABEL_POS, Graphics.HCENTER | Graphics.BASELINE);
		
		super.paint(g);
	}
	
	public void shutdown() {
		textEditor.setVisible(false);
		textEditor.setParent(null);
	}

	public boolean clicked(Overlay sprite) {
		String name = textEditor.getContent();
		storage.addRecord(name, score);
		storage.saveScore();
		canvas.back();
		
		return false;
	}
	
	public boolean clicked(int x, int y) {
		if (super.clicked(x, y))
			return true;
		
		return true;
	}
}
