package ikm;

import java.io.IOException;

import javax.microedition.lcdui.Image;

public class Res {
	public static Image newgameText;
	public static Image scoreText;
	public static Image exitText;
	public static Image gameoverText;
	public static Image aboutText;

	public static Image scoreBox;
	
	public static Image logo;
	public static Image background;
	public static Image boxImage;
	public static Image ok;
	
	public static void initialize() throws IOException {
		newgameText = Image.createImage("/text/newgame.png");
		scoreText = Image.createImage("/text/highscore.png");
		exitText = Image.createImage("/text/exit.png");
		gameoverText = Image.createImage("/text/gameover.png");
		aboutText = Image.createImage("/text/about.png");
	
		scoreBox = Image.createImage("/score.png");
		
		logo = Image.createImage("/text/loader.png");
	
		background = Image.createImage("/background.png");
		boxImage = Image.createImage("/box.png");
		
		ok = Image.createImage("/text/ok.png");
	}
}
