package ikm.state;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ikm.GameState;
import ikm.MainCanvas;
import ikm.Score;
import ikm.util.Maths;

public class AboutState extends GameState {
	private Font normalFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	private Font boldFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	private int x1, y1;
	private int width, height;
	
	public AboutState(String str, MainCanvas canvas) {
		super(str, canvas);
		this.width = canvas.getWidth();
		this.height = canvas.getHeight();
	}

	public void paint(Graphics g) {
		renderParent(g);
		g.drawImage(getShade(), 0, 0, Graphics.TOP | Graphics.LEFT);
		
		int x = width / 2;
		int y = 5 + y1;
		g.setColor(0xbafeff);
		g.drawString("Loader", x, y, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Version: 0.0.1", x, y += 32, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Kirill Ivashov", x, y += 32, Graphics.TOP | Graphics.HCENTER);
		g.drawString("ivashov@cs.karelia.ru", x, y += 32, Graphics.TOP | Graphics.HCENTER);
		
		super.paint(g);
	}

	public void update() {
	}

	public int getUpdateRate() {
		return 200;
	}
	
	private Image generateTransparentImage() {
		int h = normalFont.getHeight() * Score.MAX_SCORES + 10 + boldFont.getHeight();
		
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
