package triviaMaze;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

import admin.Login;


public class Window extends Canvas {

	private static final long serialVersionUID = 1L;
	//private Action leftAction, middleAction, rightAction;
	private GameManager gameManager;
	private Handler handler;
	public Window (int width, int height, String title, Handler handler, GameManager gameManager) {
		this.handler = handler;
		this.gameManager = gameManager;
		JFrame frame = new JFrame(title);
		Dimension dimension = new Dimension(width,height);
		frame.setPreferredSize(dimension);
		frame.setMinimumSize(dimension);
		frame.setMaximumSize(dimension);
		
		//Create the actions shared by the toolbar and menu.
//        leftAction =   new NewGame(  "Go left",
//                                        "This is the left button.", 
//                                        new Integer(KeyEvent.VK_L));
//        middleAction = new MiddleAction("Do something",
//                                        "This is the middle button.", 
//                                        new Integer(KeyEvent.VK_M));
//        rightAction =  new RightAction( "Go right",
//                                        "This is the right button.", 
//                                        new Integer(KeyEvent.VK_R));
        
		
        
        frame.getContentPane().add(BorderLayout.NORTH, createMenuBar());
        //frame.getContentPane().add(BorderLayout.NORTH, mb);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	// get exit button on window
		frame.setResizable(false); 								// do not allow resize window
		frame.setLocationRelativeTo(null);						// to place window in the center of screen
		frame.add(gameManager);
		frame.setVisible(true);
		gameManager.start();
		
	}
	public JMenuBar createMenuBar() {
		//Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Admin");
        JMenu m3 = new JMenu("About");
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        
        JMenuItem m11 = new JMenu("New Game");
        JMenuItem m111 = new JMenuItem(new Easy("Easy"));
        m111.setMnemonic(KeyEvent.VK_E);
        JMenuItem m112 = new JMenuItem(new Hard("Hard"));
        m112.setMnemonic(KeyEvent.VK_H);
        m11.add(m111);
        m11.add(m112);
        
        JMenuItem m12 = new JMenuItem(new SaveAs("Save Game"));
        m12.setMnemonic(KeyEvent.VK_S);
        JMenuItem m13 = new JMenuItem(new LoadGame("Load Game"));
        m13.setMnemonic(KeyEvent.VK_L);
        JMenuItem m14 = new JMenuItem(new Exit("Exit"));
        m14.setMnemonic(KeyEvent.VK_X);
        m1.add(m11);
        m1.add(m12);
        m1.add(m13);
        m1.add(m14);

        JMenuItem m21 = new JMenuItem(new AddQuestion("Add Question"));
        m2.add(m21);
        
        JMenuItem m31 = new JMenuItem(new Help("Help"));
        m3.add(m31);
        return mb;
    }
	public class Easy extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public Easy(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	gameManager.clearObject();
            gameManager.newGame("easy");
        }
    }
	public class Hard extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public Hard(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	gameManager.clearObject();
        	gameManager.newGame("hard");
        }
    }
    public class AddQuestion extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public AddQuestion(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	System.out.println("login");
        	Login.main(null);
        }
    }

    public class SaveAs extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SaveAs(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	int response = fc.showSaveDialog(null);
        	if(response == JFileChooser.APPROVE_OPTION) {
        		String filename = fc.getSelectedFile().toString();
        		gameManager.saveGame(filename);
        	}
        	
        }
    }
    public class LoadGame extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public LoadGame(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	int response = fc.showOpenDialog(null);
        	if(response == JFileChooser.APPROVE_OPTION) {
        		String filename = fc.getSelectedFile().toString();
        		LinkedList<GameObject> gameObjects = new LinkedList<GameObject>();
            	gameObjects = gameManager.loadGame(filename);
            	gameManager.loadGame(gameObjects);
        	}
        }
    }
    public class Exit extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public Exit(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	System.exit(1);
        }
    }
    public class Help extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public Help(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
        	String about = "Trivia Maze ver 1.0" +"\n\n" + 
        					"Player must navigate through from entrance to exit to win the game." +"\n" +
        					"In order to pass through a door, player need to have an correct answer."+"\n" +
        					"If a user is unable to answer a question, that door is then locked permanently."+"\n\n" +
        					"Use Up-Down-Left-Right keyboard to control Player.";
        	JOptionPane.showMessageDialog(null, about);
        }
    }
}
