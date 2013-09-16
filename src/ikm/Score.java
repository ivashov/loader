package ikm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

class Item {
	Item(String s, int n) {
		name = s;
		score = n;
	}
	
	Item() {
		
	}
	
	String name;
	int score;
}

public class Score {
	public static final int MAX_SCORES = 10;

	private RecordStore rms;
	private Vector scores = new Vector(MAX_SCORES);
	private boolean loaded = false;

	public Score() {
	}

	public void saveScore() {
		try {
			rms = RecordStore.openRecordStore("loader-scores", true);
			if (rms.getNumRecords() == 0) {
				rms.addRecord(new byte[4], 0, 4);
			}

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(bout);

			dout.writeInt(scores.size());
			for (int i = 0; i < scores.size(); i++) {
				Item item = (Item) scores.elementAt(i);
				dout.writeUTF(item.name);
				dout.writeInt(item.score);
			}

			byte[] arr = bout.toByteArray();
			rms.setRecord(1, arr, 0, arr.length);
			rms.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadScore() {
		if (loaded)
			return;
		loaded = true;
		
		try {
			rms = RecordStore.openRecordStore("loader-scores", true);
			if (rms.getNumRecords() == 0)
				return;
			
			byte[] arr = rms.getRecord(1);
			ByteArrayInputStream bin = new ByteArrayInputStream(arr);
			DataInputStream in = new DataInputStream(bin);
			
			int size = in.readInt();
			scores.setSize(0);
			for (int i = 0; i < size; i++) {
				Item item = new Item();
				insert(item);
				item.name = in.readUTF();
				item.score = in.readInt();
				System.out.println("Load score: name = \"" + item.name + "\", score = " + item.score);
			}

			rms.closeRecordStore();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void insert(Item item) {
		for (int i = 0; i < scores.size(); i++) {
			Item item2 = (Item) scores.elementAt(i);
			if (item.score > item2.score) {
				scores.insertElementAt(item, i);
				return;
			}
		}
		scores.addElement(item);
	}
	
	public void addRecord(String name, int score) {
		System.out.println("Add score: name = \"" + name + "\", score = " + score);
		insert(new Item(name, score));
		
		if (scores.size() > MAX_SCORES)
			scores.setSize(MAX_SCORES);
	}
	
	public boolean isTopN(int score) {
		loadScore();
		
		if (scores.size() < MAX_SCORES)
			return true;
		
		for (int i = 0; i < scores.size(); i++) {
			Item item = (Item) scores.elementAt(i);
			if (item.score < score)
				return true;
		}

		return false;
	}
	
	public void paint(Graphics g, Font normalFont, Font boldFont, int pos1, int pos2, int x1, int y1) {
		g.setColor(~0);
		g.setFont(boldFont);
		g.drawString("Name", x1 + pos1, y1 + 5, Graphics.TOP | Graphics.LEFT);
		g.drawString("Score", x1 + pos2, y1 + 5, Graphics.TOP | Graphics.RIGHT);
		
		g.setFont(normalFont);
		g.setColor(0xbafeff);
		int y = 5 + boldFont.getHeight();
		for (int i = 0; i < scores.size(); i++) {
			Item item = (Item) scores.elementAt(i);
			
			g.drawString(item.name, x1 + pos1, y1 + y, Graphics.TOP | Graphics.LEFT);
			g.drawString(String.valueOf(item.score), x1 + pos2, y1 + y, Graphics.TOP | Graphics.RIGHT);

			y += normalFont.getHeight();
		}
	}
}
