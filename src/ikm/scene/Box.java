package ikm.scene;

import java.io.IOException;

import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Box extends SceneObject {
	private static Image boxesImage;
	static {
		try {
			boxesImage = Image.createImage("/out.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final int DEGREE_STEP = 1;
	private int width;
	private int height;
	
	
	public Box(int w, int h) {
		width = w;
		height = h;
	}	
	
	public float getScale() {
		return width / 100.0f;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private static final int[] trans = {Sprite.TRANS_NONE, Sprite.TRANS_ROT90, Sprite.TRANS_ROT180, Sprite.TRANS_ROT270};
	public void paint(Graphics g, int height, int xPos, int yPos) {
		int x = (int) this.x;
		int y = height - (int) this.y + yPos;
		int rotation = 360 - (int) this.rotation;
		rotation = Maths.mod(rotation, 360);
		
		int r = rotation / 90;
		rotation = rotation % 90;
		
		int uv  = (rotation + DEGREE_STEP / 2) / DEGREE_STEP;
		if (uv == 90 / DEGREE_STEP)
			rotation = 0;
		
		int u = uv % 10;
		int v = uv / 10;
		
		g.drawRegion(boxesImage, u * 72, v * 72, 72, 72, trans[r], x, y,
				Graphics.VCENTER | Graphics.HCENTER);
	}
	
	public boolean isDragable() {
		return true;
	}
}
