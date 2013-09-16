package ikm.state.play;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class TextOverlay extends Overlay {
	private Font font;
	private String str;
	private int align;
	
	public TextOverlay(String str, int x, int y, Font font,  int align) {
		super(x, y);
		
		this.font = font;
		this.str = str;
		this.align = align;
	}
	
	public TextOverlay(String str, int x, int y) {
		this(str, x, y, Font.getDefaultFont(), Graphics.TOP | Graphics.LEFT);
	}
	
	public void setText(String str) {
		this.str = str;
	}

	public void paint(Graphics g) {
		g.setColor(0xbafeff);
		g.setFont(font);
		g.drawString(str, x, y, align);
	}

	public boolean hits(int x, int y) {
		return false;
	}
}
