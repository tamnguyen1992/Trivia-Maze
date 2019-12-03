package triviaMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameObject{
	private static final long serialVersionUID = 1L;
	private transient Handler handler;
	private transient GameManager gameManager;
	private int xFirstRoom; // Initialize location x of first room to check moving bounds
	private int yFirstRoom; // Initialize location y of first room to check moving bounds
	private int row;
	private int col;
	public Player(int x, int y, ID id, int xFirstRoom, int yFirstRoom, int row, int col, Handler handler, GameManager gameManager) {
		super(x, y, id);
		this.handler = handler;
		this.gameManager = gameManager;
		this.xFirstRoom = xFirstRoom;
		this.yFirstRoom = yFirstRoom;
		this.row = row;
		this.col = col;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		//x += velX;
		//y += velY;

		//clamp player to stay in our screen
		//x = GameManager.clamp(x, 0, GameManager.WIDTH - 48);
		//y = GameManager.clamp(y, 0, GameManager.HEIGHT - 72);
		collisionCheck();
	}
	private void collisionCheck() {
		for (GameObject gameObject : handler.getGameObjects()) {
			// check intersect of player and door
			if(gameObject.getID() == ID.DoorVertical || gameObject.getID() == ID.DoorHorizontal) {
				if(getBounds().intersects(gameObject.getBounds()) && (gameObject.getDoorStatus() == DoorStatus.Init || gameObject.getDoorStatus() == DoorStatus.Locked)) {
					// collision code in here
					// System.out.println("Hit the door!");
					gameManager.setSelectedObject(gameObject);
					gameManager.setWindowState(WindowState.QuestionWindow);
				} else if(getBounds().intersects(gameObject.getBounds()) && gameObject.getDoorStatus() == DoorStatus.Passed) {
					moveForward();
				}
			}
			else if(gameObject.getID() == ID.Target) {
				if(getBounds().intersects(gameObject.getBounds())){
					gameManager.setSelectedObject(gameObject);
					gameManager.setWindowState(WindowState.QuestionWindow);
				}
			}
		}
//		for (GameObject gameObject : handler.gameObjects) {
//			if(gameObject.getID() == ID.Player && gameManager.getWindowState() == WindowState.GameWindow) {
//				//System.out.println(key);
//				if(key == KeyEvent.VK_UP && gameObject.getY() > YPLAYER) { gameObject.setY(gameObject.getY() - DISTROOM); System.out.println(key);}				
//				if(key == KeyEvent.VK_DOWN && gameObject.getY() < YPLAYER + DISTROOM * 4 ) gameObject.setY(gameObject.getY() + DISTROOM);
//				if(key == KeyEvent.VK_RIGHT && gameObject.getX() < XPLAYER + DISTROOM * 4) gameObject.setX(gameObject.getX() + DISTROOM);
//				if(key == KeyEvent.VK_LEFT && gameObject.getX() > XPLAYER) gameObject.setX(gameObject.getX() - DISTROOM);
//			}
//		}
	}
	public void moveUp() {
		if(this.getY() > yFirstRoom)
			this.setY(this.getY() - gameManager.getDistRoom());
	}
	public void moveDown() {
		if(this.getY() < yFirstRoom + gameManager.getDistRoom() *2 * (row-1))
			this.setY(this.getY() + gameManager.getDistRoom());
	}
	public void moveRight() {
		if(this.getX() < xFirstRoom + gameManager.getDistRoom() *2 * (col-1))
			this.setX(this.getX() + gameManager.getDistRoom());
	}
	public void moveLeft() {
		if(this.getX() > xFirstRoom)
			this.setX(this.getX() - gameManager.getDistRoom());
	}
	public void moveForward() {
		switch(gameManager.getDirection()) {
		case Down:
			moveDown();
			break;
		case Up:
			moveUp();
			break;
		case Right:
			moveRight();
			break;
		case Left:
			moveLeft();
			break;
		default:
			break;
		}
	}
	public void moveBackward() {
		switch(gameManager.getDirection()) {
		case Down:
			moveUp();
			break;
		case Up:
			moveDown();
			break;
		case Right:
			moveLeft();
			break;
		case Left:
			moveRight();
			break;
		default:
			break;
		}
	}
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.black);
		g.fillRect(x, y, 32, 32);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(x,y,32,32);
	}

}
