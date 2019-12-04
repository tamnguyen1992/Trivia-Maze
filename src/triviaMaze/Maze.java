package triviaMaze;

public class Maze {
	private MazeBuilder mazeBuilder;
	public Maze(MazeBuilder mazeBuilder) {
		this.mazeBuilder = mazeBuilder;
	}
	public void buildMaze(int row, int col, int x, int y, int roomDist, int borderDist, Handler handler, GameManager gameManager) {
		mazeBuilder.buildMaze(row, col, x, y, roomDist, borderDist, handler, gameManager);
	}
}
