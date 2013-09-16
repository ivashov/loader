package ikm.state;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;

import ikm.GameLevel;
import ikm.GameState;
import ikm.MainCanvas;
import ikm.Res;
import ikm.Score;
import ikm.scene.Box;
import ikm.scene.Scene;
import ikm.scene.SceneObject;
import ikm.state.play.Overlay;
import ikm.util.Maths;

public class PlayState extends GameState {
    private Scene scene;
	private int yPos = 0;
	private GameLevel game;

	private int width, height;
	
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
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
        int imgpos = Maths.clamp(Res.background.getHeight() - yPos - height, 0, Res.background.getHeight() - height);
        g.drawRegion(Res.background, 0, imgpos, width, height, 0, 0, 0, Graphics.TOP | Graphics.LEFT);
        
        long ttt = System.currentTimeMillis();
        for (Enumeration en = scene.getBoxes().elements(); en.hasMoreElements();) {
        	SceneObject box = (SceneObject) en.nextElement();
        	box.paint(g, height, 0, yPos);
        }
        
        if (scene.isDragging()) {
        	scene.getJoint(line);
			dg.setARGBColor(0xaabaabff);
        	g.drawLine(line[0], height - line[1] + yPos, line[2], height - line[3] + yPos);
        }
        g.setFont(font);
        g.setColor(0xbafeff);
        
		super.paint(g);
        //g.drawString("Render time: " + String.valueOf(System.currentTimeMillis() - ttt), 0, 0, Graphics.TOP | Graphics.LEFT);	
	}

	public int getUpdateRate() {
		return 50;
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
	
	public void finished(int record, Score score) {
		canvas.back();
		if (score.isTopN(record)) {
			RecordState recordState = new RecordState("New record", canvas, record, score);
			canvas.pushState(recordState);
		}
	} 
}
