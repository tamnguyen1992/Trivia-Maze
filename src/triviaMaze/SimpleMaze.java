package triviaMaze;

public class SimpleMaze implements MazeBuilder{

	@Override
	public void buildMaze(int row, int col, int x, int y, int roomDist, int borderDist, Handler handler, GameManager gameManager) {
		buildRoom(row, col, x, y, handler);
		buildDoor(row, col, x, y, handler);
		handler.addObject(new Player(x + borderDist, y + borderDist, ID.Player,x + borderDist, y + borderDist,row,col, handler, gameManager));
		handler.addObject(new Target(x + borderDist + roomDist * 2 * (col -1), y + borderDist + roomDist * 2 * (row -1) , ID.Target));
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
	private void buildDoor(int row, int col, int x, int y, Handler handler) {
		int temp = x;
		int horDist = 60; //distance from 2 room
		int verDist = 14; //distance from top room to top door
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(j < col-1 )
					handler.addObject(new Door(x+horDist, y+verDist, ID.DoorVertical, new Question(), DoorStatus.Init));
				if (i < row-1)
					handler.addObject(new Door(x+verDist, y+horDist, ID.DoorHorizontal, new Question(), DoorStatus.Init));
				x +=horDist;
			}
			x = temp;
			y += horDist;
		}
	}
}
