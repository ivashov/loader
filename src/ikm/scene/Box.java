package ikm.scene;

public class Box {
	private int width;
	private int height;
	private float rotation;
	private Object data;
	
	private float x, y;
	
	public Box(int w, int h) {
		width = w;
		height = h;
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
	
	public float getScale() {
		return width / 100.0f;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setData(Object obj) {
		this.data = obj;
	}
	
	public Object getData() {
		return data;
	}
	
}
