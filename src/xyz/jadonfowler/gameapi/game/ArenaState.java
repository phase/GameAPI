package xyz._5th.gameapi.game;

public enum ArenaState {
	
	PRE_GAME(true, "§ePre-Game"), IN_GAME(false, "§aIn Game"), POST_GAME(false, "§dPost-Game");
	
	boolean canJoin;
	String string;
	
	ArenaState(boolean canJoin, String string){
		this.canJoin = canJoin;
		this.string = string;
	}
	
	/**
	 * @return Message for signs n stuff
	 */
	public String getString(){
		return string;
	}
	/**
	 * @return if a player can join
	 */
	public boolean canJoin(){
		return canJoin;
	}
}
