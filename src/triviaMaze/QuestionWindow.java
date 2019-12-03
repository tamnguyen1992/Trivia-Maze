package triviaMaze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Random;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Rectangle;

public class QuestionWindow extends MouseAdapter implements Serializable{
	private static final long serialVersionUID = 1L;
	private GameManager gameManager;
	private Handler handler;
	// To store temporary answer from render
	private String correctAnswer = "";
	private String tempAnswer1 = "";
	private String tempAnswer2 = "";
	private String tempAnswer3 = "";
	private String tempAnswer4 = "";
	public QuestionWindow(Handler handler, GameManager gameManager) {
		this.handler = handler;
		this.gameManager = gameManager;
	}
	public void mousePressed(MouseEvent e) {
		if(gameManager.getWindowState() == WindowState.QuestionWindow) {
			int xmouse = e.getX(); //get temporary mouse click position
			int ymouse = e.getY();
			// Question window handle
			if (gameManager.getSelectedObject().getDoorStatus() == DoorStatus.Init) {
				Question question = gameManager.getSelectedObject().getQuestion();
				correctAnswer = question.getCorrectAnswer();
				if (question.getType() == TypeOfQuestion.TrueFalse) {
				
					// True button click handle
					if(mouseOver(xmouse, ymouse, new Rectangle(210, 216, 200, 64))) {
						handleClickAnswer(correctAnswer,tempAnswer1);
					}
					// False button click handle
					if(mouseOver(xmouse, ymouse, new Rectangle(210, 300, 200, 64))) {
						handleClickAnswer(correctAnswer,tempAnswer2);
					}
				} else if (question.getType() == TypeOfQuestion.MutipleChoice) {
					// A. button click handle
					if(mouseOver(xmouse, ymouse, new Rectangle(10, 216, 290, 64))) {
						handleClickAnswer(correctAnswer,tempAnswer1);
					}
					// B. button click handle
					if(mouseOver(xmouse, ymouse, new Rectangle(10, 300, 290, 64))) {
						handleClickAnswer(correctAnswer,tempAnswer2);
					}
					// C. button click handle
					if(mouseOver(xmouse, ymouse, new Rectangle(320, 216, 290, 64))) {
						handleClickAnswer(correctAnswer,tempAnswer3);
					}
					// D. button click handle
					if(mouseOver(xmouse, ymouse, new Rectangle(320, 300, 290, 64))) {
						handleClickAnswer(correctAnswer,tempAnswer4);
					}
					
				}else if (question.getType() == TypeOfQuestion.ShortAnswer) {
					
				}
			// Locked window handle
			} else if(gameManager.getSelectedObject().getDoorStatus() == DoorStatus.Locked) {
				// OK button click handle
				if(mouseOver(xmouse, ymouse, new Rectangle(210, 300, 200, 64))) {
					playerMoveBackward();
					gameManager.setWindowState(WindowState.GameWindow);
				}
				
			// Won window handle
			} else if (gameManager.getSelectedObject().getID() == ID.Target) {
				// OK button click handle
				if(mouseOver(xmouse, ymouse, new Rectangle(210, 300, 200, 64))) {
					//Call the new game function
					gameManager.clearObject();
					gameManager.newGame();
					//gameManager.setWindowState(WindowState.GameWindow);
				}
			}
		}
	}
	private void handleClickAnswer(String s1, String s2) {
		// Correct answer
		if(s1.toLowerCase().equals(s2.toLowerCase())) {
			playerMoveForward();
			// change status of the door to passed
			gameManager.getSelectedObject().setDoorStatus(DoorStatus.Passed);
		}
		// wrong answer
		else {
			playerMoveBackward();
			// change status of the door to passed
			gameManager.getSelectedObject().setDoorStatus(DoorStatus.Locked);
		}
		gameManager.setWindowState(WindowState.GameWindow);
	}
	private void playerMoveForward() {
		for (GameObject gameObject : handler.getGameObjects()) {
			if(gameObject.getID() == ID.Player) {
				gameObject.moveForward();
			}
		}
	}
	private void playerMoveBackward() {
		for (GameObject gameObject : handler.getGameObjects()) {
			if(gameObject.getID() == ID.Player) {
				gameObject.moveBackward();
			}
		}
	}
	// if mouse click on a rectangle, return true
	private boolean mouseOver(int xmouse, int ymouse, Rectangle rec) {
		if(xmouse > rec.x && xmouse < rec.x + rec.width) {
			if (ymouse > rec.y && ymouse < rec.y + rec.height) {
				return true;
			}
		}
		return false;
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	public void tick() {}
	/**
	 * Split question into n lines to render
	 */
	private void renderQuestion(Graphics g, String question, int n) {
		String[] temp1;
		String [] strQuestion = new String[20];
		String string = "";
		temp1 = question.split(" ");
		int counter = 0;
		int numberOfLine = 0;
		for (int i = 0; i < temp1.length; i ++) {
			if(counter == n) {
				numberOfLine++;
				string = "";
				counter = 0;
			}
			string += temp1[i] + " ";
			strQuestion[numberOfLine] = string; 
			counter++;
		}
		int x = 20; // Initialize of location of question
		int y = 50;
		for(int j = 0; j <= numberOfLine; j++) {
			//System.out.println(temp2[j]);
			Font bigFont = new Font("airal", 1,32);
			g.setFont(bigFont);
			g.setColor(Color.WHITE);
			g.drawString(strQuestion[j], x, y);
			// Increase y location for next line
			y += 50;
		}
	}

	public void render(Graphics g) {
		if(gameManager.getWindowState() == WindowState.QuestionWindow) {
			if (gameManager.getSelectedObject().getDoorStatus() == DoorStatus.Init) {
				Question question = gameManager.getSelectedObject().getQuestion();
				// render question
				String strQuestion = question.getQuestion() + "?";
				renderQuestion(g,strQuestion,6);
				
				// Render answers
				if (question.getType() == TypeOfQuestion.TrueFalse) {
					Font normalFont = new Font("airal", 1,40);
					
					// Render True button
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(210, 216, 200, 64);
					g.drawString("True", 262, 260);
					tempAnswer1 = "True";
					
					// Render False button
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(210, 300, 200, 64);
					g.drawString("False", 258, 350);
					tempAnswer2 = "False";
					
				} else if (question.getType() == TypeOfQuestion.MutipleChoice) {
					Font normalFont = new Font("airal", 1,20);
					String[] answers = new String[4];
					answers[0] = question.getAnswer1();
					answers[1] = question.getAnswer2();
					answers[2] = question.getAnswer3();
					answers[3] = question.getAnswer4();
					
					// Render A. button
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(10, 216, 290, 64);
					g.drawString("A. " + answers[0], 20, 255);
					tempAnswer1 = answers[0];
					
					// Render B. button
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(10, 300, 290, 64);
					g.drawString("B. " + answers[1], 20, 340);
					tempAnswer2 = answers[1];
					
					// Render C. button
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(320, 216, 290, 64);
					g.drawString("C. " + answers[2], 330, 255);
					tempAnswer3 = answers[2];
					
					// Render D. button
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(320, 300, 290, 64);
					g.drawString("D. " + answers[3], 330, 340);
					tempAnswer4 = answers[3];
					
				}else if (question.getType() == TypeOfQuestion.ShortAnswer) {
					Font normalFont = new Font("airal", 1,40);
					
					g.setFont(normalFont);
					g.setColor(Color.white);
					g.drawRect(210, 300, 200, 64);
					g.drawString("OK", 280, 350);
				}
				
			} else if(gameManager.getSelectedObject().getDoorStatus() == DoorStatus.Locked) {
				Font smallFont = new Font("airal", 1,32);
				Font normalFont = new Font("airal", 1,40);
				g.setFont(smallFont);
				g.setColor(Color.WHITE);
				g.drawString("This door has been locked permanently", 10, 150);
				
				g.setFont(normalFont);
				g.setColor(Color.white);
				g.drawRect(210, 300, 200, 64);
				g.drawString("OK", 280, 350);
			} else if (gameManager.getSelectedObject().getID() == ID.Target) {
				Font smallFont = new Font("airal", 1,32);
				Font normalFont = new Font("airal", 1,40);
				g.setFont(smallFont);
				g.setColor(Color.WHITE);
				g.drawString("Congratulations !!! You won the game", 20, 150);
				
				g.setFont(smallFont);
				g.setColor(Color.WHITE);
				g.drawString("Click OK to play New Game", 100, 200);
				
				g.setFont(normalFont);
				g.setColor(Color.white);
				g.drawRect(210, 300, 200, 64);
				g.drawString("OK", 280, 350);
			}
		}
	}

}
