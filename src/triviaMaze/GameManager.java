package TrivialMaze;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class GameManager extends Canvas implements Runnable {

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
		new Window(WIDTH, HEIGHT, "Trivial Maze", this);
		
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
//		int i = 0;
//		// loop until get a question which is never used
//		while(i<7) {
//			Question q = new Question();
//			System.out.println(q.toString());
//			i++;
//		}
	}
}
