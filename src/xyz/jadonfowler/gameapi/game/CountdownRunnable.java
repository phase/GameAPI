package xyz.jadonfowler.gameapi.game;

import xyz.jadonfowler.gameapi.message.Prefix;

class CountdownRunnable implements Runnable {

	Arena arena;
	int time;
	/**
	 * 
	 * @param arena - Arena to send message to
	 * @param x - Time till game ends
	 */
	protected CountdownRunnable(Arena arena, int x) {
		this.arena = arena;
		this.time = x;
	}

	/**
	 * Main runnable
	 */
	public void run() {
		if(this.arena.getState() == ArenaState.IN_GAME)
			this.arena.broadcastMessage(Prefix.TIME, time+":00 minutes remain!");
	}

}
