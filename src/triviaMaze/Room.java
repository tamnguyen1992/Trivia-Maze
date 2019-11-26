package TrivialMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Room extends GameObject{
	private Handler handler;
	public Room(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		// TODO Auto-generated constructor stub
		this.handler = handler;
		
	}
//	private void addDoors() {
//		handler.addObject(new Door(x+60, y+14, ID.DoorVertical));
//		handler.addObject(new Door(x+14, y+60, ID.DoorHorizontal));
//	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		//addDoors();
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.black);
		g.fillRect(x, y, 68, 68);
		g.setColor(Color.white);
		g.fillRect(x+8, y+8, 52, 52);
		

		//border
//		g.setColor(Color.black);
//		g.drawRect(100, 100, 62, 62);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
