package ikm;

import ikm.scene.Scene;
import ikm.state.PlayState;

public abstract class GameLevel {
	public abstract void initialize(Scene scene, PlayState state, int width, int height);
	public abstract void update(int xPos, int yPos);
}
