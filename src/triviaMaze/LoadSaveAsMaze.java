package triviaMaze;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class LoadSaveAsMaze implements MazeBuilder{
	public void buildMaze(int row, int col, int x, int y, int roomDist, int borderDist, Handler handler, GameManager gameManager) {
		
//		handler.removeAllObject();
//		gameManager.setSelectedObject(null);
//		gameManager.setWindowState(WindowState.GameWindow);
//		for(int i = 0; i < gameObjects.size(); i++) {
//			GameObject temp = gameObjects.get(i);
//			handler.addObject(temp);
//		}
//		buildRoom(row, col, x, y, handler);
//		buildDoor(row, col, x, y, handler, gameManager);
//		handler.addObject(new Player(x + borderDist, y + borderDist, ID.Player, handler, gameManager));
//		handler.addObject(new Target(x + borderDist + roomDist * 2 * (col -1), y + borderDist + roomDist * 2 * (row -1) , ID.Target));
	}
	
	private void buildRoom(int row, int col, int x, int y, Handler handler) {
		int temp = x;
		int horDist = 60; //distance from 2 room
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				handler.addObject(new Room(x, y, ID.Room));
				x +=horDist;
			}
			x = temp;
			y += horDist;
		}
	}
	private void buildDoor(int row, int col, int x, int y, Handler handler, GameManager gameManager) {
		for (Door door : gameManager.saveAsDoors()) {
			handler.addObject(door);
		}
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
		         if(doorStatus.equals(DoorStatus.Passed))
		        	 doorStatus = DoorStatus.Passed;
		         else if(doorStatus.equals(DoorStatus.Locked))
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
}
