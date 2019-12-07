package triviaMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Health extends GameObject{
	private float health;
	private float greenValue;
	public Health(int x, int y, ID id, float health, int greenValue) {
		super(x, y, id);
		this.health = health;
		this.greenValue = greenValue;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		health -= 0.1;
		health = clamp(health, 0, health);
		greenValue = clamp(greenValue, 0, greenValue);
		greenValue = health*2;
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.gray);
		g.fillRect(x, y, 200, 32);
		
		//actual health
		//g.setColor(Color.green);
		g.setColor(new Color(75, (int)greenValue, 0));
		g.fillRect(x, y, (int) (health*2), 32);
		
		//border
		g.setColor(Color.white);
		g.drawRect(x, y, 200, 32);
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	private float clamp(float var, float min, float max) {
		if(var <= min)
			return var = min;
		else if (var >= max)
			return var = max;
		else
			return var;
	}
}
