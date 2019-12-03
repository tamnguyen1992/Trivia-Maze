package triviaMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Room extends GameObject{
	private static final long serialVersionUID = 1L;

	public Room(int x, int y, ID id) {
		super(x, y, id);		
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.black);
		g.fillRect(x, y, 68, 68);
		g.setColor(Color.white);
		g.fillRect(x+8, y+8, 52, 52);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
