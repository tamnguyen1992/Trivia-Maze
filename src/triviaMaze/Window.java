package TrivialMaze;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class Window extends Canvas {

	private static final long serialVersionUID = 1L;
	private Action leftAction, middleAction, rightAction;
	private GameManager gameManager;
	public Window (int width, int height, String title, GameManager gameManager) {
		this.gameManager = gameManager;
		JFrame frame = new JFrame(title);
		Dimension dimension = new Dimension(width,height);
		frame.setPreferredSize(dimension);
		frame.setMinimumSize(dimension);
		frame.setMaximumSize(dimension);
		
		//Create the actions shared by the toolbar and menu.
        leftAction =   new LeftAction(  "Go left",
                                        "This is the left button.", 
                                        new Integer(KeyEvent.VK_L));
        middleAction = new MiddleAction("Do something",
                                        "This is the middle button.", 
                                        new Integer(KeyEvent.VK_M));
        rightAction =  new RightAction( "Go right",
                                        "This is the right button.", 
                                        new Integer(KeyEvent.VK_R));
        
		//Creating the MenuBar and adding components
//        JMenuBar mb = new JMenuBar();
//        JMenu m1 = new JMenu("File");
//        JMenu m2 = new JMenu("Admin");
//        JMenu m3 = new JMenu("About");
//        mb.add(m1);
//        mb.add(m2);
//        mb.add(m3);
//        JMenuItem m11 = new JMenuItem("New Game");
//        JMenuItem m12 = new JMenuItem("Save as");
//        JMenuItem m13 = new JMenuItem("Load Game");
//        JMenuItem m14 = new JMenuItem("Exit");
//        m1.add(m11);
//        m1.add(m12);
//        m1.add(m13);
//        m1.add(m14);
//
//        JMenuItem m21 = new JMenuItem("Log In");
//        JMenuItem m22 = new JMenuItem("Add Question");
//        m2.add(m21);
//        m2.add(m22); 
        
        frame.getContentPane().add(BorderLayout.NORTH, createMenuBar());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	// get exit button on window
		frame.setResizable(false); 								// do not allow resize window
		frame.setLocationRelativeTo(null);						// to place window in the center of screen
		frame.add(gameManager);
		frame.setVisible(true);
		gameManager.start();
		
	}
	public JMenuBar createMenuBar() {
        JMenuItem menuItem = null;
        JMenuBar menuBar;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Create the first menu.
        JMenu mainMenu = new JMenu("Menu");

        Action[] actions = {leftAction, middleAction, rightAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            mainMenu.add(menuItem);
        }

        //Set up the menu bar.
        menuBar.add(mainMenu);
        //menuBar.add(createAbleMenu());
        return menuBar;
    }
	public class LeftAction extends AbstractAction {
        public LeftAction(String text,
                          String desc, Integer mnemonic) {
            super(text);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Action for first button/menu item");
            gameManager.clearObject();
            gameManager.newGame();
        }
    }

    public class MiddleAction extends AbstractAction {
        public MiddleAction(String text,
                            String desc, Integer mnemonic) {
            super(text);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
        	System.out.println("Action for second button/menu item");
        }
    }

    public class RightAction extends AbstractAction {
        public RightAction(String text,
                           String desc, Integer mnemonic) {
            super(text);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
        	System.out.println("Action for thirst button/menu item");
        }
    }
}
