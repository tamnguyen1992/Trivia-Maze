package TrivialMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Target extends GameObject{

	public Target(int x, int y, ID id) {
		super(x, y, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.red);
		g.fillOval(x+6, y+6, 20, 20);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(x +6, y+6, 20, 20);
	}

}
