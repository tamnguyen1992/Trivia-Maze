package triviaMaze;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public abstract class GameObject implements Serializable {
	private static final long serialVersionUID = 1L;
	//protected means it can only be accessed by which object inherits the GameObject 
	protected int x,y; 	// location
	protected ID id;
	
	public GameObject(int x, int y, ID id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public abstract void tick(); 				// Update 
	public abstract void render(Graphics g); 	// draw window
	public abstract Rectangle getBounds();		// handle collision (Rectangle has intersect method to handle if two rectangles intersect each other)
	public void moveUp() {}
	public void moveDown() {}
	public void moveRight() {}
	public void moveLeft() {}
	public void moveForward() {}
	public void moveBackward() {}
	public void setDoorStatus(DoorStatus ds) {}
	public DoorStatus getDoorStatus() {
		return null;
	}
	public Question getQuestion() {
		return null;
	}
	public String toString() {
		return null;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setID(ID id) {
		this.id = id;
	}
	public ID getID() {
		return id;
	}
}
