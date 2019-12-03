package triviaMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Door extends GameObject{
	private static final long serialVersionUID = 1L;
	private Question question;
	private DoorStatus doorStatus;
	public Door(int x, int y, ID id, Question question, DoorStatus doorStatus) {
		super(x, y, id);
		this.question = question;
		this.doorStatus = doorStatus;
	}
	public void setDoorStatus(DoorStatus ds) {
		this.doorStatus = ds;
	}
	public DoorStatus getDoorStatus() {
		return this.doorStatus;
	}
	public Question getQuestion() {
		return this.question;
	}
	public String toString() {
		return question.toString();
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		if(doorStatus == DoorStatus.Init) {
			if (this.id == ID.DoorVertical) {
				g.setColor(Color.orange);
				g.fillRect(x, y, 8, 40);
			}else if (this.id == ID.DoorHorizontal) {
				g.setColor(Color.orange);
				g.fillRect(x, y, 40, 8);
			}
		} else if (doorStatus == DoorStatus.Passed) {
			if (this.id == ID.DoorVertical) {
				g.setColor(Color.white);
				g.fillRect(x, y, 8, 40);
			}else if (this.id == ID.DoorHorizontal) {
				g.setColor(Color.white);
				g.fillRect(x, y, 40, 8);
			}
		}else if (doorStatus == DoorStatus.Locked) {
			if (this.id == ID.DoorVertical) {
				g.setColor(Color.black);
				g.fillRect(x, y, 8, 40);
			}else if (this.id == ID.DoorHorizontal) {
				g.setColor(Color.black);
				g.fillRect(x, y, 40, 8);
			}
		}
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		if (this.id == ID.DoorVertical) {
			return new Rectangle(x, y, 8, 40);
		}else if (this.id == ID.DoorHorizontal) {
			return new Rectangle(x, y, 40, 8);
		}else
			return null;
	}

}
