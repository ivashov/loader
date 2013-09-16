package ikm.state;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ikm.GameState;
import ikm.MainCanvas;
import ikm.Score;
import ikm.util.Maths;

public class HighscoreState extends GameState {
	private Score score;
	private Font normalFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	private Font boldFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	private int x1, y1;
	private int width, height;
	
	public HighscoreState(String str, MainCanvas canvas, Score score) {
		super(str, canvas);
		this.score = score;
		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
		score.loadScore();
	}

	public void paint(Graphics g) {
		renderParent(g);
		g.drawImage(getShade(), 0, 0, Graphics.TOP | Graphics.LEFT);
		super.paint(g);
		score.paint(g, normalFont, boldFont, 5, 195, x1, y1);
	}

	public void update() {
	}

	public int getUpdateRate() {
		return 200;
	}
	
	private Image generateTransparentImage() {
		int h = normalFont.getHeight() * (Score.MAX_SCORES + 1);
		
		x1 = width / 2 - 100;
		int x2 = width / 2 + 100;
		y1 = height / 2 - h / 2;
		int y2 = height / 2 + h / 2;
		
		int[] rgb = new int[width * height];
		for (int i = 0; i < rgb.length; i++) {
			int x = i % width;
			int y = i / width;
			
			if (Maths.pointInRect(x, y, x1, y1, x2 - x1, y2 - y1))
				rgb[i] = 0xcb000010;
			else
				rgb[i] = 0x00000010;
		}
		
		Image img = Image.createRGBImage(rgb, width, height, true);
		return img;
	}
	
	private Image shade;
	private Image getShade() {
		if (shade == null)
			shade = generateTransparentImage();
		
		return shade;
	}
	
	public boolean clicked(int x, int y) {
		if (super.clicked(x, y))
			return true;
		
		canvas.back();
		
		return true;
	}
}
