package TrivialMaze;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	private Handler handler;
	private GameManager gameManager;
	//private int XPLAYER = 228; // Initialize location x of player
	//private int YPLAYER = 148; // Initialize location y of player
	//private int DISTROOM = 30; // Distance of 2 room
	public KeyInput(Handler handler, GameManager gameManager) {
		this.handler = handler;
		this.gameManager = gameManager;
	}
	public void keyPressed(KeyEvent e) {
	}
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		for (GameObject gameObject : handler.gameObjects) {
			if(gameObject.getID() == ID.Player && gameManager.getWindowState() == WindowState.GameWindow) {
				//System.out.println(key);
				if(key == KeyEvent.VK_UP) { 
					gameObject.moveUp(); 
					gameManager.setDirection(Direction.Up);
				}
				if(key == KeyEvent.VK_DOWN) {
					gameObject.moveDown();
					gameManager.setDirection(Direction.Down);
				}
				if(key == KeyEvent.VK_RIGHT) {
					gameObject.moveRight();
					gameManager.setDirection(Direction.Right);
				}
				if(key == KeyEvent.VK_LEFT) {
					gameObject.moveLeft();
					gameManager.setDirection(Direction.Left);
				}
				//if(key == KeyEvent.VK_UP && gameObject.getY() > YPLAYER) { gameObject.setY(gameObject.getY() - DISTROOM); System.out.println(key);}				
				//if(key == KeyEvent.VK_DOWN && gameObject.getY() < YPLAYER + DISTROOM * 4 ) gameObject.setY(gameObject.getY() + DISTROOM);
				//if(key == KeyEvent.VK_RIGHT && gameObject.getX() < XPLAYER + DISTROOM * 4) gameObject.setX(gameObject.getX() + DISTROOM);
				//if(key == KeyEvent.VK_LEFT && gameObject.getX() > XPLAYER) gameObject.setX(gameObject.getX() - DISTROOM);
			}
		}

		if(key == KeyEvent.VK_ESCAPE)
			System.exit(1);
		
	}
}
