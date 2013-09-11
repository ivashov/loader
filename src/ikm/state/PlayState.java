package ikm.state;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableGraphics;

import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGPath;
import org.w3c.dom.svg.SVGRGBColor;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGSVGElement;

import ikm.GameState;
import ikm.MainCanvas;
import ikm.scene.Box;
import ikm.scene.Scene;

public class PlayState extends GameState {
    private ScalableGraphics sg;
    private SVGImage svgImage;
    private Scene scene;
	private SVGSVGElement rootElement;
	private Image boxesImage;
	public final int DEGREE_STEP = 4;
	private int height;
	
	int i = 0;
	public PlayState(String name, MainCanvas canvas) {
		super(name, canvas);
		scene = new Scene(canvas.getHeight());
		sg = ScalableGraphics.createInstance();
		sg.setRenderingQuality(ScalableGraphics.RENDERING_QUALITY_LOW);
		height = canvas.getHeight();
		createImage();
		
		try {
			boxesImage = Image.createImage("/out.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	long t = System.currentTimeMillis() - 8000;
	long tt;

	
	
	public void update() {
		if (System.currentTimeMillis() - t > 8000) {
			t = System.currentTimeMillis();
			scene.addBox(new Box(50, 50), 120, 270);
		}
		
		scene.update();
	}
	
	private int mod(int a, int b) {
		return (b + (a % b)) % b;
	}
	
	private int[] line = new int[4];
	public void paint(Graphics g) {
        g.setColor(127, 127, 255);
        
        g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());

        sg.bindTarget(g);
        sg.setTransparency(1f);
        
        long ttt = System.currentTimeMillis();
        for (Enumeration en = scene.getBoxes().elements(); en.hasMoreElements();) {
        	Box box = (Box) en.nextElement();
			if (true) {
				int x = (int) box.getX();
				int y = height - (int) box.getY();
				int rotation = 360 - (int) box.getRotate();
				
				rotation = (mod(rotation, 360) + DEGREE_STEP / 2) / DEGREE_STEP;
				if (rotation == 360 / DEGREE_STEP)
					rotation = 0;
				
				int u = rotation % 10;
				int v = rotation / 10;
				g.drawRegion(boxesImage, u * 75, v * 75, 75, 75, 0, x, y,
						Graphics.VCENTER | Graphics.HCENTER);
			} else {
				rootElement.getCurrentTranslate().setX(box.getX());
				rootElement.getCurrentTranslate().setY(box.getY());
				rootElement.setCurrentScale(box.getScale());
				rootElement.setCurrentRotate(box.getRotate());
				sg.render(0, 0, svgImage);
			}
        }
        
        scene.getJoint(line);
        g.setColor(255, 0, 0);
        g.drawLine(line[0], height - line[1], line[2], height - line[3]);
        
        g.setColor(0);
        g.drawString(String.valueOf(System.currentTimeMillis() - ttt), 0, 0, Graphics.TOP | Graphics.LEFT);
        sg.releaseTarget();
	}

	public int getUpdateRate() {
		return 50;
	}
	
	private void createImage() {		
		try {
			svgImage = (SVGImage) SVGImage.createImage(getClass().getResourceAsStream("/box1.svg"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		svgImage.setViewportHeight(canvas.getHeight());
		svgImage.setViewportWidth(canvas.getWidth());
		
		rootElement = (SVGSVGElement) (svgImage.getDocument().getDocumentElement());
		rootElement.getCurrentTranslate().setX(75);
		rootElement.getCurrentTranslate().setY(75);
 	}
	
	public boolean clicked(int x, int y) {
		y = height - y;
		
		Box box = scene.selectBox(x, y);
		
		if (box != null) {
			scene.startDragBox(box, x, y);
			return true;
		}
		
		return super.clicked(x, y);
	}
	
	public boolean released(int x, int y) {
		y = height - y;
		
		scene.releaseBox();
		
		return super.released(x, y);
	}
	
	public boolean dragged(int x, int y) {
		y = height - y;
		
		scene.dragBox(x, y);
		return super.dragged(x, y);
	}
}
