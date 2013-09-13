package ikm;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;

class ImageData {
	ImageData(String file) throws IOException {
		InputStream stream = getClass().getResourceAsStream(file);
		int avail = stream.available();
		System.out.println(avail);
		data = new byte[avail];
		stream.read(data, 0, avail);
		stream.close();
	}
	
	byte[] data;
	Image image;
	
	void loadImage() {
		image = Image.createImage(data, 0, data.length);
	}
	
	void unloadImage() {
		image = null;
	}
	
}

public class Background {
	private ImageData[] images;
	
	public Background() throws IOException {
		images = new ImageData[6];
		
		for (int i = 0; i < 6; i++) {
			images[i] = new ImageData("/background/back" + i + ".png");
			images[i].loadImage();
		}
	}
	
	
}
