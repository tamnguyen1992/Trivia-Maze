package triviaMaze;

import java.awt.Canvas;
import java.awt.Color;
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

public class GameManager extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 640, HEIGHT = 480;  // Center: WIDTH/2 -32, HEIGHT/2 -32
//	private static final int XPLAYER = 228; // Initialize location x of player
//	private static final int YPLAYER = 148; // Initialize location y of player
	private static final int ROOMDIST = 30; // Distance from center player to the door/wall
	private static final int BORDERDIST = 18; // Distance from border player to the door/wall
	private static int x = 210, y = 130; // Initialize location of first room.
	
	private Direction direction; 	// direction of movement
	
	private Thread threadGame;
	private boolean running = false;
	
	private Handler handler;
	private QuestionHandler questionHandler;
	private QuestionWindow questionWD;
	private GameObject selectedObject = null;
	private WindowState windowState = WindowState.GameWindow; //public for player can change the window state when hit the door
	
	public GameManager() {
		handler = new Handler();
		questionHandler = QuestionHandler.getInstance();
		questionWD = new QuestionWindow(handler,this);
		this.addMouseListener(questionWD);
		this.addKeyListener(new KeyInput(handler, this));
		new Window(WIDTH, HEIGHT, "Trivial Maze",handler, this);
		
		//handler.addObject(new Player(118, 118, ID.Player));
		if(windowState == WindowState.GameWindow) {
			newGame();
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
			handler.tick();
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
	public void saveGame() {
		// Serialization  
        try
        {    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream("save.txt"); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
            
            // Save all gameobjects to file
            out.writeObject(handler);
//            out.writeObject(handler.getGameObjects().size()); //store size of the linkedlist at the top of file
//            System.out.println(handler.getGameObjects().size());
//            for (GameObject gameObject : handler.getGameObjects()) {
//            	// Method for serialization of object 
//                out.writeObject(gameObject);
//    		}
            
            out.close(); 
            file.close(); 
              
            System.out.println("Object has been serialized"); 
  
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught: "+ ex); 
        } 
	}
	public LinkedList<GameObject> loadGame() {
		//clearObject();
		// Deserialization 
		LinkedList<GameObject> temp = new LinkedList<GameObject>();
        try
        {   
        	
            // Reading the object from a file 
            FileInputStream file = new FileInputStream("save.txt"); 
            ObjectInputStream in = new ObjectInputStream(file); 

            Handler handler = (Handler)in.readObject();
            temp = handler.getGameObjects();
            // Method for deserialization of object 
            //object1 = (Demo)in.readObject(); 
//            int size = 0;
//            size = (int)in.readObject();
//            
            for(int i = 0; i < temp.size(); i++) {
            	GameObject gameobject = temp.get(i);
            	if(gameobject.getID() == ID.Player) {
            		Player player = new Player(gameobject.getX(),gameobject.getY(), ID.Player,x + BORDERDIST, y + BORDERDIST,3,3, this.handler, this);
		        	temp.remove(i);
		        	temp.add(i, player);
            	}
            }
            in.close(); 
            file.close(); 
            System.out.println(handler.getGameObjects().size());
            System.out.println("Object has been deserialized "); 
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught: " + ex); 
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
        }
        return temp;
	}
	public LinkedList<GameObject> saveGameObject(){
		LinkedList<GameObject> saveGameObjects = new LinkedList<GameObject>();
		for (GameObject gameObject : handler.getGameObjects()) {
			saveGameObjects.add(gameObject);
		}
		return saveGameObjects;
	}
	public String saveAs() {
		int row;
		int col;
		int playerX = x;
		int playerY = y;
		LinkedList<Door> doors = new LinkedList<Door>();
		String ret = "";
		//simpleMaze
		row = 3;
		col = 3;
		for (GameObject gameObject : handler.getGameObjects()) {
			if(gameObject.getID() == ID.Player){
				playerX = gameObject.getX();
				playerY = gameObject.getY();
			}
			if(gameObject.getID() == ID.DoorHorizontal || gameObject.getID() == ID.DoorVertical) {
				doors.add((Door) gameObject);
			}
		}
		ret += row +", " + col +", " + playerX +", " + playerY + ", "+ doors.size() +". "+ doors.toString();
		return ret;
	}
	public LinkedList<Door> saveAsDoors(){
		LinkedList<Door> doors = new LinkedList<Door>();
		for (GameObject gameObject : handler.getGameObjects()) {
			if(gameObject.getID() == ID.DoorHorizontal || gameObject.getID() == ID.DoorVertical) {
				doors.add((Door) gameObject);
			}
		}
		return doors;
	}
	
	public static Connection connectionDB() throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:QuestionForTest.db");
			//System.out.println("Connect database successfully");
			return conn;
	   }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	   }
		return null;
	}
	public static void insertGaneObject(String name, int x, int y, String doorStatus, int questionID) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectionDB();
			stmt = c.createStatement();
			String sql = "INSERT INTO GameObject(Name,X,Y,DoorStatus,QuestionID)"+
							"VALUES ('"+name+"','"+x+"','"+y+"','"+doorStatus+"','"+questionID+"')";
			stmt.executeUpdate(sql);
			//System.out.println("Inserted records into the table...");
			stmt.close();
		    c.close();
	   }catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
	   }
	}
	public static String selectData() {
		Connection c = null;
		Statement stmt = null;
		String result = "";
		try {
			c = connectionDB();
			stmt = c.createStatement();
			String sql = "SELECT * FROM Question";
			ResultSet rs = stmt.executeQuery(sql);

		      // Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         int id  = rs.getInt("QuestionID");
		         String question = rs.getString("Question");
		         String correctAns = rs.getString("CorrectAnswer");
		         String wrongAns1 = rs.getString("WrongAnswer1");
		         String wrongAns2 = rs.getString("WrongAnswer2");
		         String wrongAns3 = rs.getString("WrongAnswer3");
		         int type = rs.getInt("TypeOfQuestion");
		         result += "QuestionID: " + id + ", Question: " + question + ", Correct Answer: " + correctAns +
		        		 	", Wrong Answer 1: " + wrongAns1 + ", Wrong Answer 2: " + wrongAns2 + ", Wrong Answer 3: " + wrongAns3 +
		        		 	", Type Of Question: " + type +"\n";
		      }
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		return result;
	}	
	public void saveAsGameObject(){
		String name = "";
		int x = 0;
		int y = 0;
		String doorStatus = "";
		int questionID = 0;
		//LinkedList<GameObject> saveGameObjects = new LinkedList<GameObject>();
		for (GameObject gameObject : handler.getGameObjects()) {
			if(gameObject.getID() == ID.DoorHorizontal || gameObject.getID() == ID.DoorVertical) {
				doorStatus = gameObject.getDoorStatus().name();
				questionID = gameObject.getQuestion().getId();
			}
			name = gameObject.getID().name();
			x = gameObject.getX();
			y = gameObject.getY();
			insertGaneObject(name, x, y, doorStatus, questionID);
			//saveGameObjects.add(gameObject);
		}
		//return saveGameObjects;
	}
	public LinkedList<GameObject> getGameObjects(Handler handler, GameManager gameManager) {
		LinkedList<GameObject> saveGameObjects = new LinkedList<GameObject>();
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectionDB();
			stmt = c.createStatement();
			String sql = "SELECT * FROM GameObject";
			ResultSet rs = stmt.executeQuery(sql);

		      // Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         int id  = rs.getInt("Id");
		         String name = rs.getString("Name");
		         int x = rs.getInt("X");
		         int y = rs.getInt("Y");
		         int questionID = rs.getInt("QuestionID");
		         
		         String strDoor = rs.getString("DoorStatus");
		         DoorStatus doorStatus = DoorStatus.Init;
		         //get door status
		         if(strDoor.equals(DoorStatus.Passed.name()))
		        	 doorStatus = DoorStatus.Passed;
		         else if(strDoor.equals(DoorStatus.Locked.name()))
		        	 doorStatus = DoorStatus.Locked;
		         
		         //create each gameobject base on their ID
		         if(name.equals(ID.Room.name())) {
		        	 saveGameObjects.add(new Room(x, y, ID.Room));
		         }
		         else if(name.equals(ID.DoorVertical.name())) {
		        	 saveGameObjects.add(new Door(x, y, ID.DoorVertical, new Question(questionID), doorStatus));
		         }
		         else if(name.equals(ID.DoorHorizontal.name())) {
		        	 saveGameObjects.add(new Door(x, y, ID.DoorHorizontal, new Question(questionID), doorStatus));
		         }
		         else if(name.equals(ID.Player.name())) {
		        	 //saveGameObjects.add(new Player(x, y, ID.Player, handler, gameManager));
		         }
		         else if(name.equals(ID.Player.name())) {
		        	 saveGameObjects.add(new Target(x, y, ID.Target));
		         }
		      }
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		return saveGameObjects;
	}
	public void loadGame(LinkedList<GameObject> gameObjects) {
		clearObject();
		for(int i = 0; i < gameObjects.size(); i++) {
			GameObject temp = gameObjects.get(i);
			handler.addObject(temp);
		}
	}
	public void newGame() {
		SimpleMaze simpleMaze = new SimpleMaze();
		simpleMaze.buildMaze(3, 3, x, y, ROOMDIST,BORDERDIST, handler, this);
	}
	public void clearObject() {
		selectedObject = null;
		windowState = WindowState.GameWindow;
		handler.removeAllObject();
		questionHandler.resetQuestion();
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameManager();
	}
}
