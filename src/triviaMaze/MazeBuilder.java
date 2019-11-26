package TrivialMaze;

public interface MazeBuilder {
	public void buildMaze(int row, int col, int x, int y, int roomDist, int borderDist, Handler handler, GameManager gameManager);

}
