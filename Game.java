// Gurkirn K
// This is a game where you move an otter up and down in order for it to eat fish
// and avoid plastic bags. The otter has 3 lives and it loses a live each time it
// collides with a plastic bag. The game also increases in speed as the score increases.
// There are also bonus points for catching/eating consecutive fish.

public class Game {
	
	private Grid grid;
	private int userRow;
	private int msElapsed;
	private int timesGet;
	private int timesAvoid;
	private int lives;
	private int gameSpeed;

	public Game() {
		grid = new Grid(10, 20);
		
		for (int row = 0; row < grid.getNumRows(); row++) {
			int blueShade = 150 + (row * 15);
			for (int col = 0; col < grid.getNumCols(); col++) {
				grid.setColor(new Location(row, col), new Color(0, 0, Math.min(225, blueShade)));
			}
		}
		
		userRow = 0;
		msElapsed = 0;
		timesGet = 0;
		timesAvoid = 0;
		lives = 3;
		gameSpeed = 300;
		
		updateTitle();
		grid.setImage(new Location(userRow, 0), "otter.gif");
		
		showGameOverMessage();
	}

	public void play() {
		while (!isGameOver())
		{
			grid.pause(100);
			handleKeyPress();
			if (msElapsed % gameSpeed == 0)
			{
				scrollLeft();
				populateRightEdge();
			}
			updateTitle();
			msElapsed += 100;
			
			if (timesGet > 0 && timesGet % 5 == 0) {
				gameSpeed = Math.max(100, 300- (timesGet * 10));
			}
		}
		
		grid.setTitle("Game Over! Final Score: " + getScore());
		grid.pause(4000);
	}

	public void handleKeyPress() {
		int key = grid.checkLastKeyPressed();
		grid.setImage(new Location(userRow, 0), null);
		
		if (key == 38 && userRow > 0) {
			userRow--;
		} else if (key == 40 && userRow < grid.getNumRows() - 1) {
			userRow++;
		}
		
		handleCollision(new Location(userRow, 0));
		grid.setImage(new Location(userRow, 0), "otter.gif");
	}

	public void populateRightEdge() {
		int numRows = grid.getNumRows();
		int rightCol = grid.getNumCols() - 1;
		
		if (Math.random() < 0.5) {
			int fishRow = (int) (Math.random() * numRows);
			grid.setImage(new Location(fishRow, rightCol), "fish.gif");
		}
		if (Math.random() < 0.3) {
			int plasticRow = (int) (Math.random() * numRows);
			if (grid.getImage(new Location(plasticRow, rightCol)) == null) {
				grid.setImage(new Location(plasticRow, rightCol), "plastic.gif");
			}
		}
	}

	public void scrollLeft() {
		int numRows = grid.getNumRows();
		int numCols = grid.getNumCols();
		
		for (int row = 0; row < numRows; row++) {
			for (int col = 1; col < numCols; col++) {
				String image = grid.getImage(new Location(row, col));
				grid.setImage(new Location(row, col - 1), image);
			}
			grid.setImage(new Location(row, numCols - 1), null);
		}
		
		handleCollision(new Location(userRow, 0));
		grid.setImage(new Location(userRow, 0), "otter.gif");
	}

	public void handleCollision(Location loc) {
		String image = grid.getImage(loc);
		
		if (image != null) {
			if (image.equals("fish.gif")) {
				timesGet++;
				grid.setColor(loc, new Color(0, 255, 0));
				grid.pause(100);
				grid.setColor(loc, new Color(0, 0, 255));
			} else if (image.equals("plastic.gif")) {
				timesAvoid--;
				lives--;
				grid.setColor(loc, new Color(255, 0, 0));
				grid.pause(100);
				grid.setColor(loc, new Color(0, 0, 255));
				
				if (lives > 0) {
					showLifeLostMessage();
				}
			}
			grid.setImage(loc, null);
		}
	}

	public int getScore() {
		return (timesGet * 10) + (msElapsed / 100);
	}

	public void updateTitle() {
		grid.setTitle("Sea Otter Game | Score: " + getScore() + " | Lives " + lives);
	}

	private void showGameOverMessage() {
		for (int row = 0; row < grid.getNumRows(); row ++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				grid.setImage(new Location(row, col), null);
			}
		}
		
		String message = "GAME OVER!\nFinal Score: " + getScore();
		grid.showMessageDialog(message);
	}
	
	private void showLifeLostMessage() {
		String message = "Oh No! Otter ate plastic!\nLives Remaining: " + lives;
		grid.showMessageDialog(message);
		grid.pause(500);
	}
	
	public boolean isGameOver() {
		return lives <= 0;
	}

	public static void test() {
		Game game = new Game();
		game.play();
	}

	public static void main(String[] args) {
		test();
	}
}