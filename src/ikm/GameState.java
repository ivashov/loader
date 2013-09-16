package ikm;

import ikm.state.play.Overlay;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public abstract class GameState {
	protected String name;
	
	private Vector clickables = new Vector();
	private Vector dragables = new Vector();
	
	protected MainCanvas canvas;
	private GameState parent;
	private Vector sprites = new Vector();

	public interface Clickable {
		boolean clicked(int x, int y);
	}
	
	public interface Dragable {
		boolean draged(int x, int y);
		boolean released(int x, int y);
	}
	
	public GameState(String name, MainCanvas canvas) {
		this.name = name;
		this.canvas = canvas;
	}
	
	public void addClickable(Clickable c) {
		clickables.addElement(c);
	}
	
	public void addDragable(Dragable d) {
		dragables.addElement(d);
	}
	
	public void removeDragable(Dragable d) {
		dragables.removeElement(d);
	}
	
	public void removeClickable(Clickable c) {
		clickables.removeElement(c);
	}
	
	public boolean clicked(int x, int y) {
		for (Enumeration en = clickables.elements(); en.hasMoreElements();) {
			Clickable clk = (Clickable) en.nextElement();
			if (clk.clicked(x, y))
				return true;
		}
		
		return false;
	}
	
	public boolean released(int x, int y) {
		for (Enumeration en = dragables.elements(); en.hasMoreElements();) {
			Dragable clk = (Dragable) en.nextElement();
			if (clk.released(x, y))
				return true;
		}
		
		return false;
	}
	
	public boolean dragged(int x, int y) {
		for (Enumeration en = dragables.elements(); en.hasMoreElements();) {
			Dragable clk = (Dragable) en.nextElement();
			if (clk.draged(x, y))
				return true;
		}
		
		return false;
	}
	
	final void render(Graphics g, GameState parent) {
		this.parent = parent;		
		paint(g);
		this.parent = null;
	}
	
	protected void renderParent(Graphics g) {
		if (parent != null) {
			parent.render(g, null);
			//g.drawImage(canvas.getTransparentImage(), 0, 0, Graphics.TOP | Graphics.LEFT);
		}
	}
	
	public boolean needExtraRedraw() {
		return false;
	}
	
	public void addOverlay(Overlay sprite) {
		addClickable(sprite);
		sprites.addElement(sprite);
	}
	
	public void removeOverlay(Overlay sprite) {
		sprites.removeElement(sprite);
		removeClickable(sprite);
	}
		
	public abstract void update();
	public void paint(Graphics g) {
        for (Enumeration en = sprites.elements(); en.hasMoreElements();) {
        	Overlay box = (Overlay) en.nextElement();
        	box.paint(g);
        }
	}
	public abstract int getUpdateRate();
	public void shutdown() {}
}
