package ikm;

import java.io.IOException;

import ikm.level.TowerLevel;
import ikm.state.MainMenuState;
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

	/*{
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println((Runtime.getRuntime().freeMemory() / 1024) + "/"
							+ (Runtime.getRuntime().totalMemory() / 1024));
				}
			}
		}.start();
	}*/

	public LoaderMain() {
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		if (canvas != null)
			canvas.stop();
	}
	
	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			Res.initialize();
		} catch (IOException e) {
			throw new MIDletStateChangeException("Can't load resources");
		}
		canvas = new MainCanvas(this);
		canvas.setCommandListener(this);
		canvas.addCommand(back);
		
		display = Display.getDisplay(this);
		
		
		//GameState playState = new PlayState("Play", canvas, new TowerLevel());
		GameState playState = new MainMenuState("Menu", canvas, this);
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
