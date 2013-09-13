package ikm;

import java.io.IOException;

import javax.microedition.lcdui.Image;

public class Res {
	public static Image newgameText;
	public static Image scoreText;
	public static Image exitText;
	public static Image logo;
	
	public static void initialize() throws IOException {
		newgameText = Image.createImage("/text/newgame.png");
		scoreText = Image.createImage("/text/highscore.png");
		exitText = Image.createImage("/text/exit.png");
	
		logo = Image.createImage("/text/loader.png");
	}
}
