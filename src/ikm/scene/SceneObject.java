package ikm.scene;

import javax.microedition.lcdui.Graphics;

public abstract class SceneObject {
	public abstract void paint(Graphics g, int height, int x, int y);
	public abstract boolean isDragable();
	private Object data;
	
	protected int rotation;
	protected int x, y;

	public void setData(Object obj) {
		this.data = obj;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRotation(int r) {
		rotation = r;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public float getRotate() {
		return rotation;
	}
}
