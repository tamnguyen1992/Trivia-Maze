package triviaMaze;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class GameManager extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 640, HEIGHT = 480;  // Center: WIDTH/2 -32, HEIGHT/2 -32
//	private static final int XPLAYER = 228; // Initialize location x of player
//	private static final int YPLAYER = 148; // Initialize location y of player
	private static final int ROOMDIST = 30; // Distance from center player to the door/wall
	private static final int BORDERDIST = 18; // Distance from border player to the door/wall
	private static int x = 190, y = 90; // Initialize location of first room.
	private static int row = 0, col = 0; // Initialize size of maze
	
	private Direction direction; 	// direction of movement
	
	private Thread threadGame;
	private boolean running = false;
	private static boolean paused = false;
	
	private Handler handler;
	private Health health;
	private QuestionHandler questionHandler;
	private QuestionWindow questionWD;
	private GameObject selectedObject = null;
	private WindowState windowState = WindowState.GameWindow; //public for player can change the window state when hit the door
	
	public GameManager() {
		handler = new Handler();
		questionHandler = QuestionHandler.getInstance();
		questionWD = new QuestionWindow(handler,this);
		health = new Health(10, 10, ID.Health, 100, 255); 
		this.addMouseListener(questionWD);
		this.addKeyListener(new KeyInput(handler, this));
		new Window(WIDTH, HEIGHT, "Trivia Maze",handler, this);
		
		if(windowState == WindowState.GameWindow) {
			newGame("easy");
		}
	}
	

	/*  the game loop
	 *  it checks whether enough time has passed to refresh the game, and checks whether enough time has passed (1 sec) to refresh the FPS counter;
	 *  while 'running' it adds the time it took to go through one iteration of the loop it self 
	 *  and adds it to delta (which is simplified to 1)
	 *  so once it reaches 1 delta it means enough time has passed to go forward one tick.
	 */
	@Override
	public void run()
    {
		this.requestFocus(); //focus on this game window when start the application
        long lastTime = System.nanoTime(); 		// get current time to the nanosecond
        double amountOfTicks = 60.0;			// set the number of ticks
        double ns = 1000000000 / amountOfTicks; // this determines how many times we can divide 60 into 1e9 of nanoseconds or about 1 second
        double delta = 0;						
        long timer = System.currentTimeMillis();// get current time
        int frames = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            // whenever delta >= 1, we call tick()
            while(delta >=1)
            {
                tick();
                delta--;
            }
        	//if game's running, call render()
            if(running)
                render();
            
            frames++;
            if(System.currentTimeMillis() - timer > 1000) // if one second has passed
            {
                timer += 1000;
                //System.out.println("FPS: "+ frames); // print out how many frames have happened in the last second
                frames = 0;
            }
        }
        stop();
    }
	
	private void tick() {
		if(windowState == WindowState.GameWindow) {
			if(!paused) {
				handler.tick();
				health.tick();
//				if(health.getHealth <= 0) {
//					health.setHealth = 100;
//					clearObject();
//					windowState = WindowState.End;
//				}
			}
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		if(windowState == WindowState.GameWindow) {
			handler.render(g);
			health.render(g);
			if(paused) {
				g.setColor(Color.red);
				g.setFont(new Font("airal", 1,30));
				g.drawString("PAUSED! Press P to play", 100, 200);
			}
		} else if (windowState == WindowState.QuestionWindow) {
			questionWD.render(g);
		}
		
		g.dispose();
		bs.show();
	}

	public synchronized void start() {
		threadGame = new Thread(this,"TrivialMaze");
		threadGame.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			threadGame.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void saveGame(String filename) {
		if(filename == null)
			return;
		// Serialization  
        try
        {    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
            
            // Save all gameobjects to file
            out.writeObject(x);
            out.writeObject(y);
            out.writeObject(row);
            out.writeObject(col);
            out.writeObject(handler);
            
            out.close(); 
            file.close(); 
            //System.out.println("Object has been serialized"); 
        } 
          
        catch(IOException ex) 
        { 
        	JOptionPane.showMessageDialog(null,"Cannot save to this file");
            System.out.println("IOException is caught: "+ ex); 
        } 
	}
	public LinkedList<GameObject> loadGame(String filename) {
		if(filename == null)
			return null;
		// Deserialization 
		LinkedList<GameObject> temp = new LinkedList<GameObject>();
        try
        {   
            // Reading the object from a file 
            FileInputStream file = new FileInputStream(filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
            
            // Method for deserialization of object 
            x = (int)in.readObject();
            y = (int)in.readObject();
            row = (int)in.readObject();
            col = (int)in.readObject();
            Handler handler = (Handler)in.readObject();
            temp = handler.getGameObjects();

            for(int i = 0; i < temp.size(); i++) {
            	GameObject gameobject = temp.get(i);
            	//Remove the old Player object and add new Player object with the old Player info
            	if(gameobject.getID() == ID.Player) {
            		Player player = new Player(gameobject.getX(),gameobject.getY(), ID.Player,x + BORDERDIST, y + BORDERDIST,row,col, this.handler, this);
		        	temp.remove(i);
		        	temp.add(i, player);
            	}
            }
            in.close(); 
            file.close(); 
            //System.out.println(handler.getGameObjects().size());
            //System.out.println("Object has been deserialized "); 
        } 
          
        catch(IOException ex) 
        { 
        	JOptionPane.showMessageDialog(null,"Cannot load this file");
            System.out.println("IOException is caught: " + ex); 
        } 
          
        catch(ClassNotFoundException ex) 
        { 
        	JOptionPane.showMessageDialog(null,"Cannot load this file");
            System.out.println("ClassNotFoundException is caught"); 
        }
        return temp;
	}

	public void loadGame(LinkedList<GameObject> gameObjects) {
		//Clear game objects before add new game objects from save file
		clearObject();
		for(int i = 0; i < gameObjects.size(); i++) {
			GameObject temp = gameObjects.get(i);
			handler.addObject(temp);
		}
	}
	public void newGame(String level) {
		if(level.equals("easy")) {
			SimpleMaze simpleMaze = new SimpleMaze();
			Maze maze = new Maze(simpleMaze);
			row = 4;
			col = 4;
			x = 190;
			y = 90;
			maze.buildMaze(row, col, x, y, ROOMDIST,BORDERDIST, handler, this);
		}else if(level.equals("hard")) {
			ComplexMaze complexMaze = new ComplexMaze();
			Maze maze = new Maze(complexMaze);
			x = 10;
			y = 20;
			col = 10;
			row = 6;
			maze.buildMaze(row, col, x, y, ROOMDIST, BORDERDIST, handler, this);
		}
	}
	public void clearObject() {
		selectedObject = null;
		windowState = WindowState.GameWindow;
		handler.removeAllObject();
		questionHandler.resetQuestion();
	}
	public void pauseGame() {
		if(paused)
			paused = false;
		else
			paused = true;
	}
	public boolean getPaused() {
		return this.paused;
	}
	public WindowState getWindowState() {
		return this.windowState;
	}
	public void setWindowState(WindowState ws) {
		this.windowState = ws;
	}
	public int getDistRoom() {
		return ROOMDIST;
	}
	public void setDirection(Direction d) {
		this.direction = d;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setSelectedObject(GameObject object) {
		this.selectedObject = object;
	}
	public GameObject getSelectedObject() {
		return selectedObject;
	}
}
