package five.game.game;
/**
 * Used for custom starting/stopping the game. 
 */
public interface GameRunnable {

	/**
	 * Called when game starts.
	 */
	public void start();
	/**
	 * Called when game stops.
	 */
	public void stop();
	
	/**
	 * Called at the end of the game. Can be used for giving coins/gems to the winning team.
	 * @param team - Team that won
	 */
	public void win(Team team);
}
