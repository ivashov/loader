package ikm.scene;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import at.emini.physics2D.Body;
import at.emini.physics2D.Constraint;
import at.emini.physics2D.Joint;
import at.emini.physics2D.Shape;
import at.emini.physics2D.Spring;
import at.emini.physics2D.World;
import at.emini.physics2D.util.FXUtil;
import at.emini.physics2D.util.FXVector;

public class Scene {
	private final Vector objects = new Vector();
	private final World world = new World();
	
	public Scene(int h) {
		world.setGravity(new FXVector(0, FXUtil.toFX(-39 * 4)));
		System.out.println(world.getDampingLateralFX() >> FXUtil.DECIMAL);
		world.setDampingLateralFX(FXUtil.divideFX(FXUtil.toFX(1), FXUtil.toFX(100)));
		Shape boxShape = Shape.createRectangle(240, 10);
		boxShape.setFriction(100);
		Body boxBody = new Body(120, h - 10, boxShape, false);
		world.addBody(boxBody);
		
		boxShape = Shape.createRectangle(240, 10);
		boxShape.setFriction(100);
		boxBody = new Body(120, 5, boxShape, false);
		world.addBody(boxBody);
		
		boxShape = Shape.createRectangle(10, h);
		boxShape.setFriction(100);
		boxBody = new Body(5, h / 2, boxShape, false);
		world.addBody(boxBody);
		
		boxShape = Shape.createRectangle(10, h);
		boxShape.setFriction(100);
		boxBody = new Body(240 - 5, h / 2, boxShape, false);
		world.addBody(boxBody);
	}
	
	public void addBox(Box box, int x, int y) {
		objects.addElement(box);
		Shape boxShape = Shape.createRectangle(box.getWidth(), box.getHeight());
		Body boxBody = new Body(x, y, boxShape, true);
		
		boxShape.setFriction(100);
		boxShape.setMass(150);
		world.addBody(boxBody);
		box.setData(boxBody);
	}
	
	public Vector getBoxes() {
		return objects;
	}
	
	private float fx2float(int fx) {
		int intPart = fx >> FXUtil.DECIMAL;
		int floatPart = fx & FXUtil.DECIMAL;
		
		return intPart + floatPart / (float) (1 << FXUtil.DECIMAL);
	}
	
	private float rad2deg(float rad) {
		return 180 * rad / (float) Math.PI;
	}
	
	public synchronized void update() {
		world.tick();
		
		for (Enumeration en = objects.elements(); en.hasMoreElements();) {
			Box box = (Box) en.nextElement();
			Body body = (Body) box.getData();
			
			int rotation = FXUtil.angleInDegrees2FX(body.rotation2FX());
			FXVector pos = body.positionFX();
			
			box.setRotation(rotation);
			box.setPosition(fx2float(pos.xFX), fx2float(pos.yFX));
		}
	}
	
	private int triangleArea(int x1, int y1, int x2, int y2, int x3, int y3) {
		return Math.abs(((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1))) >>> 1;
	}
	
	public boolean pointInRect(int px, int py, Box box) {
		Body body = (Body) box.getData();
		
		Shape shape = body.shape();
		
		// TODO: add broadphase
		
		FXVector[] vec = body.getVertices();
		int x1 = vec[0].xAsInt();
		int y1 = vec[0].yAsInt();
		
		int x2 = vec[1].xAsInt();
		int y2 = vec[1].yAsInt();
		
		int x3 = vec[2].xAsInt();
		int y3 = vec[2].yAsInt();
		
		int x4 = vec[3].xAsInt();
		int y4 = vec[3].yAsInt();
		
		int a1 = triangleArea(x1, y1, x2, y2, px, py);
		int a2 = triangleArea(x2, y2, x3, y3, px, py);
		int a3 = triangleArea(x3, y3, x4, y4, px, py);
		int a4 = triangleArea(x4, y4, x1, y1, px, py);

		return a1 + a2 + a3 + a4 <= box.getWidth() * box.getHeight();
	}
	
	public Box selectBox(int px, int py) {
		int xx = px << FXUtil.DECIMAL;
		int yy = py << FXUtil.DECIMAL;
		
		Body body = world.findBodyAt(xx, yy);
		if (body == null)
			return null;
		
		for (Enumeration en = objects.elements(); en.hasMoreElements();) {
			Box box = (Box) en.nextElement();
			/*if (pointInRect(px, py, box)) {
				System.out.println("OK");
				return box;
			}*/
			if (box.getData().equals(body)) {
				return box;
			}
		}
		
		return null;
	}
	
	private Spring dragJoint;
	private Body dragBody;
	private Box dragedBox;
	
	public synchronized void startDragBox(Box box, int x, int y) {
		System.out.println("Start drag box");
		
		Body body = (Body) box.getData();
		//body.setGravityAffected(false);
		
		dragedBox = box;
		dragBody = new Body(x, y, Shape.createCircle(1), false);
		dragBody.setInteracting(false);
		dragBody.setGravityAffected(false);
		dragBody.setDynamic(false);
		
		FXVector p1 = body.getRelativePoint(new FXVector(x << FXUtil.DECIMAL, y << FXUtil.DECIMAL));
		FXVector p2 = new FXVector(0, 0);
		
		dragJoint = new Spring(body, dragBody, p1, p2, 5);
		dragJoint.setCoefficient(1000);
		world.addConstraint(dragJoint);
	}
	
	private FXVector vec = new FXVector();
	public synchronized void dragBox(int x, int y) {
		if (dragedBox != null) {
			vec.assignFX(x << FXUtil.DECIMAL, y << FXUtil.DECIMAL);
			dragBody.setPositionFX(vec);
		}
	}
	
	public synchronized void releaseBox() {
		if (dragedBox != null) {
			Body body = (Body) dragedBox.getData();
			body.setGravityAffected(true);
			
			world.removeConstraint(dragJoint);
			world.removeBody(dragBody);
			
			dragedBox = null;
			dragBody = null;
			dragJoint = null;
		}
	}

	public void getJoint(int[] line) {
		if (dragJoint != null) {
			FXVector v1 = dragJoint.getPoint1();
			FXVector v2 = dragJoint.getPoint2();
			line[0] = v1.xAsInt();
			line[1] = v1.yAsInt();
			line[2] = v2.xAsInt();
			line[3] = v2.yAsInt();
		}
	}
}
