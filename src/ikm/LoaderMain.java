package ikm;

import ikm.level.TowerLevel;
import ikm.state.PlayState;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class LoaderMain extends MIDlet implements Application, CommandListener {
	private MainCanvas canvas;
	private Display display;
	private Command back = new Command("Back", Command.BACK, 0);
	
	public LoaderMain() {
		
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		canvas.stop();
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		canvas = new MainCanvas(this);
		canvas.setCommandListener(this);
		canvas.addCommand(back);
		
		display = Display.getDisplay(this);
		GameState playState = new PlayState("Play", canvas, new TowerLevel());
		canvas.pushState(playState);
		display.setCurrent(canvas);
		
		new Thread(canvas).start();
	}

	public void quit() {
		try {
			destroyApp(false);
		} catch (MIDletStateChangeException e) {
			e.printStackTrace();
		}
		notifyDestroyed();		
	}

	public void restart() {
		
	}

	public boolean hasHardwareBack() {
		return false;
	}

	public void commandAction(Command c, Displayable d) {
		if (c.equals(back)) {
			if (!canvas.back()) {
				quit();
			}
		}
	}
}
