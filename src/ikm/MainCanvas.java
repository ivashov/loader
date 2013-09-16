package ikm;

import ikm.util.Maths;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class MainCanvas extends GameCanvas implements Runnable {
	public static final int FRAME_TIME = 50;
	public Vector states = new Vector();
	
	private boolean cont = true;
	private long gameTime;

	private Image darkImage;
	private Graphics g;
	private Application main;
	private boolean backVisible = false;
	
	protected MainCanvas(Application main) {
		super(false);
		setFullScreenMode(true);
		
		this.main = main;
	}
	
	public void pushState(GameState state) {
		synchronized (this) {
			states.addElement(state);
			this.notifyAll();
			gameTime = System.currentTimeMillis();
		}
	}
	
	public void popState() {
		synchronized (this) {
			states.removeElementAt(states.size() - 1);
		}
	}
	
	public GameState currentState() {
		return (GameState) states.elementAt(states.size() - 1);
	}
	
	public GameState previousState() {
		return (GameState) (states.size() == 1 ? null : states.elementAt(states.size() - 2));
	}

	protected void pointerPressed(int x, int y) {
		synchronized (this) {
			if (backVisible && Maths.pointInRect(x, y, getWidth() - 32, getHeight() - 16, 32, 16)) {
				back();
				return;
			}
			
			currentState().clicked(x, y);
			if (currentState().needExtraRedraw()) {
				this.notifyAll();
			}
		}
	}
	
	protected void pointerReleased(int x, int y) {
		synchronized (this) {
			currentState().released(x, y);
			if (currentState().needExtraRedraw()) {
				this.notifyAll();
			}
		}
	}
	
	protected void pointerDragged(int x, int y) {
		synchronized (this) {
			currentState().dragged(x, y);
			if (currentState().needExtraRedraw()) {
				this.notifyAll();
			}
		}
	}
	
	public void redraw() {
		synchronized (this) {
			currentState().render(g, previousState());
			flushGraphics();
		}
	}
	
	public void quit() {
		main.quit();
	}
	
	public void stop() {
		synchronized (this) {
			while (back())
				;
			currentState().shutdown();
			
			cont = false;
			notifyAll();
		}
	}
	
	private void safeRun() {
		g = getGraphics();
		gameTime = System.currentTimeMillis();
		
		while (cont) {
			synchronized (this) {
				GameState state = currentState();
				int rate = state.getUpdateRate();

				currentState().render(g, previousState());
				paintBackButton(g);
				flushGraphics();
				
				if (System.currentTimeMillis() >= gameTime) {
					state.update();
					gameTime += rate;
				}

				long sleep = gameTime - System.currentTimeMillis();

				if (sleep <= 0)
					sleep = 1;

				try {
					this.wait(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void run() {
		safeRun();
	}
	
	private void paintBackButton(Graphics g) {
		if (!main.hasHardwareBack() && states.size() > 1) {
			int posX = getWidth();
			int posY = getHeight();
			backVisible = true;
			g.setColor(0xbafeff);
			g.drawString(/*Translation.tr(*/"Back"/*)*/, posX, posY, Graphics.BOTTOM | Graphics.RIGHT);
		} else
			backVisible = false;
	}
	
	public boolean back() {
		if (states.size() > 1) {
			currentState().shutdown();
			popState();
			return true;
		} else {
			return false;
		}
	}

	public void restart() {
		synchronized(this) {
			notifyAll();
			main.restart();
		}
	}
}
