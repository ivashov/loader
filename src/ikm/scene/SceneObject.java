package ikm.scene;

import javax.microedition.lcdui.Graphics;

public abstract class SceneObject {
	public abstract void paint(Graphics g, int height, int x, int y);
	public abstract boolean isDragable();
	private Object data;
	
	protected float rotation;
	protected float x, y;

	public void setData(Object obj) {
		this.data = obj;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRotation(float r) {
		rotation = r;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getRotate() {
		return rotation;
	}

}
