package ikm.state;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ikm.GameLevel;
import ikm.GameState;
import ikm.MainCanvas;
import ikm.scene.Box;
import ikm.scene.Scene;
import ikm.scene.SceneObject;
import ikm.util.Maths;

public class PlayState extends GameState {
    private Scene scene;
	private int yPos = 0;
	private Image background;
	private GameLevel game;

	private int width, height;
	private Vector sprites = new Vector();
	
	public static Font font = Font.getDefaultFont();
	public static Font largeFont;
	static {
		largeFont = Font.getFont(font.getFace(), font.getStyle(), Font.SIZE_LARGE);
	}
	
	int i = 0;
	public PlayState(String name, MainCanvas canvas, GameLevel game) {
		super(name, canvas);
		scene = new Scene(canvas.getHeight());
		this.game = game;
		createImage();
		
		width = canvas.getWidth();
		height = canvas.getHeight();
		
		game.initialize(scene, this, width, height);
	}
	
	public void update() {
		game.update(0, yPos);
		scene.update();
	}
	
	private int[] line = new int[4];
	public void paint(Graphics g) {
        g.setColor(127, 127, 255);
        
        g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
        int imgpos = Maths.clamp(background.getHeight() - yPos - height, 0, background.getHeight() - height);
        g.drawRegion(background, 0, imgpos, width, height, 0, 0, 0, Graphics.TOP | Graphics.LEFT);
        
        long ttt = System.currentTimeMillis();
        for (Enumeration en = scene.getBoxes().elements(); en.hasMoreElements();) {
        	SceneObject box = (SceneObject) en.nextElement();
        	box.paint(g, height, 0, yPos);
        }
        
        for (Enumeration en = sprites.elements(); en.hasMoreElements();) {
        	Overlay box = (Overlay) en.nextElement();
        	box.paint(g);
        }
        
        scene.getJoint(line);
        g.setColor(255, 0, 0);
        g.drawLine(line[0], height - line[1] + yPos, line[2], height - line[3] + yPos);
        
        g.setFont(font);
        g.setColor(~0);
        g.drawString("Render time: " + String.valueOf(System.currentTimeMillis() - ttt), 0, 0, Graphics.TOP | Graphics.LEFT);
	}

	public int getUpdateRate() {
		return 50;
	}
	
	private void createImage() {	
		try {
			background = Image.createImage("/background.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
 	}
	
	public void addSprite(Overlay sprite) {
		sprite.setPlayState(this);
		addClickable(sprite);
		sprites.addElement(sprite);
	}
	
	public void removeSprite(Overlay sprite) {
		sprites.removeElement(sprite);
		removeClickable(sprite);
	}
	
	private int dragStartX;
	private int dragStartY;
	private boolean drag = false;
	
	public boolean clicked(int x, int y) {
		if (super.clicked(x, y)) {
			return true;
		}
		
		int yy = height - y + yPos;
		
		SceneObject object = scene.selectObject(x, yy);
		
		if (object != null) {
			scene.startDragBox(object, x, yy);
			return true;
		} else {
			drag = true;
			dragStartX = x;
			dragStartY = y;
		}
		
		return true;
	}
	
	public boolean released(int x, int y) {
		int yy = height - y + yPos;
		
		scene.releaseBox();
		drag = false;
		
		return super.released(x, yy);
	}
	
	public boolean dragged(int x, int y) {
		int yy = height - y + yPos;
		scene.dragBox(x, yy);
		
		if (drag) {
			yPos += y - dragStartY;
			if (yPos < 0)
				yPos = 0;
			
			dragStartY = y;
			dragStartX = x;
		}
		
		return super.dragged(x, y);
	}
}
