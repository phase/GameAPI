package xyz._5th.gameapi.game;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import xyz._5th.gameapi.main.GameAPI;

public class Game {

	public static ArrayList<Game> GameList = new ArrayList<Game>();
	ArrayList<Listener> Listeners;
	String name;
	String desc;
	ArrayList<String> preGameNotes;
	ArrayList<Arena> arenas;
	boolean useTime;
	int time;

	/**
	 * Creates a game.
	 * 
	 * @param name - Name of Game
	 * @param desc - Description of Game
	 * @param preGameNotes - Notes before Game starts
	 * @param arenas - Arenas for Game
	 * @param listeners - Listeners before Game
	 */
	public Game(String name, String desc, ArrayList<String> preGameNotes,
			ArrayList<Arena> arenas, ArrayList<Listener> listeners) {
		this.name = name;
		this.desc = desc;
		this.preGameNotes = preGameNotes;
		this.arenas = arenas;
		this.Listeners = listeners;
		this.useTime = true;
		this.time = 10;
		if (listeners != null)
			for (Listener l : listeners)
				Bukkit.getPluginManager().registerEvents(l,
						GameAPI.getInstance());
		GameList.add(this);
	}

	/**
	 * Starts Arena from the Game instance
	 * @param a - Arena
	 */
	public void startGame(Arena a) {
		a.start();
	}

	/**
	 * Gets name of Game
	 * @return name of Game
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets decription of Game
	 * @return description of Game
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Stops Arena from the Game instace
	 * @param a - Arena
	 */
	public void stopGame(Arena a) {
		a.stop();
	}
	
	/**
	 * Gets Game by String
	 * @param s - Name of Game
	 * @return Game
	 */
	public static Game getGame(String s) {
		for (Game g : GameList)
			if (g.getName().equalsIgnoreCase(s))
				return g;
		return null;
	}

	/**
	 * Checks if Game exist from a String
	 * @param s
	 * @return if Game exist
	 */
	public static boolean gameExist(String s) {
		return getGame(s) != null;
	}

	/**
	 * Checks if Player is in a game
	 * @param p - Player
	 * @return if Player is in a Game
	 */
	public static boolean isInGame(Player p) {
		for (Game g : GameList)
			for (Arena a : g.arenas)
				if (a.getPlayers().contains(p.getUniqueId()))
					return true;
		return false;
	}

	/**
	 * Get's Players Game
	 * @param p - Player
	 * @return Player's game
	 */
	public static Game getGame(Player p) {
		for (Game g : GameList)
			for (Arena a : g.arenas)
				if (a.getPlayers().contains(p.getUniqueId()))
					return g;
		return null;
	}

	/**
	 * Static method to get a Game by Arena
	 * @param a - Arena to get Game from
	 * @return Game from Arena
	 */
	public static Game getGame(Arena a) {
		for (Game g : GameList)
			for (Arena a2 : g.arenas)
				if (a.equals(a2))
					return g;
		return null;
	}

	/**
	 * Gets Arenas for Game
	 * @return Arenas for Game
	 */
	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	/**
	 * Set whether the game should use a time limit, if not, the game will have
	 * to stop itself.
	 * 
	 * @param b Use time?
	 */
	public void useTime(boolean b) {
		this.useTime = b;
	}

	public boolean usesTime() {
		return useTime;
	}

	/**
	 * Time for game.
	 * 
	 * @param i time in minutes.
	 */
	public void setTime(int i) {
		this.time = i;
	}

	/**
	 * Time for game.
	 * 
	 * @return time in minutes.
	 */
	public int getTime() {
		return time;
	}
	
	public static boolean gameEquals(Player p, String name){
		return isInGame(p) && gameExist(name) && getGame(p).getName().equals(name);
	}
}