package ikm.scene;

import java.io.IOException;

import ikm.util.Maths;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Box extends SceneObject {
	private static Image boxesImage;
	static {
		try {
			boxesImage = Image.createImage("/out.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final int DEGREE_STEP = 4;
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
	
	public void paint(Graphics g, int height, int xPos, int yPos) {
		int x = (int) this.x;
		int y = height - (int) this.y + yPos;
		int rotation = 360 - (int) this.rotation;
		
		rotation = (Maths.mod(rotation, 360) + DEGREE_STEP / 2) / DEGREE_STEP;
		if (rotation == 360 / DEGREE_STEP)
			rotation = 0;
		
		int u = rotation % 10;
		int v = rotation / 10;
		g.drawRegion(boxesImage, u * 75, v * 75, 75, 75, 0, x, y,
				Graphics.VCENTER | Graphics.HCENTER);
	}
	
	public boolean isDragable() {
		return true;
	}
}
